pluginManagement {
    includeBuild("gradle/build-logic")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "test-jwt"

include("auth0", "fusionauth", "jose4j", "jsonwebtoken", /*"kjwt",*/ "nimbus", "shared")
