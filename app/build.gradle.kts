plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

fun String.asBuildConfigString(): String = "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""

val releaseKeystorePath = providers.gradleProperty("releaseKeystorePath").orNull
val releaseKeystorePassword = providers.gradleProperty("releaseKeystorePassword").orNull
val releaseKeyAlias = providers.gradleProperty("releaseKeyAlias").orNull
val releaseKeyPassword = providers.gradleProperty("releaseKeyPassword").orNull

val productionAdMobAppId = providers.gradleProperty("admobAppId").orElse("MISSING_ADMOB_APP_ID").get()
val productionBannerAdUnitId = providers.gradleProperty("admobBannerAdUnitId").orElse("MISSING_ADMOB_BANNER_ID").get()

android {
    namespace = "com.example.universal"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.universal"
        minSdk = 24
        targetSdk = 36
        versionCode = providers.gradleProperty("versionCode").orElse("1").get().toInt()
        versionName = providers.gradleProperty("versionName").orElse("1.0.0").get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (releaseKeystorePath != null) {
                storeFile = file(releaseKeystorePath)
                storePassword = releaseKeystorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField("String", "ADMOB_APP_ID", "ca-app-pub-3940256099942544~3347511713".asBuildConfigString())
            buildConfigField("String", "ADMOB_BANNER_AD_UNIT_ID", "ca-app-pub-3940256099942544/9214589741".asBuildConfigString())
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "ADMOB_APP_ID", productionAdMobAppId.asBuildConfigString())
            buildConfigField("String", "ADMOB_BANNER_AD_UNIT_ID", productionBannerAdUnitId.asBuildConfigString())
            manifestPlaceholders["admobAppId"] = productionAdMobAppId
            if (releaseKeystorePath != null) signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }

    packaging {
        resources.excludes += setOf("/META-INF/{AL2.0,LGPL2.1}", "META-INF/LICENSE*", "META-INF/NOTICE*")
    }

    lint {
        abortOnError = true
        checkReleaseBuilds = true
        warningsAsErrors = false
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.activity:activity-compose:1.12.4")
    implementation("androidx.compose.ui:ui:1.11.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.11.4")
    implementation("androidx.compose.foundation:foundation:1.11.4")
    implementation("androidx.compose.material3:material3:1.4.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.11.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk:1.4.0")
    implementation("com.google.android.ump:user-messaging-platform:4.0.0")
    testImplementation("junit:junit:4.13.2")
}
