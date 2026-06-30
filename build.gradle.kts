plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
}

group = "ru.nezxenka"
version = "1.0"

repositories {
    mavenCentral() // FastUtil
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
    maven("https://jitpack.io") // VaultAPI
    maven("https://repo.extendedclip.com/releases/") // PlaceholderAPI
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.12-R0.1-SNAPSHOT")

    // https://mvnrepository.com/artifact/it.unimi.dsi/fastutil
    // doesn't want to be imported from paper (idk why...)
    compileOnly("it.unimi.dsi:fastutil:8.5.16")

    // https://github.com/MilkBowl/VaultAPI
    implementation("com.github.MilkBowl:VaultAPI:1.7.1")

    // https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Hook-into-PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.12.2")

    compileOnly("org.jetbrains:annotations:24.1.0")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    shadowJar {
        archiveClassifier.set("") // removes "-all"
    }

    build {
        dependsOn(shadowJar)
    }
}
