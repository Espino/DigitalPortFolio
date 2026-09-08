plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val portfolioBaseUrl = providers.gradleProperty("PORTFOLIO_BASE_URL")
    .orElse("https://TU_USUARIO.github.io/digital-portfolio/")

android {
    namespace = "com.jordiphonedeveloper.digitalportfolio"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.jordiphonedeveloper.digitalportfolio"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "PROFILE_BASE_URL", "\"${portfolioBaseUrl.get()}\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":feature:profile"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.koin.android)
    implementation(libs.moshi)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
