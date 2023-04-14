dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "test-jwt"

include("auth0")
include("jose4j")
include("nimbus")
include("shared")
include("jsonwebtoken")
include("fusionauth")
