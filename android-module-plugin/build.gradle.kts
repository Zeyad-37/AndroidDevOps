plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation(project(":testing"))
    implementation(project(":coverage"))
    implementation(project(":detekt"))
    implementation(project(":utils"))
    implementation("com.android.tools.build:gradle:7.4.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
}

pluginBundle {
    website = "https://github.com/Zeyad-37/AndroidDevOps"
    vcsUrl = "https://github.com/Zeyad-37/AndroidDevOps.git"
    tags = listOf("devOps", "android", "github-actions", "kotlin")
}

group = "com.zeyadgasser"
version = "1.0"

gradlePlugin {
    plugins {
        create("AndroidModulePlugin") {
            id = "com.zeyadgasser.gradle.plugin.android-module-plugin"
            implementationClass = "com.zeyadgasser.plugins.AndroidModulePlugin"
            displayName = "Android Module Plugin"
            description = "Plugin for setting up Android modules (App & Lib)"
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
