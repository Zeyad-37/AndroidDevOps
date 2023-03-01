import com.zeyadgasser.plugins.GitHooksPlugin

plugins {
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.16.0"
}
apply<GitHooksPlugin>()

pluginBundle {
    website = "<substitute your project website>"
    vcsUrl = "<uri to project source repository>"
    tags = listOf("tags", "for", "your", "plugins")
}
