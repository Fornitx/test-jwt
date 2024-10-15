plugins {
    kotlin("jvm")
}

group = "com.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
