plugins {
    kotlin("jvm") version System.getProperty("kotlin.version")
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        testImplementation(kotlin("test"))
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(19)
    }
}
