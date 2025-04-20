package blend.ktor.data

import blend.Client
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Response

@Serializable
@SerialName("info")
data class ClientInfoResponse(
    val name: String,
    val version: String
): Response {
    companion object {
        fun get() = ClientInfoResponse(
            name = Client.NAME,
            version = Client.VERSION
        )
    }
}
