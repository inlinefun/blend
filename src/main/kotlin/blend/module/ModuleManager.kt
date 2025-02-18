package blend.module

import blend.Client
import blend.module.impl.visual.*
import blend.module.impl.movement.*

object ModuleManager {

    lateinit var modules: List<AbstractModule>

    fun init() {
        modules = listOf(
            ClickGUIModule,
            SprintModule
        ).sortedBy { it.name }.also { modules ->
            modules.filter { it.defaultEnabled }.forEach{ it.set(true) }
        }
        Client.logger.info("Loaded ${modules.count()} modules")
    }

}