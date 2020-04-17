buildscript {
    Deps.addRepos(repositories)
    dependencies {
        classpath(Deps.androidGradlePlugin)
        classpath(Deps.Kotlin.plugin)
        classpath(Deps.navigationPlugin)
    }
}

allprojects {
    Deps.addRepos(repositories)
}
tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}














//apply from: "config.gradle"
//buildscript {
////    ext.kotlin_version = '1.3.72'
////    ext.objectboxVersion = '2.5.0'
////    ext.android_plugin_version = '3.1.4'
////    ext.nav_version = "2.2.1"
//    repositories {
//        google()
//        jcenter()
//    }
//    dependencies {
//        classpath 'com.android.tools.build:gradle:3.6.2'
//        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72"
//        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.2.1"
////        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4'
////        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'
//        //更新R8版本，解决正式版无法打包的问题 https://github.com/square/okhttp/issues/4604
//        classpath "com.android.tools:r8:1.6.84"
//    }
//}
//
//allprojects {
//    repositories {
//        google()
//        jcenter()
//        mavenCentral()
//        maven { url 'https://jitpack.io' }
//        maven { url "https://dl.bintray.com/thelasterstar/maven/" }
//        maven { url 'https://dl.bintray.com/umsdk/release' }
//        maven {url  "https://dl.bintray.com/rongcloud/maven"}
//    }
//    tasks.withType(Javadoc) {
//        options.addStringOption('Xdoclint:none', '-quiet')
//        options.addStringOption('encoding', 'UTF-8')
//    }
//}
//
//task clean(type: Delete) {
//    delete rootProject.buildDir
//}
