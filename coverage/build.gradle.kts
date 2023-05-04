plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation(plugin("org.jetbrains.kotlinx.kover", "0.6.1"))
    implementation("com.android.tools.build:gradle:8.0.1")
}

group = "com.zeyadgasser"
version = "1.0.2"

gradlePlugin {
    website.set("https://github.com/Zeyad-37/AndroidDevOps")
    vcsUrl.set("https://github.com/Zeyad-37/AndroidDevOps.git")
    plugins {
        create("KoveragePlugin") {
            id = "com.zeyadgasser.gradle.plugins.koverage"
            implementationClass = "com.zeyadgasser.plugins.KoveragePlugin"
            displayName = "Koverage Plugin"
            description = "Plugin for setting up Kover for kotlin modules"
            tags.set(listOf("devOps", "android", "kotlin", "test-coverage"))
        }
    }
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}

fun DependencyHandler.plugin(id: String, version: String) = create("$id:$id.gradle.plugin:$version")
