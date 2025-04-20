package blend.ktor.data

import kotlinx.serialization.Serializable

/**
 * Events are emitted on the websocket connections.
 * Those events are parsed in the webapp to sync necessary changes
 */
@Serializable
sealed interface WSEvent {
    val event: String
}
