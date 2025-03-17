package blend.command.impl

import blend.Client
import blend.command.AbstractCommand
import blend.config.ConfigManager
import blend.handler.ThemeHandler
import blend.util.extensions.addChatError
import blend.util.extensions.addChatInfo
import blend.util.extensions.addChatWarn
import kotlinx.coroutines.Dispatchers
import net.minecraft.text.Text

class ConfigCommand: AbstractCommand(
    aliases = arrayOf("config", "c"),
    description = "Save current client configuration to a file."
) {

    override fun execute(args: Array<String>) {
        Client.launch(Dispatchers.IO) {
            if (args.isNotEmpty()) {
                val modifier = args[0].lowercase()
                when {
                    modifier.startsWith("lo") -> {
                        if (args.size > 1) {
                            val name = args[1]
                            if (ConfigManager.loadAll(name)) {
                                Text.of("Loaded config $name").addChatInfo()
                            } else {
                                Text.of("Error loading config $name").addChatError()
                            }
                        } else {
                            Text.empty()
                                .append(Text.literal(".config load "))
                                .append(Text.literal("<name>").withColor(ThemeHandler.secondary.rgb)).addChatWarn()
                        }
                    }
                    modifier.startsWith("s") -> {
                        if (args.size > 1) {
                            val name = args[1]
                            ConfigManager.save(name)
                            Text.of("Saved config $name").addChatInfo()
                        } else {
                            Text.empty()
                                .append(Text.literal(".config save "))
                                .append(Text.literal("<name>").withColor(ThemeHandler.secondary.rgb)).addChatWarn()
                        }
                    }
                    modifier.startsWith("li") -> {
                        ConfigManager.listAll()
                    }
                }
            } else {
                Text.of("Hmm").addChatWarn()
            }
        }
    }


}