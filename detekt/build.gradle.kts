plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation(project(":utils"))
    implementation(plugin("io.gitlab.arturbosch.detekt", "1.22.0"))
    implementation("com.android.tools.build:gradle:7.4.1")
}

pluginBundle {
    website = "https://github.com/Zeyad-37/AndroidDevOps"
    vcsUrl = "https://github.com/Zeyad-37/AndroidDevOps.git"
    tags = listOf("devOps", "android", "github-actions", "kotlin", "static-analysis")
}

group = "com.zeyadgasser"
version = "1.0"

gradlePlugin {
    plugins {
        create("detektPlugin") {
            id = "com.zeyadgasser.gradle.plugin.detekt-plugin"
            implementationClass = "com.zeyadgasser.plugins.DetektPlugin"
            displayName = "Detekt Plugin"
            description = "Plugin configures detekt to kotlin project"
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
