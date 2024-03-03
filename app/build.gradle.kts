plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.jobfinder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jobfinder"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    val retrofitVersion = "2.9.0";
    val lifecycleVersion = "2.7.0";
    val glideVersion = "4.16.0";
    val coroutinesVersion = "1.7.3";
    val nav_version = "2.7.7";
    val firebase_ver = "8.0.2";

    // default
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // google service firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation("com.google.firebase:firebase-auth:22.3.1")
    // FirebaseUI for Firebase Realtime Database
    implementation ("com.firebaseui:firebase-ui-database:$firebase_ver")
    implementation("com.google.firebase:firebase-database:20.3.1")
    // FirebaseUI for Cloud Firestore
    implementation ("com.firebaseui:firebase-ui-firestore:$firebase_ver")
    // FirebaseUI for Firebase Auth
    implementation ("com.firebaseui:firebase-ui-auth:$firebase_ver")
    // FirebaseUI for Cloud Storage
    implementation ("com.firebaseui:firebase-ui-storage:$firebase_ver")
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // animation
    implementation("androidx.compose.animation:animation-core-android:1.6.2")

    // Retrofit2
//    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
//    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
//    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
//    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.11.0")

    // Android lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // store local user data
//    implementation("androidx.datastore:datastore-preferences:1.0.0")
//    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
//    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Rxjava
//    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

    // kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // use Glide for retrieving image from a remote source/URL
//    implementation("com.github.bumptech.glide:glide:$glideVersion")
//    ksp("androidx.room:room-compiler:2.6.1")

    // dot indicator
    implementation("com.tbuonomo:dotsindicator:4.2")

    // Timber log
//    implementation("com.jakewharton.timber:timber:4.7.1")
}