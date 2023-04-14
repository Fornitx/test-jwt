dependencies {
    implementation(project(":shared"))
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // or "io.jsonwebtoken:jjwt-gson:0.11.5" for gson
}
