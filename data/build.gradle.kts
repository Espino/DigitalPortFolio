plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.jordiphonedeveloper.digitalportfolio.data"
    compileSdk = 37
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":domain"))
    implementation(libs.moshi)
    implementation(libs.kotlinx.coroutines.core)
}
