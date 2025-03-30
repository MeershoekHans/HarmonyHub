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

rootProject.name = "HarmonyHub"

// App module
include(":app")

// Core modules
include(":core:ui")
include(":core:common")
include(":core:data")
include(":core:model")
include(":core:network")
include(":core:testing")

// Feature modules
include(":feature:auth")
include(":feature:schedule")
include(":feature:messaging")
include(":feature:expense")
include(":feature:storage")
include(":feature:checkin")