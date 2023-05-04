package com.zeyadgasser.plugins

import com.android.build.gradle.BaseExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import java.io.File

class TestingPlugin : Plugin<Project> {

    companion object {
        private const val JUNIT_5_VERSION = "5.9.3"
        private const val HILT_VERSION = "2.46"
        private const val COMPOSE_UI_VERSION = "1.4.3"
    }

    override fun apply(project: Project) = with(project) {
        plugins.apply("kotlin-kapt")
        with(android()) {
            defaultConfig {
                testInstrumentationRunner = "de.mannodermaus.junit5.AndroidJUnit5Builder"
                testInstrumentationRunnerArguments["runnerBuilder"] =
                    "de.mannodermaus.junit5.AndroidJUnit5Builder"
            }
            testOptions.unitTests.isReturnDefaultValues = true
            packagingOptions {
                exclude("META-INF/DEPENDENCIES")
                exclude("META-INF/NOTICE")
                exclude("META-INF/LICENSE")
            }
        }
        createJUnit5PropertiesFile()
        addDependencies()
        setupTestTask()
    }

    private fun Project.createJUnit5PropertiesFile() {
        val resourcesDir = project.file("src/test/resources")
        if (!resourcesDir.exists()) {
            resourcesDir.mkdirs()
        }
        val propertiesFile = File(resourcesDir, "junit-platform.properties")
        if (!propertiesFile.exists()) {
            propertiesFile.createNewFile()
            propertiesFile.writeText("junit.jupiter.testinstace.lifecycle.default=per_class")
        }
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
            "androidTestImplementation", "com.google.dagger:hilt-android-testing:$HILT_VERSION"
        )
        dependencies.add(
            "kaptAndroidTest", "com.google.dagger:hilt-android-compiler:$HILT_VERSION"
        )
    }

    private fun Project.ui() {
        dependencies.add("androidTestImplementation", "androidx.test.ext:junit:1.1.5")
        dependencies.add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.5.1")
        dependencies.add(
            "androidTestImplementation", "androidx.compose.ui:ui-test-junit4:$COMPOSE_UI_VERSION"
        )
    }

    private fun Project.mocking() {
        dependencies.add("testImplementation", "org.mockito:mockito-core:5.3.1")
        dependencies.add("testImplementation", "org.mockito.kotlin:mockito-kotlin:4.1.0")
    }

    private fun Project.coroutines() {
        dependencies.add(
            "testImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"
        )
        dependencies.add("testImplementation", "app.cash.turbine:turbine:0.12.3")
    }

    private fun Project.junit() {
        dependencies.add("testImplementation", "junit:junit:4.13.2")
        dependencies.add(
            "testImplementation", "org.junit.jupiter:junit-jupiter:$JUNIT_5_VERSION"
        )
        dependencies.add(
            "testImplementation", "org.junit.jupiter:junit-jupiter:$JUNIT_5_VERSION"
        )
        dependencies.add(
            "testRuntimeOnly", "org.junit.jupiter:junit-jupiter-params:$JUNIT_5_VERSION"
        )
        dependencies.add(
            "testRuntimeOnly", "org.junit.vintage:junit-vintage-engine:$JUNIT_5_VERSION"
        )
        dependencies.add(
            "androidTestImplementation", "androidx.test:runner:1.5.2"
        )
        dependencies.add(
            "androidTestImplementation", "org.junit.jupiter:junit-jupiter-api:$JUNIT_5_VERSION"
        )
        dependencies.add(
            "androidTestImplementation", "de.mannodermaus.junit5:android-test-core:1.2.2"
        )
        dependencies.add(
            "androidTestRuntimeOnly", "de.mannodermaus.junit5:android-test-runner:1.2.2"
        )
    }

    private fun Project.setupTestTask() {
        tasks.withType(Test::class.java) {
            useJUnitPlatform {
                excludeTags("slow", "ci")
                includeEngines("junit-jupiter")
            }
        }
    }

    private fun Project.android(): BaseExtension =
        project.extensions.findByType(BaseExtension::class.java)
            ?: throw GradleException("Project $name is not an Android project")
}
