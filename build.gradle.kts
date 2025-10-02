plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "unhurian"
version = "1.0.4"

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
    version.set("PS-2025.2.2")
    type.set("IU")
    plugins.set(listOf("com.jetbrains.php", "com.intellij.modules.json"))
}

tasks {
    patchPluginXml {
        changeNotes.set("Implement possibility convert to JSON with pretty print")
        sinceBuild.set("241")
    }
    
    buildSearchableOptions {
        enabled = false
    }
    
    test {
        enabled = false
    }
}
