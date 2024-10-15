plugins {
    id("myproject.java-conventions")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.auth0:java-jwt:4.4.0")
}
