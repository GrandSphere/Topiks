import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

// Path to the version file
val versionPropsFile = project.file("version.properties")

// Load the version properties and initialize variables outside the tasks block
val props = Properties().apply {
    versionPropsFile.inputStream().use { load(it) }
}

val major: Int = props.getProperty("version.major").toInt()
val minor: Int = props.getProperty("version.minor").toInt()
val patch: Int = props.getProperty("version.patch").toInt()
val versionName: String = "$major.$minor.$patch"
val versionCode: Int = (major * 10000 + minor * 100 + patch)  // Example versionCode calculation

// Task to increment the patch number
val patchVersionUpdate by tasks.registering {
    // Declare inputs/outputs for incremental builds
    inputs.file(versionPropsFile)
    outputs.file(versionPropsFile)

    doLast {
        // Increment the patch version
        val newPatch = props.getProperty("version.patch").toInt() + 1
        props.setProperty("version.patch", newPatch.toString())
        versionPropsFile.outputStream().use { props.store(it, null) }
    }
}

val generateVersionKt by tasks.registering {
    dependsOn(patchVersionUpdate)  // ensure patch bumps first

    // Inputs: the updated version file
    inputs.file(versionPropsFile)
    val outputDir = layout.buildDirectory.dir("generated/source/version")
    outputs.dir(outputDir)

    doLast {
        val dir = outputDir.get().asFile.apply { mkdirs() }
        file("$dir/Version.kt").writeText("""
            package com.GrandSphere.Topiks

            /** Auto‑generated semantic version */
            object Version {
                const val APP = "$versionName"
            }
        """.trimIndent())
    }
}

kotlin {
    sourceSets["main"].kotlin.srcDir(generateVersionKt.map { it.outputs.files.singleFile })
}

tasks.matching { it.name.startsWith("compile") && it.name.endsWith("Kotlin") }
    .configureEach {
        dependsOn(generateVersionKt)
    }

android {
    namespace = "com.GrandSphere.Topiks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.GrandSphere.Topiks"
        minSdk = 31
        targetSdk = 34
        versionCode = versionCode
        versionName = versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            applicationVariants.configureEach {
                outputs.configureEach {
                    this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
                    outputFileName = "Topiks.apk"
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    kapt(libs.androidx.room.compiler)
    implementation(libs.runtime.livedata)
    implementation(libs.material.icons.extended)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.coil.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.annotation)
}
