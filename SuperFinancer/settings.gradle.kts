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

rootProject.name = "SuperFinancer"
include(":app")
include(":feature:home")
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":core:util")
include(":core:resources")
include(":feature:feed")
include(":feature:finance")
include(":feature:news-details")
include(":shared:paging")
include(":shared:search")
