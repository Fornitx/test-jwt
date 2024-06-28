pluginManagement {
    plugins {
        kotlin("jvm") version System.getProperty("kotlin_version")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "test-jwt"

include("auth0", "fusionauth", "jose4j", "jsonwebtoken", "kjwt", "nimbus", "shared")
