plugins {
    id("myproject.java-conventions")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.nimbusds:nimbus-jose-jwt:9.41.2")
}
