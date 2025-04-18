package blend.ktor

import blend.util.Constants
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.websocket.WebSockets

object KtorApplication {
    
    private val server = embeddedServer(
        factory = Netty,
        port = 6969,
        module = {
            installModules()
            configureRouting()
        }
    )
    
    fun initialize() {
        server.start(wait = false)
    }

    private fun Application.installModules() {
        install(ContentNegotiation) {
            json(Constants.json)
        }
        install(CORS) {
            allowHost("localhost:5173")
        }
        install(WebSockets) {
            masking = false
            maxFrameSize = Long.MAX_VALUE
            contentConverter = KotlinxWebsocketSerializationConverter(Constants.json)
        }
    }
    
}
