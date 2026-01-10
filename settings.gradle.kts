pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/")
		mavenCentral()
		gradlePluginPortal()
	}
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

if (file("Ponder").exists()) {
	includeBuild(".")
	includeBuild("Ponder") {
		dependencySubstitution {
			substitute(module("net.createmod.ponder:Ponder-Fabric-1.20.1")).using(project(":Fabric"))
			substitute(module("net.createmod.ponder:Ponder-Common-1.20.1")).using(project(":Common"))
		}
	}
}