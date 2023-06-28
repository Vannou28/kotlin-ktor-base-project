val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project

plugins {
    kotlin("jvm") version "1.8.21"

    // ces deux plugins Gradle sont nécessaires pour que Ktor et la sérialisation fonctionnent pleinement
    id("io.ktor.plugin") version "2.3.1"
    kotlin("plugin.serialization") version "1.8.21"
}

application {
    mainClass.set("fr.wildcodeschool.kotlinsample.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")

    // ces dépendances permettent d'assurer la dé/sérialisation de JSON vers des objets
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation ("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation ("io.ktor:ktor-client-json:$ktor_version")
    implementation ("io.ktor:ktor-client-serialization:$ktor_version")

    // fournir une implémentation de client HTTP pour Ktor
    implementation("io.ktor:ktor-client-apache:$ktor_version")
}