import java.util.Locale

plugins {
    id("dev.kikugie.stonecutter")
    kotlin("jvm") apply false
    id("com.google.devtools.ksp") apply false
    id("dev.kikugie.fletching-table.fabric") apply false
    id("me.modmuss50.mod-publish-plugin") apply false
}

stonecutter active "26.3-fabric"

stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)
    val versionFormatted = version.replace(".", "_")
    val loaderFormatted = loader.replaceFirstChar { it.uppercase(Locale.getDefault()) }
    val modId = properties.get<String>("mod.id")
    val modGroup = properties.get<String>("mod.group")

    properties {
        tags(version, loader)
    }

    constants {
        match(loader, "fabric", "neoforge")
    }

    swaps["mod_id"] = "\"${modId}\";"
    swaps["version_util_import"] = "import ${modGroup}.${modId}.platform.version.Util${versionFormatted};"
    swaps["version_util_inst"] = "new Util${versionFormatted}();"
    swaps["loader_util_import"] = "import ${modGroup}.${modId}.platform.${loader}.${loaderFormatted}LoaderUtil;"
    swaps["loader_util_inst"] = "new ${loaderFormatted}LoaderUtil();"
    constants["release"] = properties.get<String>("mod.id") != "template"
    dependencies["fapi"] = properties.getOrNull<String>("deps.fabric_api") ?: "0"

    replacements {
        filters.exclude("**/*.ct")
        string(current.parsed >= "1.21.11") {
            replace("ValidatedIdentifier", "ValidatedIdentifier")
            replace("ResourceLocation", "Identifier")
            replace("location()", "identifier()")
            replace("net.ramixin.mixson_backport", "net.ramixin.mixson")
        }
    }
}
