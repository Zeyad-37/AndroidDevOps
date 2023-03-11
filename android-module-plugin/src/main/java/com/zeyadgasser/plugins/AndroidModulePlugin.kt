package com.zeyadgasser.plugins


import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.builder.core.DefaultApiVersion
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

const val COMPILE_SDK_VERSION = 33
const val MIN_SDK_VERSION = 24
const val TARGET_SDK_VERSION = 33
const val LIFECYCLE_VERSION = "2.5.1"
const val NAV_VERSION = "2.5.3"
const val HILT_VERSION = "2.44.2"
const val COMPOSE_UI_VERSION = "1.3.3"

class AndroidModulePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        val isAppModule = path.split("/").last() == ":app"
        plugins.apply(
            if (isAppModule) "com.android.application"
            else "com.android.library"
        )
        plugins.apply("kotlin-android")
        plugins.apply("kotlin-parcelize")
        plugins.apply("kotlin-kapt")
        plugins.apply("com.google.dagger.hilt.android")
        apply<TestingPlugin>()
        apply<KoveragePlugin>()
        apply<DetektPlugin>()
        if (isAppModule) extensions.configure<ApplicationExtension>("android") {
            lint {
                checkDependencies = true
                abortOnError = false
                file("$rootDir/config/lint/lint-baseline.xml")
                    .takeIf { it.exists() }?.let { baseline = it }
            }
        }
        else extensions.configure<LibraryExtension>("android") {
            lint {
                checkDependencies = true
                abortOnError = false
                file("$rootDir/config/lint/lint-baseline.xml")
                    .takeIf { it.exists() }?.let { baseline = it }
            }
        }
        with(android()) {
            compileSdkVersion(COMPILE_SDK_VERSION)
            buildToolsVersion("33.0.1")
            defaultConfig {
                minSdkVersion = DefaultApiVersion(MIN_SDK_VERSION)
                targetSdkVersion = DefaultApiVersion(TARGET_SDK_VERSION)
                versionCode = getVersionCode()
                versionName = getVersionName()
                vectorDrawables.useSupportLibrary = true
            }
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = "1.4.2"
            buildTypes {
                getByName("debug") {
                    isDebuggable = true
                }
                getByName("release") {
                    isMinifyEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"
                    )
                }
            }
            compileOptions.sourceCompatibility = JavaVersion.VERSION_11
            compileOptions.targetCompatibility = JavaVersion.VERSION_11
            testOptions.unitTests.isReturnDefaultValues = true
            packagingOptions {
                exclude("META-INF/DEPENDENCIES")
                exclude("META-INF/NOTICE")
                exclude("META-INF/LICENSE")
            }
        }
        setupIfApplication()
        addDependencies()
        extensions.configure<KaptExtension>("kapt") { correctErrorTypes = true }
    }

    private fun Project.addDependencies() {
        dependencies.add("implementation", "androidx.core:core-ktx:1.9.0")
        dependencies.add("implementation", "androidx.appcompat:appcompat:1.6.1")
        dependencies.add("implementation", "com.google.android.material:material:1.8.0")
        hilt()
        navigate()
        compose()
        lifecycle()
    }

    private fun Project.lifecycle() {
        dependencies.add(
            "implementation", "androidx.lifecycle:lifecycle-viewmodel-compose:$LIFECYCLE_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.lifecycle:lifecycle-runtime-ktx:$LIFECYCLE_VERSION"
        )
    }

    private fun Project.compose() {
        dependencies.add(
            "debugImplementation", "androidx.compose.ui:ui-tooling:$COMPOSE_UI_VERSION"
        )
        dependencies.add(
            "debugImplementation", "androidx.compose.ui:ui-test-manifest:$COMPOSE_UI_VERSION"
        )
        dependencies.add("implementation", "androidx.activity:activity-compose:1.6.1")
        dependencies.add(
            "implementation", "androidx.compose.ui:ui:$COMPOSE_UI_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.compose.ui:ui-tooling-preview:$COMPOSE_UI_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.compose.ui:ui-viewbinding:$COMPOSE_UI_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.compose.runtime:runtime:$COMPOSE_UI_VERSION"
        )
        dependencies.add("implementation", "androidx.compose.foundation:foundation:1.3.1")
        dependencies.add("implementation", "androidx.compose.material:material:1.3.1")
    }

    private fun Project.navigate() {
        dependencies.add(
            "implementation", "androidx.navigation:navigation-compose:$NAV_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.navigation:navigation-fragment:$NAV_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.navigation:navigation-fragment-ktx:$NAV_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.navigation:navigation-ui:$NAV_VERSION"
        )
        dependencies.add(
            "implementation", "androidx.navigation:navigation-ui-ktx:$NAV_VERSION"
        )
        dependencies.add("implementation", "androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    }

    private fun Project.hilt() {
        dependencies.add(
            "implementation", "com.google.dagger:hilt-android:$HILT_VERSION"
        )
        dependencies.add(
            "kapt", "com.google.dagger:hilt-compiler:$HILT_VERSION"
        )
    }

    private fun Project.setupIfApplication() = plugins.withId("com.android.application") {
        configure<AppExtension> {
            applicationVariants.configureEach variant@{
                assembleProvider.configure {
                    doLast {
                        check(buildType.isDebuggable || buildType.isMinifyEnabled) {
                            "The build '$${this@variant.name}' is not minified. It must not be released!"
                        }
                    }
                }
            }
        }
    }

    private fun Project.android(): BaseExtension =
        project.extensions.findByType(BaseExtension::class.java)
            ?: throw GradleException("Project $name is not an Android project")

    private fun getVersionCode() = 1

    private fun getVersionName() = "1.0.0"
}
