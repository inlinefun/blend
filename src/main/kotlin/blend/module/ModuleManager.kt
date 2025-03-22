package blend.module

import blend.module.impl.movement.SprintModule
import blend.module.impl.visual.ClickGUIModule
import blend.module.impl.visual.ClientThemeModule
import blend.util.interfaces.Initializable
import com.google.gson.JsonArray
import com.google.gson.JsonObject

object ModuleManager: Initializable {

    lateinit var modules: Array<AbstractModule>

    override fun init() {
        modules = listOf(
            SprintModule,
            ClientThemeModule,
            ClickGUIModule
        ).sortedBy {
            it.name
        }.also { modules ->
            modules.filter { module ->
                module.defaultEnabled
            }.forEach { module ->
                module.set(true)
            }
        }.toTypedArray()
    }

    fun getJsonArray(): JsonArray {
        val arr = JsonArray()
        modules.forEach {
            arr.add(it.getJsonObject())
        }
        return arr
    }
    fun useJsonArray(arr: JsonArray) {
        arr.map {
            it.asJsonObject
        }.forEach { obj ->
            modules.firstOrNull { it.name.equals(obj.get("name").asString, true) }?.useJsonObject(obj)
        }
    }

    private fun AbstractModule.getJsonObject(): JsonObject {
        val obj = JsonObject()
        val values = JsonArray()
        this.values.forEach { value ->
            values.add(value.getJsonObject())
        }
        obj.addProperty("name", name)
        obj.addProperty("state", get())
        obj.add("values", values)
        return obj
    }
    private fun AbstractModule.useJsonObject(obj: JsonObject) {
        if (this !is ClickGUIModule)
            set(obj.get("state").asBoolean)
        obj.get("values").asJsonArray.map {
            it.asJsonObject
        }.forEach { obj ->
            this.values.firstOrNull { it.name.equals(obj.get("name").asString, true) }?.useJsonObject(obj)
        }
    }

}