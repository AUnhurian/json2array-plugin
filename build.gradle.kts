plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "unhurian"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20210307")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("PS-2024.2.5")
    type.set("IU")
    plugins.set(listOf("com.jetbrains.php"))
}

tasks {
    patchPluginXml {
        changeNotes.set("First version of JSON to PHP Array converter")
    }
}
