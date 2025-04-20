package blend.ktor

import blend.ktor.data.ClientInfoResponse
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.channels.consumeEach

fun Application.configureRouting() {
    routing {

        /**
         * The websocket does not listen to incoming frames, it will only be used to emit events
         * @see blend.ktor.data.WSEvent
         */
        webSocket("/blend") {
            try {
                WebSocket.newSession(this@webSocket)
                incoming.consumeEach { _ ->  }
            } finally {
                WebSocket.handleClose(this@webSocket)
            }
        }

        route("/api") {
            get {
                call.respondText("Documentation has not been prepared yet. Please refer the source.")
            }
            get("/info") {
                call.respond(ClientInfoResponse.get())
            }
        }

        staticResources(
            remotePath = "/",
            basePackage = "/assets/static/"
        )

    }
}