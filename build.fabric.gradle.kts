@file:Suppress("AvoidDuplicateDependencies")
import me.modmuss50.mpp.platforms.modrinth.ModrinthEnvironment

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table.fabric")
    id("me.modmuss50.mod-publish-plugin")
    id("dev.kikugie.loom-back-compat")
}

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${property("mod.id") as String}-fabric"

repositories {
    mavenCentral()
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
    strictMaven("https://maven.fzzyhmstrs.me/", "Fzzy Config", "me.fzzyhmstrs")
    strictMaven("https://maven.terraformersmc.com/", "TerraformersMC", "com.terraformersmc")
    strictMaven("https://maven.caffeinemc.net/releases", "CaffeineMC", "net.caffeinemc")
    strictMaven("https://maven.su5ed.dev/releases", "Sinytra", "org.sinytra.forgified-fabric-api")
    strictMaven("https://maven.nucleoid.xyz/releases", "Nucleoid")
    strictMaven("https://maven.theillusivec4.top/", "TheIllusiveC4")
    strictMaven("https://repo.sleeping.town/", "Sleeping Town")
    strictMaven("https://maven.ladysnake.org/releases", "Ladysnake Libs")
    ivy {
        url = uri("https://github.com/pajicadvance/Mixson/releases/download/")
        patternLayout {
            artifact("[revision]/mixson_backport-fabric-${sc.current.version}-[revision].[ext]")
        }
        metadataSources { artifact() }
    }
}

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    else -> JavaVersion.VERSION_21
}

val compatibleVersions: List<String> = sc.properties.rawOrNull("mod", "mc_releases")
    ?.asList().orEmpty().map { it.toString() }
val runtimeOptionals: List<String> = sc.properties.rawOrNull("dev", "runtime_optionals")
    ?.asList().orEmpty().map { it.toString() }

data class ModDep(val key: String, val version: String) {
    private fun meta(suffix: String): String? = findProperty("dep.$key.$suffix")?.toString()?.takeIf { it.isNotBlank() }
    val id: String get() = meta("id") ?: key
    val coords: String? get() = meta("coords")?.replace($$"$id", id)?.replace($$"$loader", "fabric")?.replace($$"$mc", sc.current.version)
    val base: String get() = version.substringBefore('+').substringBefore("-beta")
    val range: String get() = meta("range") ?: ">=$base"
    fun slug(platform: String): String = meta("slug.$platform") ?: meta("slug") ?: key
}

val requiredDeps = project.ext.properties
    .filterKeys { it.startsWith("required.") }
    .map { (k, v) -> ModDep(
        key = k.substringAfter('.'),
        version = v.toString()
    ) }.sortedBy { it.key }
val includeDeps = project.ext.properties
    .filterKeys { it.startsWith("include.") }
    .map { (k, v) -> ModDep(
        key = k.substringAfter('.'),
        version = v.toString()
    ) }.sortedBy { it.key }
val optionalDeps = project.ext.properties
    .filterKeys { it.startsWith("optional.") }
    .map { (k, v) -> ModDep(
        key = k.substringAfter('.'),
        version = v.toString()
    ) }.sortedBy { it.key }
val runtimeDeps = project.ext.properties
    .filterKeys { it.startsWith("runtime.") }
    .map { (k, v) -> ModDep(
        key = k.substringAfter('.'),
        version = v.toString()
    ) }.sortedBy { it.key }

fun jsonObject(entries: List<Pair<String, String>>): String =
    if (entries.isEmpty()) "{}"
    else entries.joinToString(",\n    ", "{\n    ", "\n  }") { (k, v) -> "\"$k\": \"$v\"" }

val fabricDepends = jsonObject(
    buildList {
        add("minecraft" to sc.properties["mod.mc_compat"])
        add("fabricloader" to ">=${property("loader.fabric")}")
        add("java" to ">=${requiredJava.majorVersion}")
        requiredDeps.forEach { add(it.id to it.range) }
    }
)
val fabricSuggests = jsonObject(optionalDeps.map { it.id to it.range })

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()
    modImplementation("net.fabricmc:fabric-loader:${property("loader.fabric")}")
    fun ModDep.declare(vararg configurations: String) {
        val notation = (coords ?: return).replace($$"$version", version)
        configurations.forEach {
            conf -> conf(notation) {
                if (id != "fabric-api") exclude(group = "net.fabricmc.fabric-api")
            }
        }
    }
    requiredDeps.forEach { it.declare("modImplementation") }
    includeDeps.forEach { it.declare("modImplementation", "include")}
    optionalDeps.forEach {
        it.declare("modCompileOnly")
        if (runtimeOptionals.contains(it.key)) {
            it.declare("modRuntimeOnly")
        }
    }
    runtimeDeps.forEach { it.declare("modRuntimeOnly") }
}

loom {
    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1")
    }

    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run/${sc.current.version.replace(".", "_")}_fabric/")
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor = JvmVendorSpec.MICROSOFT
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "${property("mod.id")}.mixins.json") {
            env("CLIENT", "${property("mod.group")}.${property("mod.id")}.mixin.client")
        }
    }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, property: String) {
            val value: String = sc.properties[property]
            inputs.property(key, value)
            set(key, value)
        }

        val ct = "aw/${sc.current.project.substringBefore('-')}.ct"
        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        val depends = fabricDepends
        val suggests = fabricSuggests

        val props = buildMap {
            register("id", "mod.id")
            register("group", "mod.group")
            register("name", "mod.name")
            register("version", "mod.version")
            register("minecraft", "mod.mc_compat")
            register("description", "mod.description")
            register("license", "mod.license")
            register("sources_url", "mod.sources_url")
            register("homepage_url", "mod.homepage_url")
            register("issues_url", "mod.issues_url")
            register("discord_url", "mod.discord_url")
            register("authors", "mod.authors")
            register("contributors", "mod.contributors")
            inputs.property("ct", ct)
            put("ct", ct)
            inputs.property("depends", depends)
            inputs.property("suggests", suggests)
        }

        filesMatching("fabric.mod.json") { expand(props) }
        filesMatching("fabric.mod.json") {
            filter { line -> line
                .replace("\"depends\": {}", "\"depends\": $depends")
                .replace("\"suggests\": {}", "\"suggests\": $suggests")
            }
        }

        filesMatching("*.mixins.json") { expand("java" to mixinJava) }

        exclude("META-INF/neoforge.mods.toml")
        exclude { it.path.startsWith("aw/") && it.path != ct }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", project.property("mod.version"))
        from(loomx.modJar.flatMap { it.archiveFile }, loomx.modSourcesJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
    }
}

publishMods {
    file.set(loomx.modJar.get().archiveFile)
    additionalFiles.from(loomx.modSourcesJar.get().archiveFile)
    changelog.set(rootProject.file("CHANGELOG.md").readText())
    type.set(STABLE)
    modLoaders.add("fabric")
    displayName = "${property("mod.version")} for Fabric ${sc.current.version}"
    dryRun = (property("publish.dry_run") as String).toBooleanStrict()

    val mrRequired = requiredDeps.map { it.slug("modrinth") }
    val cfRequired = requiredDeps.map { it.slug("curseforge") }

    modrinth {
        projectId.set("${property("publish.modrinth")}")
        accessToken.set(providers.environmentVariable("MR_KEY"))
        minecraftVersions.addAll(compatibleVersions)
        environment.set(ModrinthEnvironment.valueOf(property("publish.env.mr") as String))
        requires(*mrRequired.toTypedArray())
    }

    curseforge {
        projectId.set("${property("publish.curseforge")}")
        accessToken.set(providers.environmentVariable("CF_KEY"))
        minecraftVersions.addAll(compatibleVersions)
        client = (property("publish.env.cf.client") as String).toBooleanStrict()
        server = (property("publish.env.cf.server") as String).toBooleanStrict()
        requires(*cfRequired.toTypedArray())
    }
}
