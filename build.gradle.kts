import com.zeyadgasser.plugins.GitHooksPlugin

plugins {
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.1.0"
    `ivy-publish`
}
apply<GitHooksPlugin>()

pluginBundle {
    website = "https://github.com/Zeyad-37/AndroidDevOps"
    vcsUrl = "https://github.com/Zeyad-37/AndroidDevOps.git"
    tags = listOf("devOps", "android", "gradle", "github-actions", "gradle-plugins", "kotlin")
}

group = "io.github.zeyadgasser"
version = "1.0"

gradlePlugin {
    plugins {
        create("androidModulePlugin") {
            id = "com.zeyadgasser.gradle.plugin.android-module-plugin"
            implementationClass = "com.zeyadgasser.plugins.AndroidModulePlugin"
            displayName = "AndroidModulePlugin"
            description = "Plugin for setting up android modules"
        }
    }
    plugins {
        create("gitHooksPlugin") {
            id = "com.zeyadgasser.gradle.plugin.git-hooks-plugin"
            implementationClass = "com.zeyadgasser.plugins.GitHooksPlugin"
            displayName = "GitHooksPlugin"
            description = "Plugin install git hooks from config file"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}
