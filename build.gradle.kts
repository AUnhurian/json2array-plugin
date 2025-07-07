plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "unhurian"
version = "1.0.3"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.json:json:20210307")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("PS-2025.1.3")
    type.set("IU")
    plugins.set(listOf("com.jetbrains.php", "com.intellij.modules.json"))
}

tasks {
    patchPluginXml {
        changeNotes.set("Updated for PhpStorm 2025.1.3 compatibility. Fixed indentation for objects in the JSON array.")
        sinceBuild.set("241")
        untilBuild.set("999.*")
    }
    
    buildSearchableOptions {
        enabled = false
    }
    
    test {
        enabled = false
    }
}
