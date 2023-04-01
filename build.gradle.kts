plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "yalexaner"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // Kotlin Telegram Bot: https://github.com/kotlin-telegram-bot/kotlin-telegram-bot
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.7") {
        // excluded because of the next warning:
        // Provides transitive vulnerable dependency maven:com.google.code.gson:gson:2.8.5
        exclude(group = "com.google.code.gson", module = "gson")
    }

    // Gson (needed for Kotlin Telegram Bot)
    implementation("com.google.code.gson:gson:2.10.1")

    // 🗝️ dotenv-kotlin: https://github.com/cdimascio/dotenv-kotlin
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // OpenAI API client for Kotlin: https://github.com/aallam/openai-kotlin
    implementation(platform("com.aallam.openai:openai-client-bom:3.2.0"))
    implementation("com.aallam.openai:openai-client")
    implementation("io.ktor:ktor-client-okhttp")

    // Test
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}