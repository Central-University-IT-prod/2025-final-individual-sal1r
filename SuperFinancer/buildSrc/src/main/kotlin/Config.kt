import com.android.build.api.dsl.CompileOptions
import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

object Config {

    // android
    val compileSdk = 35
    // defaultConfig
    val minSdk = 26
    val targetSdk = 35

    // compileOptions
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11

    // kotlinOptions
    val languageVersion = "2.1"
    val apiVersion = "2.1"
    val jvmTarget = "11"
}

fun configureKotlin(options: KotlinJvmOptions) = with(options) {
    jvmTarget = Config.jvmTarget
    languageVersion = Config.languageVersion
    apiVersion = Config.apiVersion
}

fun configureCompile(options: CompileOptions) = with(options) {
    sourceCompatibility = Config.sourceCompatibility
    targetCompatibility = Config.targetCompatibility
}