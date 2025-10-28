plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spring.dm)
}

allprojects {
    group = "org.example"
    version = "1.0"

    tasks.register<DependencyReportTask>("allDeps")
}

subprojects {
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = rootProject.libs.plugins.spring.dm.get().pluginId)

    dependencyManagement {
        imports {
            mavenBom(rootProject.libs.spring.bom.get().toString())
        }
    }

    ext["kotlin.version"] = rootProject.libs.versions.kotlin.lang.get()

    dependencies {
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation("org.junit.jupiter:junit-jupiter")

        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    kotlin {
        jvmToolchain(25)
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}

