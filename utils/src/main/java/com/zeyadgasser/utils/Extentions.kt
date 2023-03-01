package com.zeyadgasser.utils

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName

val BaseExtension.variants: DomainObjectSet<out BaseVariant>
    get() = when (this) {
        is AppExtension -> applicationVariants
        is LibraryExtension -> libraryVariants
        is TestExtension -> applicationVariants
        else -> error("unsupported module type: $this")
    }

fun Project.android(): BaseExtension =
    project.extensions.findByType(BaseExtension::class.java)
        ?: throw GradleException("Project $name is not an Android project")

val Project.androidComponents
    get() = extensions.getByName<AndroidComponentsExtension<*, *, *>>("androidComponents")
