plugins {
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.loom)
}

val buildNum = providers.environmentVariable("GITHUB_RUN_NUMBER")
    .filter(String::isNotEmpty)
    .map { "build.$it" }
    .getOrElse("local")


val mcVer: String = libs.versions.minecraft.get()
val modVersion: String = libs.versions.version.get()

version = "$modVersion-$buildNum+$mcVer-fabric"

group = libs.versions.group.get()

repositories {
    fun mavenProviding(url: String, vararg groups: String) {
        exclusiveContent {
            forRepositories(maven(url)).filter {
                for (group in groups) {
                    includeGroupAndSubgroups(group)
                }
            }
        }
    }

    mavenProviding("https://maven.parchmentmc.org", "org.parchmentmc.data") // Parchment mappings
    mavenProviding("https://maven.createmod.net") // Create, Flywheel, Ponder
    mavenProviding("https://modmaven.dev", "vazkii.botania")
    mavenProviding("https://api.modrinth.com/maven", "maven.modrinth") 	// LazyDFU
    mavenProviding("https://mvn.devos.one/snapshots") // Create (snapshots), Registrate, Milk Lib, Dripstone Fluid Lib
    mavenProviding("https://mvn.devos.one/releases") // Create (releases), Porting Lib
    mavenProviding("https://maven.jamieswhiteshirt.com/libs-release", "com.jamieswhiteshirt") // Reach Entity Attributes
    mavenProviding("https://maven.terraformersmc.com", "com.terraformersmc", "dev.emi") // Mod Menu
    mavenProviding("https://maven.blamejared.com", "mezz.jei")
    mavenProviding("https://maven.architectury.dev", "dev.architectury", "me.shedaniel")
    mavenProviding("https://raw.githubusercontent.com/Fuzss/modresources/main/maven", "fuzs.forgeconfigapiport")
}

val modApiInclude: Configuration by configurations.dependencyScope("modApiInclude")
val apiInclude: Configuration by configurations.dependencyScope("apiInclude")

configurations.modApi { extendsFrom(modApiInclude) }
configurations.api { extendsFrom(apiInclude) }
configurations.include { extendsFrom(modApiInclude, apiInclude) }

val recipeViewer = libs.versions.recipeViewer.get()

dependencies {

    // setup
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings { nameSyntheticMembers = false }
        parchment(libs.parchment)
    })
    modImplementation(libs.bundles.fabric)
    // create is only needed at compile time in this setup — avoid resolving the missing runtime artifact during configuration
    // Use compile-only for Create to avoid forcing resolution of a runtime snapshot during configuration
    modImplementation(libs.create)
    modApiInclude(libs.bundles.porting.lib)

    // dependencies
    modApiInclude(libs.registrate) {
        // avoid duplicate Porting Lib
        exclude(group = "io.github.fabricators_of_create.Porting-Lib")
    }


    modApiInclude(libs.flywheel)
    modApiInclude(libs.flywheel.api)
//    modApiInclude(libs.ponder)
//    modApiInclude(libs.ponder.commun)
    modApiInclude(libs.bundles.config)
    modApiInclude(libs.rea)
    modApiInclude(libs.milk)
//    apiInclude(libs.jsr305)

    // compat
    modCompileOnly(libs.modmenu)

    modCompileOnly(libs.jei) { isTransitive = false }
    modCompileOnly(libs.bundles.rei)
    modCompileOnly(variantOf(libs.emi) {
        classifier("api")
    })

    when (recipeViewer) {
        "jei" -> modLocalRuntime(libs.jei)
        "rei" -> modLocalRuntime(libs.rei.runtime)
        "emi" -> modLocalRuntime(libs.emi)
    }

    // dev env
    modLocalRuntime(libs.modmenu)

    // have deprecated modules present at runtime only for some older mods
    modLocalRuntime(libs.fabric.api.deprecated)

    // other mods easily toggleable at runtime for testing
}

java {
    withSourcesJar()
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

tasks.named<JavaCompile>("compileJava") {
    // this makes it possible to actually count errors after a big merge, since by default only 100 are shown
    options.compilerArgs.add("-Xmaxerrs")
    options.compilerArgs.add("10000")
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
            exclude("src/generated/resources/.cache")
        }
    }
}


loom {
	accessWidenerPath = file("src/main/resources/createnuclear.accesswidener")
	
	runs {
        register("datagen") {
			client()

			name("Data Generator")
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=${file("src/generated/resources")}")
			vmArg("-Dfabric-api.datagen.modid=createnuclear")
			vmArg("-Dporting_lib.datagen.existing_resources=${file("src/main/resources")}")
		}

        named("server") {
            runDir("run/server")
        }

        configureEach {
            vmArg("-XX:+AllowEnhancedClassRedefinition")
            vmArg("-XX:+IgnoreUnrecognizedVMOptions")
            property("mixin.debug.export", "true")
        }
	}
}

tasks.named<ProcessResources>("processResources") {
    exclude("**/*.bbmodel", "**/*.lnk")

    val properties: MutableMap<String, Any> = mutableMapOf(
        "version" to version,
        "minecraft_version" to libs.versions.minecraft.get(),
        "loader_version" to libs.versions.fabric.loader.get(),
        "fabric_version" to libs.versions.fabric.api.get(),
        "create_version" to libs.versions.create.get(),
        "jei_version" to libs.versions.jei.get(),
        "rei_version" to libs.versions.rei.get(),
        "emi_version" to libs.versions.emi.get(),
        "forge_config_api_port_version" to libs.versions.forge.config.get(),
        "mod_id" to "createnuclear",
        "mod_name" to "Create Nuclear",
        "mod_description" to "Create nuclear is a mod that adds a whole nuclear system to the Create mod. An optimized reactor produces over eight million SU.",
        "mod_author" to "Create Nuclear Team",
        "mod_homepage" to "https://www.curseforge.com/minecraft/mc-mods/create-nuclear",
        "mod_sources" to "https://github.com/Create-Nuclear-Team/CreateNuclearFabric",
        "mod_issues" to "https://github.com/Create-Nuclear-Team/CreateNuclearFabric/issues",
        "mod_license" to "MIT",
    )

    for (module: MinimalExternalModuleDependency in libs.bundles.porting.lib.get()) {
        val name = module.module.name
        val version: String = if (name == "tags") { "3.0" } else { libs.versions.porting.lib.get() }
        properties["port_lib_${name}_version"] = version
    }

    filesMatching("fabric.mod.json") {
        expand(properties)
    }
}

// configure the maven publication
publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}