package blend.event

sealed interface Event

sealed class CancellableEvent : Event {
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

