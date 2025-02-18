package blend.handler

import blend.event.EventBus
import blend.event.Subscriber
import blend.handler.impl.KeybindingHandler

object Handlers {
    fun init() {
        arrayOf(
            KeybindingHandler
        ).forEach {
            EventBus.register(it)
        }
    }
}

interface IHandler: Subscriber
