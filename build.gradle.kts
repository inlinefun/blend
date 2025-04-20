import com.github.gradle.node.npm.task.NpmInstallTask
import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.gradle.node)
}

val webappDir = "webapp"
version = "6.0"
group = "blend"

repositories {
	mavenCentral()
}

dependencies {

	minecraft(libs.minecraft)
	mappings(libs.yarn)
	modImplementation(libs.fabric.loader)
	modImplementation(libs.fabric.language.kotlin)

	library(libs.server.ktor.core)
	library(libs.server.ktor.cors)
	library(libs.server.ktor.netty)
	library(libs.server.ktor.websockets)
	library(libs.server.ktor.serialization.json)
	library(libs.server.ktor.content.negotation)

}

tasks.withType<ProcessResources>() {
	dependsOn("buildWebApp")

	from("webapp/dist/") {
		into("/assets/static/")
	}

	filesMatching("fabric.mod.json") {
		expand(mapOf(
			"version" to project.version,
			"minecraft" to libs.versions.minecraft.get(),
			"fabric_loader" to libs.versions.loader.get(),
			"fabric_kotlin" to libs.versions.fabric.kotlin.get()
		))
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(21)
}

tasks.withType<KotlinCompile>().configureEach {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_21)
	}
}

tasks.register<NpmInstallTask>("installPackages") {
	workingDir = file(webappDir)
	inputs.file("$webappDir/package.json")
	outputs.dir("$webappDir/node_modules")
}

tasks.register<NpmTask>("buildWebApp") {
	dependsOn("installPackages")
	workingDir = file(webappDir)
	args = listOf<String>("run", "build")
	inputs.files("$webappDir/index.html", "$webappDir/src/")
	outputs.dir("$webappDir/dist")
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

afterEvaluate {
	configurations.runtimeClasspath.get().resolvedConfiguration.resolvedArtifacts.filter {
		val group = it.moduleVersion.id.group
		group.startsWith("io.ktor") || group.startsWith("io.netty") // filter required transitive dependencies
	}.forEach { artifact ->
		val notation = "${artifact.moduleVersion.id.group}:${artifact.moduleVersion.id.name}:${artifact.moduleVersion.id.version}"
		dependencies.include(notation)
	}
}
fun DependencyHandler.library(lib: Provider<MinimalExternalModuleDependency>) {
	val dependency = implementation(lib.get())
	include(dependency ?: error("Dependency was null (?)"))
}