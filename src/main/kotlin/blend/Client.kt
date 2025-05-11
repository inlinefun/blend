package blend

import blend.command.CommandManager
import blend.config.ConfigManager
import blend.handler.ChatInputHandler
import blend.handler.KeybindHandler
import blend.handler.TargetHandler
import blend.module.ModuleManager
import blend.util.interfaces.IAccessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object Client: IAccessor {

    const val NAME = "Blend"
    const val VERSION = "5.0"
    val logger: Logger = LoggerFactory.getLogger(NAME)
    val folder = File(mc.runDirectory, NAME.lowercase()).also {
        if (!it.exists())
            it.mkdir()
    }

    fun init() {
        val preInit = Clock.System.now()

        arrayOf(
            ModuleManager,
            CommandManager
        ).forEach { it.init() }

        arrayOf(
            KeybindHandler,
            ChatInputHandler,
            TargetHandler
        ).forEach { it.init() }

        launch(Dispatchers.IO) {
            if (!ConfigManager.loadAll())
                ConfigManager.save()
        }

        logger.info("Initialized $NAME v$VERSION in ${Clock.System.now().minus(preInit).inWholeMilliseconds}ms")
    }

    fun shutdown() {
        ConfigManager.save()
        logger.info("Shutdown $NAME")
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit): Job {
        return GlobalScope.launch(context) {
            block(this)
        }
    }

}