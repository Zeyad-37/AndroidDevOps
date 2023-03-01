package com.zeyadgasser.plugins

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        plugins.apply("io.gitlab.arturbosch.detekt")
        configure<DetektExtension> {
            ignoreFailures = true
//            config.from(files("$rootDir/config/detekt/detekt.yml"))
//            baseline = file("$projectDir/detekt-baseline.xml")
        }
        dependencies {
//            "detektPlugins"(CoreLib.Detekt.glovoRules)
        }
        val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
            output.set(layout.buildDirectory.file("reports/detekt/merge.sarif"))
        }
        tasks.withType<Detekt>().configureEach {
            buildUponDefaultConfig = true
//        baseline.set(file("$rootDir/config/detekt/baseline.xml"))
            jvmTarget = JavaVersion.VERSION_11.toString()
            reports {
                html.required.set(true)
                sarif.required.set(true)
            }
            basePath = rootDir.absolutePath
            finalizedBy(detektReportMergeSarif)
        }
        detektReportMergeSarif { input.from(tasks.withType<Detekt>().map { it.sarifReportFile }) }
        tasks.withType<DetektCreateBaselineTask>()
            .configureEach { jvmTarget = JavaVersion.VERSION_11.toString() }
    }
}
