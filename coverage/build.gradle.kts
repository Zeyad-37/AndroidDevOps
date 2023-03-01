plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation(project(":utils"))
    implementation(plugin("org.jetbrains.kotlinx.kover", "0.6.1"))
    implementation("com.android.tools.build:gradle:7.4.1")
}

pluginBundle {
    website = "https://github.com/Zeyad-37/AndroidDevOps"
    vcsUrl = "https://github.com/Zeyad-37/AndroidDevOps.git"
    tags = listOf("devOps", "android", "github-actions", "kotlin", "test-coverage")
}

group = "com.zeyadgasser"
version = "1.0"

gradlePlugin {
    plugins {
        create("KoveragePlugin") {
            id = "com.zeyadgasser.gradle.plugin.koverage"
            implementationClass = "com.zeyadgasser.plugins.KoveragePlugin"
            displayName = "Koverage Plugin"
            description = "Plugin for setting up Kover for kotlin modules"
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
