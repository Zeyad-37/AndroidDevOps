package com.zeyadgasser.plugins

import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet

@Suppress("LeakingThis")
open class CoveragePluginExtension : PatternFilterable by PatternSet() {

    init {
        exclude("android/**/*.class")
        exclude("**/R.class")
        exclude("**/R$*.class")
        exclude("**/BuildConfig.class")
        exclude("**/Manifest*.class")

        exclude("**/*Module.class") // dagger modules
        exclude("**/*Module_*.class") // dagger modules providers
        exclude("**/*Module\$Companion.class") // dagger modules's companion
        exclude("**/*Module_Companion_*.class") // dagger modules's companion providers
       }

}
