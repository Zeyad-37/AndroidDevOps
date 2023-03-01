plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.1")
}

fun DependencyHandler.plugin(id: String, version: String) =
    create("$id:$id.gradle.plugin:$version")

