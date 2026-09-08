plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.androidx.room) apply false
}

tasks.register<Copy>("syncProfileContent") {
    group = "portfolio"
    description = "Copies docs/profile.json into the Android offline seed asset."
    from(layout.projectDirectory.file("docs/profile.json"))
    into(layout.projectDirectory.dir("data/src/main/assets"))
}
