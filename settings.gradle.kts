enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "AndroidLearning"
include(":app")
include(":features:authorization")
include(":base:datasources:local:room")
include(":base:datasources:remote:retrofit")
include(":base:datasources:contract")
include(":base:core-api")
include(":base:core")
include(":features:help")
include(":features:profile")
include(":features:news")
include(":features:search")
