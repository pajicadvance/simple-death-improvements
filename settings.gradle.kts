pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        maven("https://repo.codemc.io/repository/relativitymc/") { name = "RelativityMC" }
    }
    plugins {
        kotlin("jvm") version "2.3.21"
        id("com.google.devtools.ksp") version "2.3.10"
        id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
        id("me.modmuss50.mod-publish-plugin") version "2.1.1"
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
    id("dev.kikugie.loom-back-compat") version "0.4"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        fun match(project: String, vararg loaders: String, version: String = project) {
            for (loader in loaders) version("$project-$loader", version).buildscript("build.$loader.gradle.kts")
        }
        match("1.21.1", "fabric", "neoforge")
        match("26.1", "fabric", "neoforge", version = "26.1.2")
        match("26.2", "fabric", "neoforge")
        match("26.3", "fabric")
        vcsVersion = "26.3-fabric"
    }
}

rootProject.name = "Simple Death Improvements"
