plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:" + System.getProperty("kotlinVersion"))
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-reflect:" + System.getProperty("kotlinVersion"))
    }
}
