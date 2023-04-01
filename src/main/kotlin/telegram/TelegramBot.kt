package telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text

fun telegramBot(token: String, body: TelegramBot.() -> Unit) = TelegramBot(token).build(body)

class TelegramBot(private val token: String) {

    //    private var textDispatcher: ((String, TextHandlerEnvironment) -> Unit)? = null
    private var textDispatcher: ((Long, String) -> Unit)? = null

    //    fun dispatchText(body: (String, TextHandlerEnvironment) -> Unit) {
    fun dispatchText(body: (Long, String) -> Unit) {
        textDispatcher = body
    }

    private fun build(): Bot {
        return bot {
            this.token = this@TelegramBot.token
            dispatch { text { textDispatcher?.invoke(message.chat.id, text) } }
        }
    }

    fun build(body: TelegramBot.() -> Unit): Bot {
        body()
        return build()
    }
}