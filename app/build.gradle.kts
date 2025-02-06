plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("androidx.room")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
//    id("com.google.gms.google-services")
}

android {
    namespace = "us.smt.mylearningcentre"
    compileSdk = 35

    defaultConfig {
        applicationId = "us.smt.mylearningcentre"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}
kapt {
    correctErrorTypes = true
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.lottie.compose)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenmodel)
    implementation(libs.voyager.bottom.sheet.navigator)
    implementation(libs.voyager.tab.navigator)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.hilt)
    implementation(libs.gson)
    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.analytics)
    implementation(libs.okhttp)
//    implementation(libs.google.auth.library.oauth2.http)

    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.guava)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)
}
