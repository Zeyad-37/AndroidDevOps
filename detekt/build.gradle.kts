plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation(plugin("io.gitlab.arturbosch.detekt", "1.22.0"))
    implementation("com.android.tools.build:gradle:8.0.1")
}

group = "com.zeyadgasser"
version = "1.0.2"

gradlePlugin {
    website.set("https://github.com/Zeyad-37/AndroidDevOps")
    vcsUrl.set("https://github.com/Zeyad-37/AndroidDevOps.git")
    plugins {
        create("detektPlugin") {
            id = "com.zeyadgasser.gradle.plugins.detekt-plugin"
            implementationClass = "com.zeyadgasser.plugins.DetektPlugin"
            displayName = "Detekt Plugin"
            description = "Plugin configures detekt to kotlin project"
            tags.set(listOf("devOps", "android", "kotlin", "static-analysis"))
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
