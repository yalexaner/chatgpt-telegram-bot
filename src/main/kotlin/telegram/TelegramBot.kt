package telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text

fun telegramBot(token: String, body: TelegramBot.() -> Unit) = TelegramBot(token).build(body)

class TelegramBot(private val token: String) {

    private var textDispatcher: ((String, TextHandlerEnvironment) -> Unit)? = null

    fun dispatchText(body: (String, TextHandlerEnvironment) -> Unit) {
        textDispatcher = body
    }

    private fun build(): Bot {
        return bot {
            this.token = this@TelegramBot.token
            dispatch { text { textDispatcher?.invoke(text, this) } }
        }
    }

    fun build(body: TelegramBot.() -> Unit): Bot {
        body()
        return build()
    }
}