plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

group = "com.zeyadgasser"
version = "1.0.1"

gradlePlugin {
    website.set("https://github.com/Zeyad-37/AndroidDevOps")
    vcsUrl.set("https://github.com/Zeyad-37/AndroidDevOps.git")
    plugins {
        create("gitHooksPlugin") {
            id = "com.zeyadgasser.gradle.plugins.git-hooks-plugin"
            implementationClass = "com.zeyadgasser.plugins.GitHooksPlugin"
            displayName = "GitHooksPlugin"
            description = "Plugin installs git hooks from config file"
            tags.set(listOf("devOps", "git-hooks", "git"))
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
