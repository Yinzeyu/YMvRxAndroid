plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Build.compileSdkVersion)
    buildToolsVersion(Build.buildToolsVersion)
    defaultConfig{
        minSdkVersion(Build.minSdk)
        targetSdkVersion(Build.targetSdk)
        versionCode = Build.versionCode
        versionName = Build.versionName
    }

    androidExtensions{
//        configure(delegateClosureOf<org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension> {  isExperimental=true  }
        isExperimental=true
    }
    kotlinOptions{
        jvmTarget="1.8"
    }
    kapt{
        correctErrorTypes=true
    }
    lintOptions {
        isAbortOnError = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/NOTICE")
        exclude("META-INF/MANIFEST")
        exclude("META-INF/LICENSE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/LICENSE.txt")
    }
}
dependencies {
    api(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation(Deps.Kotlin.stdlib)
    api("androidx.appcompat:appcompat:1.2.0-beta01")
    api("androidx.core:core-ktx:1.3.0-rc01")
    api("androidx.cardview:cardview:1.0.0")
    api("androidx.recyclerview:recyclerview:1.2.0-alpha02")
    api("androidx.fragment:fragment:1.3.0-alpha03")
    api("androidx.fragment:fragment-ktx:1.3.0-alpha03")
    api("androidx.navigation:navigation-fragment-ktx:2.3.0-alpha05")
    api("androidx.navigation:navigation-ui-ktx:2.3.0-alpha05")
    api("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-alpha01")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0-alpha01")
    api("androidx.viewpager2:viewpager2:1.1.0-alpha01")
    api("com.github.bumptech.glide:glide:4.11.0")
    api("com.github.bumptech.glide:okhttp3-integration:4.11.0")
    api("com.squareup.okhttp3:okhttp:4.5.0")
    api("com.squareup.okhttp3:logging-interceptor:4.5.0")
    api("com.squareup.retrofit2:retrofit:2.8.1")
    api("com.squareup.retrofit2:converter-gson:2.8.1")
    api("com.google.code.gson:gson:2.8.6")
    api("androidx.multidex:multidex:2.0.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")
    api("com.blankj:utilcodex:1.26.0")

}