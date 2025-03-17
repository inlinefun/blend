package blend.config

import blend.Client
import blend.handler.ThemeHandler
import blend.module.ModuleManager
import blend.util.extensions.addChatError
import blend.util.extensions.addChatInfo
import blend.util.extensions.addChatWarn
import com.google.gson.FormattingStyle
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object ConfigManager {

    val folder = File(Client.folder, "configs").also {
        it.mkdirs()
    }
    private val gson = GsonBuilder()
        .setFormattingStyle(FormattingStyle.PRETTY.withIndent("    "))
        .create()

     fun loadAll(config: String = "default"): Boolean {
        try {
            val file = File(folder, "$config.json")
            if (!file.exists()) {
                Text.literal("Config ")
                    .append(Text.of(config)).withColor(ThemeHandler.primary.rgb)
                    .append(" does not exist")
                return false
            }

            FileReader(file).use { fr -> //file reader frr
                val root = JsonParser.parseReader(fr).asJsonObject
                val version = root.get("version").asDouble
                when {
                    version > (Client.VERSION.toDoubleOrNull() ?: -1.0) -> {
                        Text.literal("Config is for a newer version of the client. Update your client to load the config.")
                            .addChatError()
                        false
                    }
                    version < (Client.VERSION.toDoubleOrNull() ?: -1.0) -> {
                        Text.of("Config might be for an older version of the client, Some features might not work as expected.")
                            .addChatWarn()
                    }
                }
                ModuleManager.useJsonArray(root.get("modules").asJsonArray)

            }
            return true
        } catch (e: Exception) {
            Client.logger.error("Error loading config file: $config", e)
            return false
        }
    }

    fun save(config: String = "default") {
        try {
            val file = File(folder, "$config.json")
            if (!file.exists())
                file.createNewFile()
            FileWriter(file).use {
                val root = JsonObject()
                val modules = ModuleManager.getJsonArray()

                root.addProperty("client", Client.NAME)
                root.addProperty("version", Client.VERSION.toDoubleOrNull() ?: -1.0)
                root.add("modules", modules)

                it.write(gson.toJson(root))
            }
        } catch (e: Exception) {
            Client.logger.error("Error saving config file: $config", e)
        }
    }

    fun listAll() {
        try {
            Text.of("Local configs: ").addChatInfo()
            folder.listFiles()?.forEach { config ->
                Text.literal(config.name).apply {
                    style = Style.EMPTY
                        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of(".config load ${config.nameWithoutExtension}")))
                }.addChatInfo(false)
            }
        } catch (e: Exception) {
            Text.of("Error discovering configs")
            Client.logger.error("Error listing configs", e)
        }
    }

}
