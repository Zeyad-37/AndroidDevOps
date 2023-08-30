plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation(project(":testing"))
    implementation(project(":coverage"))
    implementation(project(":detekt"))
    implementation("com.android.tools.build:gradle:8.0.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
}

group = "com.zeyadgasser"
version = "1.1.3"

gradlePlugin {
    website.set("https://github.com/Zeyad-37/AndroidDevOps")
    vcsUrl.set("https://github.com/Zeyad-37/AndroidDevOps.git")
    plugins {
        create("AndroidModulePlugin") {
            id = "com.zeyadgasser.gradle.plugins.android-module-plugin"
            implementationClass = "com.zeyadgasser.plugins.AndroidModulePlugin"
            displayName = "Android Module Plugin"
            description = "Plugin for setting up Android modules (App & Lib)"
            tags.set(listOf("android", "kotlin"))
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
