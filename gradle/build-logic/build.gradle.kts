plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:" + System.getProperty("kotlin.version"))
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-reflect:" + System.getProperty("kotlin.version"))
    }
}
