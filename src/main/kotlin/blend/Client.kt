package blend

import blend.ktor.KtorApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Client {

    const val NAME = "Blend"
    const val VERSION = "6.0"
    val logger: Logger = LoggerFactory.getLogger(NAME)
    val scope = CoroutineScope(Dispatchers.Default)

    fun initialize() {
        val time = System.currentTimeMillis()

        scope.launch {
            KtorApplication.initialize()
        }

        logger.info("Initialized $NAME v$VERSION in ${System.currentTimeMillis() - time}ms")
    }

    fun shutdown() {
        KtorApplication.shutdown()
    }

}