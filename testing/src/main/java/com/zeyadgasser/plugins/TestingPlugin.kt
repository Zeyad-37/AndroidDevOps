package com.zeyadgasser.plugins

import com.android.build.gradle.BaseExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import java.io.File

class TestingPlugin : Plugin<Project> {

    companion object {
        private const val JUNIT_5_VERSION = "5.10.0"
        private const val HILT_VERSION = "2.47"
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
            packagingOptions.setExcludes(
                setOf(
                    "META-INF/LICENSE",
                    "META-INF/NOTICE",
                    "META-INF/DEPENDENCIES",
                    "META-INF/MANIFEST.MF",
                    "META-INF/com.android.tools/proguard/coroutines.pro",
                    "META-INF/proguard/coroutines.pro",
                    "LICENSE.txt",
                )
            )
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
            propertiesFile.writeText("junit.jupiter.testinstance.lifecycle.default=per_class")
        }
    }

    private fun Project.addDependencies() {
        junit()
        coroutines()
        mocking()
        ui()
        hilt()
        testParameterInjector()
        dependencies.add("testImplementation", "org.robolectric:robolectric:4.10.3")
    }

    private fun Project.testParameterInjector() {
        dependencies.add(
            "testImplementation",
            "com.google.testparameterinjector:test-parameter-injector-junit5:1.11"
        )
        dependencies.add(
            "testImplementation", "com.google.testparameterinjector:test-parameter-injector:1.11"
        )
        dependencies.add(
            "androidTestImplementation",
            "com.google.testparameterinjector:test-parameter-injector:1.11"
        )
    }

    private fun Project.hilt() {
        dependencies.add(
            "androidTestImplementation", "com.google.dagger:hilt-android-testing:$HILT_VERSION"
        )
        dependencies.add("kaptAndroidTest", "com.google.dagger:hilt-android-compiler:$HILT_VERSION")
    }

    private fun Project.ui() {
        dependencies.add("androidTestImplementation", "androidx.test.ext:junit:1.1.5")
        dependencies.add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.5.1")
        dependencies.add(
            "androidTestImplementation", "androidx.compose.ui:ui-test-junit4:$COMPOSE_UI_VERSION"
        )
        dependencies.add(
            "debugImplementation", "androidx.compose.ui:ui-test-manifest:$COMPOSE_UI_VERSION"
        )
    }

    private fun Project.mocking() {
        dependencies.add("testImplementation", "org.mockito:mockito-core:5.5.0")
        dependencies.add("testImplementation", "org.mockito.kotlin:mockito-kotlin:5.1.0")
    }

    private fun Project.coroutines() {
        dependencies.add(
            "testImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
        )
        dependencies.add("testImplementation", "app.cash.turbine:turbine:1.0.0")
    }

    private fun Project.junit() {
        dependencies.add("testImplementation", "junit:junit:4.13.2")
        dependencies.add("testImplementation", "org.junit.jupiter:junit-jupiter:$JUNIT_5_VERSION")
        dependencies.add(
            "testImplementation", "org.junit.jupiter:junit-jupiter-params:$JUNIT_5_VERSION"
        )
        dependencies.add(
            "testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:$JUNIT_5_VERSION"
        )
        dependencies.add(
            "testRuntimeOnly", "org.junit.vintage:junit-vintage-engine:$JUNIT_5_VERSION"
        )
        dependencies.add("androidTestImplementation", "androidx.test:runner:1.5.2")
        dependencies.add(
            "testImplementation", "org.junit.jupiter:junit-jupiter-api:$JUNIT_5_VERSION"
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
