import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    kotlin("jvm")
}

dependencies {
//    constraints {
//        // Define dependency versions as constraints
//        implementation("org.apache.commons:commons-text:1.12.0")
//    }
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation(libs.junit.jupiter)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//testing {
//    suites {
//        // Configure the built-in test suite
//        val test by getting(JvmTestSuite::class) {
//            // Use JUnit Jupiter test framework
//            useJUnitJupiter("5.10.3")
//        }
//    }
//}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.register<DependencyReportTask>("allDeps") {}
