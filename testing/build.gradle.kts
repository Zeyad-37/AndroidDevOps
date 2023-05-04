plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation("com.android.tools.build:gradle:8.0.1")
}

group = "com.zeyadgasser"
version = "1.0.7"

gradlePlugin {
    website.set("https://github.com/Zeyad-37/AndroidDevOps")
    vcsUrl.set("https://github.com/Zeyad-37/AndroidDevOps.git")
    plugins {
        create("TestingPlugin") {
            id = "com.zeyadgasser.gradle.plugins.testing"
            implementationClass = "com.zeyadgasser.plugins.TestingPlugin"
            displayName = "Testing Plugin"
            description = "Plugin for setting up testing for kotlin modules"
            tags.set(listOf("devOps", "android", "kotlin", "testing"))
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
