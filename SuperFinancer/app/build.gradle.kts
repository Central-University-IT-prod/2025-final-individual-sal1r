import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.salir.superFinancer"
    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = "com.salir.superFinancer"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        val major = 0
        val minor = 0
        val patch = 1
        versionCode = major * 1_000_000 + minor * 1_000 + patch
        versionName = "$major.$minor.$patch"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"

            )
            signingConfig = signingConfigs.getByName("debug")
            project.extensions.extraProperties["android.enableAppCompileTimeRClass"] = true
        }

        debug {
            isMinifyEnabled = false

            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }

        flavorDimensions += "version"

        productFlavors {
            create("dev") {
                dimension = "version"
                versionNameSuffix = "-dev"
            }
            create("rc") {
                dimension = "version"
                versionNameSuffix = "-rc"
            }
            // 🤩
            create("prod") {
                dimension = "version"
                versionNameSuffix = "-prod"
            }
        }

        defaultConfig {
            val properties = Properties().apply {
                load(rootProject.file("local.properties").inputStream())
            }

            buildConfigField("String", "FINHUB_API_KEY", "\"${properties.getProperty("FINHUB_API_KEY")}\"")
            buildConfigField("String", "NYTIMES_API_KEY", "\"${properties.getProperty("NYTIMES_API_KEY")}\"")
            buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("SUPABASE_URL")}\"")
            buildConfigField("String", "SUPABASE_KEY", "\"${properties.getProperty("SUPABASE_KEY")}\"")
        }
    }

    configureCompile(compileOptions)
    configureKotlin(kotlinOptions)

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {

    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":core:resources"))

    implementation(project(":feature:home"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:finance"))
    implementation(project(":feature:news-details"))

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.storage)
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.androidx.startup)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.content.negotiation)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.navigation.compose)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}