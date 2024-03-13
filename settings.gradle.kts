dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "test-jwt"

include("auth0", "fusionauth", "jose4j", "jsonwebtoken", "kjwt", "nimbus", "shared")
