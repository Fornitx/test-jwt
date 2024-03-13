import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version System.getProperty("kotlin_version")
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
