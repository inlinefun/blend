package blend.ktor

import blend.Client
import blend.ktor.data.WSEvent
import blend.util.Constants
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object WebSocket {

    private val sessions = mutableListOf<WebSocketSession>()
    private val socketScope = CoroutineScope(Dispatchers.Default + CoroutineName("Ktor/Websocket"))

    fun newSession(handler: DefaultWebSocketServerSession) {
        sessions.add(handler)
        Client.logger.info("New session")
    }
    fun handleClose(handler: DefaultWebSocketServerSession) {
        sessions.remove(handler)
        Client.logger.info("closed session")
    }

    fun WSEvent.respond() {
        send(this)
    }
    inline fun <reified T: WSEvent> send(response: T) {
        send(Constants.json.encodeToString(response))
    }
    fun send(content: String) {
        socketScope.launch {
            sessions.forEach { session ->
                session.send(content)
            }
        }
    }

}