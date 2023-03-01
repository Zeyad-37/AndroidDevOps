package com.zeyadgasser.utils//package com.zeyadgasser.plugins

object AndroidVersions {
    const val compileSdkVersion = 33
    const val buildToolsVersion = "33.0.1"
    const val minSdkVersion = 24
    const val targetSdkVersion = 33
}

object AndroidConfig {
    const val appId = "com.zeyadgasser.platform"
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val testInstrumentationRunnerArguments = "de.mannodermaus.junit5.AndroidJUnit5Builder"
    const val testInstrumentationRunner = "de.mannodermaus.junit5.AndroidJUnit5Builder"
//    const val testInstrumentationRunner = "com.zeyadgasser.test_base.FluxTestRunner"
}

object DepVersions {
    const val lifecycleVersion = "2.5.1"
    const val navVersion = "2.5.3"
    const val junit5Version = "5.9.2"
    const val hiltVersion = "2.44.2"
    const val composeUIVersion = "1.3.3"
}
