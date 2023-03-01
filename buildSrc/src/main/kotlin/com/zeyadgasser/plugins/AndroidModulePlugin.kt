package com.zeyadgasser.plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AppExtension
import com.android.builder.core.DefaultApiVersion
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AndroidModulePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        val isAppModule = path.split("/").last() == ":app"
        plugins.apply(if (isAppModule) "com.android.application" else "com.android.library")
        plugins.apply("kotlin-android")
        plugins.apply("kotlin-parcelize")
        plugins.apply("kotlin-kapt")
        plugins.apply("com.google.dagger.hilt.android")
        apply<TestingPlugin>()
        apply<CoveragePlugin>()
        apply<DetektPlugin>()
        extensions.configure<ApplicationExtension>("android") {
            lint {
                checkDependencies = true
                abortOnError = false
//                baseline = file("lint-baseline.xml")
            }
        }
        with(android()) {
            compileSdkVersion(AndroidVersions.compileSdkVersion)
            buildToolsVersion(AndroidVersions.buildToolsVersion)
            defaultConfig {
                if (isAppModule) applicationId = AndroidConfig.appId
                minSdkVersion = DefaultApiVersion(AndroidVersions.minSdkVersion)
                targetSdkVersion = DefaultApiVersion(AndroidVersions.targetSdkVersion)
                versionCode = AndroidConfig.versionCode
                versionName = AndroidConfig.versionName
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
        extensions.configure<KaptExtension>("kapt") {
            correctErrorTypes = true
        }
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
            "implementation",
            "androidx.lifecycle:lifecycle-viewmodel-compose:${DepVersions.lifecycleVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${DepVersions.lifecycleVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.lifecycle:lifecycle-runtime-ktx:${DepVersions.lifecycleVersion}"
        )
    }

    private fun Project.compose() {
        dependencies.add(
            "debugImplementation",
            "androidx.compose.ui:ui-tooling:${DepVersions.composeUIVersion}"
        )
        dependencies.add(
            "debugImplementation",
            "androidx.compose.ui:ui-test-manifest:${DepVersions.composeUIVersion}"
        )
        dependencies.add("implementation", "androidx.activity:activity-compose:1.6.1")
        dependencies.add(
            "implementation",
            "androidx.compose.ui:ui:${DepVersions.composeUIVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.compose.ui:ui-tooling-preview:${DepVersions.composeUIVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.compose.ui:ui-viewbinding:${DepVersions.composeUIVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.compose.runtime:runtime:${DepVersions.composeUIVersion}"
        )
        dependencies.add("implementation", "androidx.compose.foundation:foundation:1.3.1")
        dependencies.add("implementation", "androidx.compose.material:material:1.3.1")
    }

    private fun Project.navigate() {
        dependencies.add(
            "implementation",
            "androidx.navigation:navigation-compose:${DepVersions.navVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.navigation:navigation-fragment:${DepVersions.navVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.navigation:navigation-fragment-ktx:${DepVersions.navVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.navigation:navigation-ui:${DepVersions.navVersion}"
        )
        dependencies.add(
            "implementation",
            "androidx.navigation:navigation-ui-ktx:${DepVersions.navVersion}"
        )
        dependencies.add("implementation", "androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    }

    private fun Project.hilt() {
        dependencies.add(
            "implementation",
            "com.google.dagger:hilt-android:${DepVersions.hiltVersion}"
        )
        dependencies.add(
            "kapt",
            "com.google.dagger:hilt-compiler:${DepVersions.hiltVersion}"
        )
    }

    private fun Project.setupIfApplication() =
        plugins.withId("com.android.application") { configure<AppExtension> { ensureObfuscations() } }

    private fun AppExtension.ensureObfuscations() {
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
