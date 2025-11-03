plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // 🧠 wajib buat Glide compiler
}

android {
    namespace = "com.example.thriftso"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.thriftso"
        minSdk = 27
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // 🔹 AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // 🔹 RecyclerView & CardView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    // 🔹 Lifecycle (ViewModel & LiveData)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // 🔹 SwipeRefresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // 🔹 Animations & Circle Image
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // 🔹 Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // 🧠 ADD THIS — Glide (buat load gambar dari URL)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // 🔹 Unit tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("net.zetetic:android-database-sqlcipher:4.5.3")
    implementation("androidx.biometric:biometric:1.1.0")


    // Untuk CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")
}
