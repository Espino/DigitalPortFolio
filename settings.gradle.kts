pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DigitalPortfolio"

include(
    ":app",
    ":core:common",
    ":core:model",
    ":core:database",
    ":core:network",
    ":core:designsystem",
    ":domain",
    ":data",
    ":feature:profile",
)
