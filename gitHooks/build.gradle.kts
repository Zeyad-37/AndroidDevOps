plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

pluginBundle {
    website = "https://github.com/Zeyad-37/AndroidDevOps"
    vcsUrl = "https://github.com/Zeyad-37/AndroidDevOps.git"
    tags = listOf("devOps", "git-hooks", "git")
}

group = "com.zeyadgasser"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("gitHooksPlugin") {
            id = "com.zeyadgasser.gradle.plugins.git-hooks-plugin"
            implementationClass = "com.zeyadgasser.plugins.GitHooksPlugin"
            displayName = "GitHooksPlugin"
            description = "Plugin installs git hooks from config file"
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
