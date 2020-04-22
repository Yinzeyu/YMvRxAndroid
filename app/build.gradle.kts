plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Build.compileSdkVersion)
    buildToolsVersion(Build.buildToolsVersion)
    defaultConfig {
        applicationId = "com.yzy.myapplication"
        minSdkVersion(Build.minSdk)
        targetSdkVersion(Build.targetSdk)
        versionCode = Build.versionCode
        versionName = Build.versionName
        multiDexEnabled = true
        ndk {
            abiFilter("armeabi-v7a")
        }
        resConfigs("zh", "en")
    }
    androidExtensions {
        isExperimental = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        correctErrorTypes = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets["main"].assets.srcDir("src/assets")
    sourceSets["main"].jniLibs.srcDir("libs")
    signingConfigs {
        create("release") {
            storeFile = file("../yzy_mvrx.jks")
            storePassword = "yzy123"
            keyAlias = "yzy_com"
            keyPassword = "yzy123"
        }
        getByName("debug") {
            storeFile = file("../yzy_mvrx.jks")
            storePassword = "yzy123"
            keyAlias = "yzy_com"
            keyPassword = "yzy123"
        }
    }
    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isZipAlignEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isZipAlignEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    android.applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                val appName = "demo"
                val path = projectDir.path + "/src/main/res/values/strings.xml"
                //正式版还是测试版
                var typeName = buildType.name
                typeName =
                    typeName.substring(0, 1).toUpperCase() + typeName.substring(1).toLowerCase()
                //build名称
                val buildVer = versionCode.toString()
                val buildName = "_build$buildVer"
                //编译日期
                val buildTime = "_" + org.apache.tools.ant.types.resources.selectors.Date()
                    .setDateTime("yyyyMMdd_HHmm")
                //后缀名
                val suffix = ".apk"
                //生成输出文件名称
                outputFileName = "${appName}${typeName}${buildName}${buildTime}${suffix}"
            }
        }
    }
}
dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation(project(":ybase"))
    //https://github.com/Justson/AgentWeb
    implementation("com.just.agentweb:agentweb:4.1.3")
    implementation(Deps.Kotlin.stdlib)
    //https://github.com/airbnb/lottie-android
    implementation("com.airbnb.android:lottie:3.4.0")
    implementation("com.jeremyliao:live-event-bus-x:1.5.7")
    kapt("com.github.bumptech.glide:compiler:4.11.0")
    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.2")
}
//放在这省的忘
//@SuppressLint("ParcelCreator")
//@Parcelize
