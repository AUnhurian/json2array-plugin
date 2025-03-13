plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "unhurian"
version = "1.0.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20210307")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("PS-2024.2.5")
    type.set("IU")
    plugins.set(listOf("com.jetbrains.php"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
//    toolchain.vendor.set(JvmVendorSpec.ANY)
}

tasks {
    patchPluginXml {
        changeNotes.set("Fixed indentation for objects in the JSON array.")
    }
}
