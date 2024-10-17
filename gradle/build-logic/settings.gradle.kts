dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
            version("kotlin-gradle-plugin", System.getProperty("kotlin_version"))
        }
    }
}

rootProject.name = "build-logic"
