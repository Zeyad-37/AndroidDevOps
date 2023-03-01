plugins {
    `kotlin-dsl`
}

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.1")
        classpath("com.android.tools.build:gradle-api:7.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
    }
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api("com.android.tools.build:gradle:7.4.1")
    api(plugin("io.gitlab.arturbosch.detekt", version = "1.22.0"))
    api(plugin("org.jetbrains.kotlinx.kover", version = "0.6.1"))
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
}

gradlePlugin {
    plugins {
        create("androidModulePlugin") {
            id = "com.zeyadgasser.gradle.android-module-plugin"
            implementationClass = "com.zeyadgasser.plugins.AndroidModulePlugin"
            displayName = "AndroidModulePlugin"
            description = "Plugin for setting up android modules"
        }
        create("gitHooksPlugin") {
            id = "com.zeyadgasser.gradle.git-hooks-plugin"
            implementationClass = "com.zeyadgasser.plugins.GitHooksPlugin"
            displayName = "GitHooksPlugin"
            description = "Plugin install git hooks from config file"
        }
    }
}

fun DependencyHandler.plugin(id: String, version: String) =
    create("$id:$id.gradle.plugin:$version")
