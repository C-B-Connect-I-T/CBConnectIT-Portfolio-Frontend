plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kobweb.application) apply false
    alias(libs.plugins.kobweb.library) apply false
    alias(libs.plugins.kobwebx.markdown) apply false
    alias(libs.plugins.detekt) apply false
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
}
