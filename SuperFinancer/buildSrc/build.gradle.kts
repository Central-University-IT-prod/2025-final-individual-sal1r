plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api(libs.kotlin.gradle.plugin.api)
    api(libs.gradle.api)
}