plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.1")
}

pluginBundle {
    website = "https://github.com/Zeyad-37/AndroidDevOps"
    vcsUrl = "https://github.com/Zeyad-37/AndroidDevOps.git"
    tags = listOf("devOps", "android", "kotlin", "testing")
}

group = "com.zeyadgasser"
version = "1.0.2"

gradlePlugin {
    plugins {
        create("TestingPlugin") {
            id = "com.zeyadgasser.gradle.plugins.testing"
            implementationClass = "com.zeyadgasser.plugins.TestingPlugin"
            displayName = "Testing Plugin"
            description = "Plugin for setting up testing for kotlin modules"
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
