package blend.ktor

import blend.util.Constants
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.uri
import io.ktor.server.response.respondRedirect
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlin.time.Duration.Companion.seconds

object KtorApplication {
    
    private val server = embeddedServer(
        factory = Netty,
        host = "0.0.0.0",
        port = 6969,
        module = {
            installModules()
            configureRouting()
        }
    )
    
    fun initialize() {
        server.start(wait = false)
    }
    fun shutdown() {
        server.stop()
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
            timeout = 15.seconds
            pingPeriod = 15.seconds
            maxFrameSize = Long.MAX_VALUE
            contentConverter = KotlinxWebsocketSerializationConverter(Constants.json)
        }

        intercept(ApplicationCallPipeline.Call) {
            val path = call.request.uri
            if (path.endsWith("/") && path != "/") {
                val normalizedPath = path.removeSuffix("/")
                call.respondRedirect(normalizedPath, permanent = true)
                finish()
            }
        }

    }
    
}
