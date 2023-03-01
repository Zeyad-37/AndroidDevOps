package com.zeyadgasser.plugins

import com.zeyadgasser.utils.AndroidConfig
import com.zeyadgasser.utils.DepVersions.composeUIVersion
import com.zeyadgasser.utils.DepVersions.hiltVersion
import com.zeyadgasser.utils.DepVersions.junit5Version
import com.zeyadgasser.utils.android
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestingPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
//        plugins.apply("de.mannodermaus.android-junit5")
        with(android()) {
            defaultConfig {
                testInstrumentationRunner =
                    AndroidConfig.testInstrumentationRunner
                testInstrumentationRunnerArguments["runnerBuilder"] =
                    AndroidConfig.testInstrumentationRunnerArguments
            }
            testOptions.unitTests.isReturnDefaultValues = true
            packagingOptions {
                exclude("META-INF/DEPENDENCIES")
                exclude("META-INF/NOTICE")
                exclude("META-INF/LICENSE")
            }
        }
        addDependencies()
        setupTestTask()
    }

    private fun Project.addDependencies() {
        junit()
        coroutines()
        mocking()
        ui()
        hilt()
    }

    private fun Project.hilt() {
        dependencies.add(
            "androidTestImplementation",
            "com.google.dagger:hilt-android-testing:$hiltVersion"
        )
        dependencies.add(
            "kaptAndroidTest",
            "com.google.dagger:hilt-android-compiler:$hiltVersion"
        )
    }

    private fun Project.ui() {
        dependencies.add("androidTestImplementation", "androidx.test.ext:junit:1.1.5")
        dependencies.add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.5.1")
        dependencies.add(
            "androidTestImplementation",
            "androidx.compose.ui:ui-test-junit4:$composeUIVersion"
        )
    }

    private fun Project.mocking() {
        dependencies.add("testImplementation", "org.mockito:mockito-core:5.1.1")
        dependencies.add("testImplementation", "org.mockito.kotlin:mockito-kotlin:4.1.0")
    }

    private fun Project.coroutines() {
        dependencies.add(
            "testImplementation",
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"
        )
        dependencies.add("testImplementation", "app.cash.turbine:turbine:0.12.1")
    }

    private fun Project.junit() {
        dependencies.add("testImplementation", "junit:junit:4.13.2")
        dependencies.add(
            "testImplementation",
            "org.junit.jupiter:junit-jupiter:$junit5Version"
        )
        dependencies.add(
            "testImplementation",
            "org.junit.jupiter:junit-jupiter:$junit5Version"
        )
        dependencies.add(
            "testRuntimeOnly",
            "org.junit.jupiter:junit-jupiter-params:$junit5Version"
        )
        dependencies.add(
            "testRuntimeOnly",
            "org.junit.vintage:junit-vintage-engine:$junit5Version"
        )
        dependencies.add(
            "androidTestImplementation", "androidx.test:runner:1.5.2"
        )
        dependencies.add(
            "androidTestImplementation",
            "org.junit.jupiter:junit-jupiter-api:$junit5Version"
        )
        dependencies.add(
            "androidTestImplementation",
            "de.mannodermaus.junit5:android-test-core:1.2.2"
        )
        dependencies.add(
            "androidTestRuntimeOnly",
            "de.mannodermaus.junit5:android-test-runner:1.2.2"
        )
    }

    private fun Project.setupTestTask() {
        tasks.withType(Test::class.java) {
            useJUnitPlatform() {
                excludeTags("slow", "ci")
                includeEngines("junit-jupiter")
            }
        }
    }
}
