plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    `kotlin-dsl`
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.1.0"
    `ivy-publish`
    id("java-gradle-plugin")  // Allows us to create and configure plugin
    id("kotlin") //We'll write our plugin in Kotlin

}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.1")
    implementation("com.android.tools.build:gradle-api:7.4.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
}
