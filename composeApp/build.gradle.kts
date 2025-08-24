import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)

}

kotlin {
val xcframework = XCFramework()

//    tasks.create("testClasses")
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
        xcframework.add(this) // ✅ Add each framework to the XCFramework bundle

        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(compose.material)
//            implementation(libs.androidx.material) // Or latest version
            implementation("io.ktor:ktor-client-okhttp:3.2.3")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1") // Or a compatible version
//            implementation(libs.compass.geolocation.mobile)
//            implementation(libs.compass.geocoder.mobile)
//            implementation(libs.compass.autocomplete.mobile)
//            implementation(libs.compass.permissions.mobile)
            implementation(libs.androidx.material.icons.extended) // This uses the org.jetbrains.compose.material version from libs.versions.toml

//            implementation(platform("androidx.compose:compose-bom:2024.03.00"))
//            implementation("androidx.compose.material:material-icons-extended")



        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
//            implementation(compose.material3) // <<< ADD THIS for common Material 3

            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("com.kizitonwose.calendar:compose-multiplatform:2.6.0-alpha02")
            implementation(libs.kotlinx.datetime.core)
            implementation("io.ktor:ktor-client-core:3.2.3")
            implementation("io.ktor:ktor-client-content-negotiation:3.2.3")

            implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.3")
//            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:3.2.3")
//            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.23") // Or whatever matches your project
            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.0")

            // Or your Kotlin version
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
            // Geolocation
            implementation(libs.compass.geolocation)
//            implementation(libs.compass.geolocation.mobile)

            // Geocoding
            implementation(libs.compass.geocoder)
//            implementation(libs.compass.geocoder.mobile)

            // Autocomplete
            implementation(libs.compass.autocomplete)
//            implementation(libs.compass.autocomplete.mobile)

            // Location permissions for mobile
//            implementation(libs.compass.permissions.mobile)
//            implementation(libs.sqldelight.runtime)
//            implementation(androidx.compose.material:material-icons-extended)
//            implementation(libs.material)


            implementation(libs.compass.geolocation.mobile)
            implementation(libs.compass.geocoder.mobile)
            implementation(libs.compass.autocomplete.mobile)
            implementation(libs.compass.permissions.mobile)
        }
         iosMain.dependencies {

             implementation("io.ktor:ktor-client-darwin:3.2.3")

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
//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//     kotlinOptions {
//        jvmTarget = "21"
//    }
//}
//compose {
//    kotlinCompilerPlugin.set("1.5.12") // or your matching version
//}
compose {
    kotlinCompilerPlugin.set(libs.versions.kotlin.get()) // Uses the version from libs.versions.toml
}
dependencies {
    implementation(libs.places)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.core.i18n)
    implementation(libs.locationdelegation)
//    implementation(libs.androidx.compose.material.core)
//    implementation(libs.androidx.material3.android)
    debugImplementation(compose.uiTooling)

}



