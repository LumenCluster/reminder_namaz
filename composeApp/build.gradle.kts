import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)

}

kotlin {
    tasks.create("testClasses")
    jvmToolchain(21) // ⬅️ This is even better: sets JDK version for compilation
    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(compose.material)
            implementation(libs.androidx.material) // Or latest version
            implementation("io.ktor:ktor-client-okhttp:2.3.12")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0-RC2") // Or a compatible version


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("com.kizitonwose.calendar:compose-multiplatform:2.6.0-alpha02")
            implementation(libs.kotlinx.datetime.core)
            implementation("io.ktor:ktor-client-core:2.3.12")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.11")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.11")
            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.10") // Or your Kotlin version
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
            // Geolocation
            implementation(libs.compass.geolocation)
            implementation(libs.compass.geolocation.mobile)

            // Geocoding
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geocoder.mobile)

            // Autocomplete
            implementation(libs.compass.autocomplete)
            implementation(libs.compass.autocomplete.mobile)

            // Location permissions for mobile
            implementation(libs.compass.permissions.mobile)
//            implementation(libs.sqldelight.runtime)
//            implementation(androidx.compose.material:material-icons-extended)
//            implementation(libs.material)
            implementation(libs.androidx.material.icons.extended)




        }
         iosMain.dependencies {

             implementation("io.ktor:ktor-client-darwin:2.3.12")

        }
    }


}



android {
    namespace = "org.lumincluster.namazreminder"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.lumincluster.namazreminder"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }

}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
     kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(libs.places)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.core.i18n)
    implementation(libs.locationdelegation)
//    implementation(libs.androidx.compose.material.core)
    implementation(libs.androidx.material3.android)
    debugImplementation(compose.uiTooling)

}



