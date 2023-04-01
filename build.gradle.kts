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

tasks {
    val jar by getting(Jar::class) {
        manifest {
            attributes["Main-Class"] = "MainKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    @Suppress("UNUSED_VARIABLE")
    val dockerBuild by creating {
        dependsOn(jar)
        doLast {
            val jarFile = jar.archiveFile.get().asFile
            val dockerImageName = "chatgpt-telegram-bot"
            val dockerFilePath = File(projectDir, "Dockerfile")
            val dockerBuildContext = File(projectDir, "build/docker")
            dockerBuildContext.mkdirs()

            copy {
                from(jarFile)
                into(dockerBuildContext)
                rename(jarFile.name, "ChatGptTelegramBot.jar")
            }

            project.exec {
                commandLine(
                    "docker", "build",
                    "-f", dockerFilePath.absolutePath,
                    "-t", dockerImageName,
                    "."
                )
                workingDir(dockerBuildContext)
            }
        }
    }
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}