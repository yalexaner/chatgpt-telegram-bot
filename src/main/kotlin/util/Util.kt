package util

import io.github.cdimascio.dotenv.dotenv

val dotenv by lazy { dotenv() }

const val TELEGRAM_BOT_TOKEN = "TELEGRAM_BOT_TOKEN"
const val OPEN_AI_API_KEY = "OPEN_AI_API_KEY"