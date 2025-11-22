pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                // CRITICAL FIX: Adding androidx.* to the plugin resolution content filter
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven{url=uri("https://www.jitpack.io")}
    }

    plugins {
        // ⬅️ FIX: Updated KSP version to 2.0.21-1.0.28 to match the implied Kotlin 2.0.21 version.
        id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // This is correct for library dependencies
        google()
        mavenCentral()
        maven{url=uri("https://www.jitpack.io")}
    }
}

rootProject.name = "Mad-v2"
include(":app")