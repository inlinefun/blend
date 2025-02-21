package blend.handler

import blend.event.EventBus
import blend.event.Subscriber
import blend.handler.impl.ChatMessageHandler
import blend.handler.impl.KeybindingHandler
import blend.util.interfaces.IAccessor

object Handlers {
    fun init() {
        arrayOf(
            KeybindingHandler,
            ChatMessageHandler
        ).forEach {
            EventBus.register(it)
        }
    }
}

interface IHandler: Subscriber, IAccessor
