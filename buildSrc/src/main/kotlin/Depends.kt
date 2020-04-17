import groovy.util.XmlSlurper
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import java.io.File
import javax.xml.parsers.SAXParserFactory

/**
 * Created by haipo
 * on 2019-09-05.
 */
object Deps {
    object Support {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.support}"
        const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
        const val design = "com.google.android.material:material:${Versions.support}"
    }

    object Lifecycle {
        const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    }

    object Retrofit {
        const val runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
        const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpLoggingInterceptor}"
    }


    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val navigationPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationPlugin}"

    fun addRepos(handler: RepositoryHandler) {
        handler.apply {
            google()
            jcenter()
            maven {
                setUrl("https://jitpack.io")
            }
            mavenCentral()
        }
    }
}

fun DependencyHandlerScope.addCommonDeps(){

}