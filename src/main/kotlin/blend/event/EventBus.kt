package blend.event

import blend.util.interfaces.IAccessor
import org.greenrobot.eventbus.EventBus

object EventBus: IAccessor {

    private val bus = EventBus
        .builder()
        .throwSubscriberException(false)
        .logSubscriberExceptions(true)
        .strictMethodVerification(true)
        .build()!!

    fun register(subscriber: Subscriber) {
        if (bus.hasSubscriberForEvent(Event::class.java))
            bus.register(subscriber)
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