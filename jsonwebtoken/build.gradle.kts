dependencies {
    implementation(project(":shared"))
    // WARNING no multiple audience
    val version = "0.12.6"
    implementation("io.jsonwebtoken:jjwt-api:$version")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$version")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$version") // or "io.jsonwebtoken:jjwt-gson:$version" for gson
}
