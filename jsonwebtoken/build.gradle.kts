dependencies {
    implementation(project(":shared"))
    // WARNING no multiple audience
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5") // or "io.jsonwebtoken:jjwt-gson:0.12.5" for gson
}
