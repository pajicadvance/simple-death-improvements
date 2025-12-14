plugins {
	id("mod-platform")
	id("fabric-loom")
	id("dev.kikugie.fletching-table") version "0.1.0-alpha.22"
	kotlin("jvm") version "2.2.10"
	id("com.google.devtools.ksp") version "2.2.10-2.0.2"
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			versionRange = prop("deps.minecraft").replace("pre", "beta.")
		}
		required("fabric-api") {
			slug("fabric-api")
			versionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			versionRange = ">=${libs.fabric.loader.get().version}"
		}
		required("fzzy_config") {
			slug("fzzy-config")
			versionRange = "*"
		}
		optional("modmenu") {
			slug("modmenu")
		}
	}
}

loom {
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server"
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

repositories {
	maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
	maven("https://maven.fzzyhmstrs.me/") { name = "Fzzy Config" }
	maven("https://maven.terraformersmc.com/" ) { name = "TerraformersMC" }
	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "KotlinForForge" }
	maven("https://maven.wispforest.io/releases") { name = "Wisp Forest" }
	maven("https://jitpack.io") { name = "Jitpack" }
	exclusiveContent {
		forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
		filter { includeGroup("maven.modrinth") }
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	@Suppress("UnstableApiUsage")
	mappings(
		loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment")) parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	modImplementation(libs.fabric.loader)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	modImplementation("me.fzzyhmstrs:fzzy_config:${prop("deps.fzzy_config")}")
	modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
	implementation("com.moulberry:mixinconstraints:${prop("deps.mixinconstraints")}")
	include("com.moulberry:mixinconstraints:${prop("deps.mixinconstraints")}")

	modImplementation("io.wispforest:accessories-fabric:${prop("deps.accessories")}") {
		exclude(group = "net.fabricmc.fabric-api", module = "fabric-api")
	}
	modImplementation("io.wispforest:owo-lib:${prop("deps.owo")}") {
		exclude(group = "net.fabricmc.fabric-api", module = "fabric-api")
	}
}
