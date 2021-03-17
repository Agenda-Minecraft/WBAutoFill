import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "kiwi.cat"
version = "1.3.5-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url="https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    implementation(files("jars/WorldBorder.jar"))
    testImplementation(kotlin("test-junit"))
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("WBAutoFill")
        dependencies {
            exclude(dependency("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT"))
            exclude("WorldBorder.jar")
        }
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}