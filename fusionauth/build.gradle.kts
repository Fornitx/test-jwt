plugins {
    id("buildlogic.kotlin-conventions")
}

dependencies {
    implementation(project(":shared"))
    implementation("io.fusionauth:fusionauth-jwt:5.3.3")
}
