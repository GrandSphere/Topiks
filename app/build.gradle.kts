import com.android.build.gradle.internal.scope.InternalArtifactType
import org.gradle.internal.extensions.core.extra
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}



// Path to the version file
val versionPropsFile = project.file("version.properties")

// Task to bump the patch number
val patchVersionUpdate by tasks.registering {
    // Declare inputs/outputs for incremental builds
    inputs.file(versionPropsFile)
    outputs.file(versionPropsFile)

    doLast {
        // 1. Load current properties
        val props = Properties().apply {
            versionPropsFile.inputStream().use { load(it) }
        }
        // 2. Increment patch
        val newPatch = props.getProperty("version.patch").toInt() + 1
        props.setProperty("version.patch", newPatch.toString())
        // 3. Save back to file
        versionPropsFile.outputStream().use { props.store(it, null) }
    }
}

val generateVersionKt by tasks.registering {
    dependsOn(patchVersionUpdate)  // ensure patch bumps first

    // Inputs: the updated version file
    inputs.file(versionPropsFile)
    // Outputs: the generated source file
    val outputDir = layout.buildDirectory.dir("generated/source/version")
    outputs.dir(outputDir)

    doLast {
        val props = Properties().apply {
            versionPropsFile.inputStream().use { load(it) }
        }
        val major = props.getProperty("version.major")
        val minor = props.getProperty("version.minor")
        val patch = props.getProperty("version.patch")
        val versionName = "$major.$minor.$patch"

        // Write Version.kt
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
        versionCode = 3
        versionName = "ABC"
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
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.documentfile)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.coil.compose)
    implementation(libs.picasso)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.material.icons.extended)
    implementation(libs.runtime.livedata)
    implementation (libs.androidx.webkit)

    kapt(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
}