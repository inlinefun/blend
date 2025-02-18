package blend.event

import blend.util.interfaces.IAccessor
import org.greenrobot.eventbus.EventBus

object EventBus: IAccessor {

    private val bus = EventBus
        .builder()
        .apply {
            logSubscriberExceptions(true)
            logNoSubscriberMessages(false)
            sendSubscriberExceptionEvent(false)
            sendNoSubscriberEvent(false)
            throwSubscriberException(false)
        }
        .build()!!

    fun register(subscriber: Subscriber) {
        try {
            bus.register(subscriber)
        } catch (_: Exception) {
        }
    }

    fun unregister(subscriber: Subscriber) {
        if (bus.isRegistered(subscriber))
            bus.unregister(subscriber)
    }

    fun post(event: Event) {
        if (mc.player == null || mc.world == null)
            return
        bus.post(event)
    }

}

interface Subscriber