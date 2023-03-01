package com.zeyadgasser.plugins

import kotlinx.kover.api.DefaultIntellijEngine
import kotlinx.kover.api.KoverProjectConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KoveragePlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        plugins.apply("org.jetbrains.kotlinx.kover")
        extensions.configure(KoverProjectConfig::class) {
            isDisabled.set(false)
            // to change engine, use kotlinx.kover.api.IntellijEngine("xxx") or kotlinx.kover.api.JacocoEngine("xxx")
            engine.set(DefaultIntellijEngine)
            // common filters for all default Kover tasks
            filters {
                // common class filter for all default Kover tasks in this project
                classes {
                    // class inclusion rules
                    includes += "com.zeyadgasser.*"
                    // class exclusion rules
                    excludes += "*.databinding.*"
                    excludes += "android/**/*.class"
                    excludes += "**/R.class"
                    excludes += "**/R$*.class"
                    excludes += "**/BuildConfig.class"
                    excludes += "**/Manifest*.class"
                    excludes += "**/*Module.class" // dagger modules
                    excludes += "**/*Module_*.class" // dagger modules providers
                    excludes += "**/*Module\$Companion.class" // dagger modules's companion
                    excludes += "**/*Module_Companion_*.class" // dagger modules's companion providers
                }
            }
            instrumentation {
                // set of test tasks names to exclude from instrumentation. The results of their execution will not be presented in the report
                excludeTasks += "dummy-tests"
            }
            htmlReport {
                // set to true to run koverHtmlReport task during the execution of the check task (if it exists) of the current project
                onCheck.set(false)
                // change report directory
                reportDir.set(layout.buildDirectory.dir("reports/kover/html-result"))
            }
        }
    }
}
