package blend.command.impl

import blend.command.AbstractCommand
import blend.handler.impl.ThemeHandler
import blend.module.ModuleManager
import blend.util.misc.ChatUtil
import net.minecraft.text.Text
import java.awt.Color

class ModulesCommand: AbstractCommand(
    identifiers = arrayOf("modules", "mods"),
    description = "List all available modules",
    syntax = arrayOf(
        Text.literal("modules")
    )
) {
    override fun execute(arguments: Array<String>) {
        ChatUtil.info(
            Text.empty()
                .append("All ")
                .append(Text.literal(ModuleManager.modules.size.toString()).withColor(ThemeHandler.secondary.rgb))
                .append(" modules.")
        )
        ModuleManager.modules.sortedBy {
            it.category.name
        }.forEach { module ->
            ChatUtil.info(
                Text.empty()
                    .append(Text.literal(module.name))
                    .append(Text.literal(" » ").withColor(if (module.get()) Color(0, 255, 0).rgb else Color(255, 0, 0).rgb))
                    .append(Text.literal(module.description)),
                false
            )
        }
    }
}