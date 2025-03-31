import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.dragon925.androidlearning"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.github.dragon925.androidlearning"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties().apply {
            val file = rootProject.file("local.properties")
            if(file.exists()) {
                load(file.inputStream())
            }
        }

        val apiUrl = if(localProperties.containsKey("api.url")) {
            localProperties.getProperty("api.url")
        } else {
            throw  GradleException("Api url not found")
        }

        buildConfigField("String", "API_URL", apiUrl)

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.bundles.coroutines)

    implementation(libs.kotlinx.datetime)

    implementation(libs.material)
    implementation(libs.bundles.android.views)

    implementation(libs.bundles.network)
    implementation(libs.gson)
    implementation(libs.bundles.rx)

    implementation(libs.bundles.coil)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.bundles.android.navigation)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}