plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
	id("dev.kikugie.fletching-table") version "0.1.0-alpha.22"
	kotlin("jvm") version "2.2.10"
	id("com.google.devtools.ksp") version "2.2.10-2.0.2"
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeVersionRange = "[${prop("deps.minecraft")}]"
		}
		required("neoforge") {
			forgeVersionRange = "[1,)"
		}
		required("fzzy_config") {
			slug("fzzy-config")
			forgeVersionRange = "[0,)"
		}
	}
}

stonecutter {
	val dir = eval(current.version, ">1.21.10")
	replacements.string {
		direction = dir
		replace("ValidatedIdentifier", "ValidatedIdentifier")
	}
	replacements.string {
		direction = dir
		replace("ResourceLocation", "Identifier")
	}
}

fletchingTable {
	mixins.create("main") {
		mixin("default", "${prop("mod.id")}.mixins.json")
	}
}

val devOnly by sourceSets.creating

val curiosRun by sourceSets.creating {
	runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

val accessoriesRun by sourceSets.creating {
	runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

neoForge {
	version = property("deps.neoforge") as String

	if (hasProperty("deps.parchment")) {
		parchment {
			val (mc, ver) = (property("deps.parchment") as String).split(':')
			mappingsVersion = ver
			minecraftVersion = mc
		}
	}

	addModdingDependenciesTo(curiosRun)
	addModdingDependenciesTo(accessoriesRun)

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${prop("deps.minecraft")})"
			programArgument("--username=Dev")
		}
		propOrNull("deps.curios")?.let {
			register("clientCurios") {
				client()
				gameDirectory = file("run/curios")
				ideName = "NeoForge Client + Curios (${prop("deps.minecraft")})"
				programArgument("--username=Dev")
				sourceSet = curiosRun
			}
		}
		propOrNull("deps.accessories")?.let {
			register("clientAccessories") {
				client()
				gameDirectory = file("run/accessories")
				ideName = "NeoForge Client + Accessories (${prop("deps.minecraft")})"
				programArgument("--username=Dev")
				sourceSet = accessoriesRun
			}
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${prop("deps.minecraft")})"
		}
	}

	mods {
		register(property("mod.id") as String) {
			sourceSet(sourceSets["main"])
			sourceSet(devOnly)
		}
	}
}

tasks.named("processResources") {
	dependsOn(
		"kspDevOnlyKotlin",
		"kspCuriosRunKotlin",
		"kspAccessoriesRunKotlin",
	)
}

repositories {
	maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
	maven("https://maven.fzzyhmstrs.me/") { name = "Fzzy Config" }
	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "KotlinForForge" }
	maven("https://maven.wispforest.io/releases") { name = "Wisp Forest" }
	maven("https://maven.su5ed.dev/releases") { name = "su5ed" }
	maven("https://jitpack.io") { name = "Jitpack" }
	exclusiveContent {
		forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
		filter { includeGroup("maven.modrinth") }
	}
}

dependencies {
	implementation("me.fzzyhmstrs:fzzy_config:${prop("deps.fzzy_config")}+neoforge")
	implementation("com.moulberry:mixinconstraints:${prop("deps.mixinconstraints")}")
	jarJar("com.moulberry:mixinconstraints:${prop("deps.mixinconstraints")}")

	compileOnly("io.wispforest:accessories-neoforge:${prop("deps.accessories")}")
	compileOnlyApi("org.sinytra.forgified-fabric-api:fabric-api-base:0.4.42+d1308dedd1") {
		exclude(group = "fabric-api")
	}

	propOrNull("deps.curios")?.let { version ->
		add(curiosRun.runtimeOnlyConfigurationName, "maven.modrinth:curios:$version")
	}

	propOrNull("deps.accessories")?.let { version ->
		add(accessoriesRun.runtimeOnlyConfigurationName, "io.wispforest:accessories-neoforge:$version")
	}
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
