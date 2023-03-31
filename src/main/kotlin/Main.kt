import chat.ChatGpt
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import telegram.telegramBot
import util.OPEN_AI_API_KEY
import util.TELEGRAM_BOT_TOKEN
import util.dotenv

private lateinit var chatGpt: ChatGpt
private lateinit var bot: Bot

fun main() {
    chatGpt = ChatGpt(dotenv[OPEN_AI_API_KEY])
    bot = telegramBot(dotenv[TELEGRAM_BOT_TOKEN]) {
        dispatchText(::dispatchText)
    }
    bot.startPolling()
}

private fun dispatchText(text: String, handleText: TextHandlerEnvironment) = CoroutineScope(Dispatchers.IO).launch {
    bot.sendMessage(
        ChatId.fromId(handleText.message.chat.id),
        text = chatGpt.completeRequest(text) ?: "```No message received```",
        parseMode = ParseMode.MARKDOWN,
    )
}