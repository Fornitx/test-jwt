plugins {
    alias(libs.plugins.kotlin.jvm)
}

allprojects {
    group = "org.example"
    version = "1.0"

    tasks.register<DependencyReportTask>("allDeps") {}
}

subprojects {
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {
        implementation(platform(rootProject.libs.spring.bom))
        testImplementation(platform(rootProject.libs.spring.bom))

        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}

