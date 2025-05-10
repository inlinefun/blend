package blend.event

import net.minecraft.util.PlayerInput

@Suppress("Unchecked_Cast")
sealed interface Event {
    fun <T: Event> call(): T {
        EventBus.post(this)
        return this as T
    }
}
sealed class CancellableEvent: Event {
    var cancelled = false
    fun cancel() {
        cancelled = true
    }
}

data class KeyEvent(
    val key: Int,
    val scancode: Int,
    val action: Int,
    val modifiers: Int
): Event
data class MouseTickEvent(
    val deltaX: Double,
    val deltaY: Double
): Event

data class ChatSendEvent(
    val content: String
): CancellableEvent()

data class PlayerInputEvent(
    var forward: Boolean,
    var backward: Boolean,
    var left: Boolean,
    var right: Boolean,
    var jump: Boolean,
    var sneak: Boolean,
    private val sprint: Boolean // this does NOT even work for some reason. probably a skill issue :(
): Event {
    constructor(input: PlayerInput) : this(input.forward, input.backward, input.left, input.right, input.jump, input.sneak, input.sprint)
    val asPlayerInput
        get() = PlayerInput(forward, backward, left, right, jump, sneak, sprint)
}

data object PlayerTickEvent: Event
data object InputHandleEvent: Event