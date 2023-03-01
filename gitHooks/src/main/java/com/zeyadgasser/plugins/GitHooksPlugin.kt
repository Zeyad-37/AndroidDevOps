package com.zeyadgasser.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

class GitHooksPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        check(project == rootProject) { "This plugin can only be applied at root project" }
        val isCI = !System.getenv("CI").isNullOrEmpty()
        if (!isCI && isUnixOs()) {
            file("${rootDir}/config/git-hooks/")
                .copyRecursively(file("${rootDir}/.git/hooks/"), overwrite = true)
            exec {
                workingDir = project.rootDir
                commandLine("chmod")
                args("-R", "+x", ".git/hooks/")
                logger.info("Git hooks installed successfully.")
            }
        }
    }

    private fun isUnixOs(): Boolean = DefaultNativePlatform.getCurrentOperatingSystem()
        .run { isMacOsX || isLinux || isFreeBSD || isSolaris }
}
