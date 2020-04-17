plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(29)
    defaultConfig{
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode=1
        versionName="1.0"
    }
    androidExtensions{
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
    api("androidx.appcompat:appcompat:1.1.0")
    api("androidx.core:core-ktx:1.2.0")
    api("androidx.cardview:cardview:1.0.0")
    api("androidx.recyclerview:recyclerview:1.1.0")
    api("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    api("androidx.navigation:navigation-fragment-ktx:2.2.2")
    api("androidx.navigation:navigation-ui-ktx:2.2.2")
    api("com.github.bumptech.glide:glide:4.11.0")
    api("com.github.bumptech.glide:okhttp3-integration:4.11.0")
    api("com.squareup.okhttp3:okhttp:4.5.0")
    api("com.squareup.okhttp3:logging-interceptor:4.5.0")
    api("com.squareup.retrofit2:retrofit:2.8.1")
    api("com.squareup.retrofit2:converter-gson:2.8.1")
    api("com.google.code.gson:gson:2.8.6")
    api("androidx.multidex:multidex:2.0.1")
    //https://github.com/airbnb/lottie-android
    api("com.airbnb.android:lottie:3.4.0")
    api("com.jeremyliao:live-event-bus-x:1.5.7")
    api("androidx.viewpager2:viewpager2:1.0.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    api("me.jessyan:autosize:1.2.1")
    api("com.blankj:utilcodex:1.26.0")
}
//apply plugin: 'com.android.library'
//apply plugin: 'kotlin-android'
//apply plugin: 'kotlin-android-extensions'
//apply plugin: 'kotlin-kapt'
//android {
//    compileSdkVersion rootProject.ext.android.compileSdkVersion
//    buildToolsVersion rootProject.ext.android.buildToolsVersion
//    defaultConfig {
//        minSdkVersion rootProject.ext.android.minSdkVersion
//        targetSdkVersion rootProject.ext.android.targetSdkVersion
//        versionCode rootProject.ext.android.versionCode
//        versionName rootProject.ext.android.versionName
//    }
//    packagingOptions {
//        exclude 'META-INF/DEPENDENCIES'
//        exclude 'META-INF/NOTICE'
//        exclude 'META-INF/MANIFEST'
//        exclude 'META-INF/LICENSE'
//        exclude 'META-INF/LICENSE.txt'
//        exclude 'META-INF/NOTICE.txt'
//    }
////使用Kotlin实验特性
//    androidExtensions {
//        experimental = true
//    }
//    kapt {
//        correctErrorTypes = true
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//    compileOptions {
//        sourceCompatibility rootProject.ext.android.javaSourceCompatibility
//        targetCompatibility rootProject.ext.android.javaTargetCompatibility
//    }
////    dataBinding {
////        enabled = true
////    }
//    sourceSets { main { assets.srcDirs = ['src/main/assets', 'src/main/assets/'] } }
//}
//dependencies {
//    api fileTree(dir: 'libs', include: ['*.jar'])
//    api rootProject.ext.dependencies.kotlin_jdk
//    api rootProject.ext.dependencies.appcompat_v7
//    api rootProject.ext.dependencies.core_ktx
//    api rootProject.ext.dependencies.constraintLayout
//    api rootProject.ext.dependencies.navigation_fragment_ktx
//    api rootProject.ext.dependencies.navigation_ui_ktx
//    api rootProject.ext.dependencies.fragment
//    api rootProject.ext.dependencies.fragment_ktx
//    api rootProject.ext.dependencies.utilcode
//    api rootProject.ext.dependencies.okhttp3
//    api rootProject.ext.dependencies.okhttp3_log
//    api rootProject.ext.dependencies.retrofit
//    api rootProject.ext.dependencies.retrofit_converter_gson
//    api rootProject.ext.dependencies.glide
//    api rootProject.ext.dependencies.glide_okhttp
//    api rootProject.ext.dependencies.multidex
//    api rootProject.ext.dependencies.gson
//    api rootProject.ext.dependencies.autosize
//    api rootProject.ext.dependencies.coroutines_android
//    api rootProject.ext.dependencies.lifecycle_viewmodel_ktx
//    api rootProject.ext.dependencies.lifecycle_livedata_ktx
//}