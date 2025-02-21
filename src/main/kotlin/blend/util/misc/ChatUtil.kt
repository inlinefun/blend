package blend.util.misc

import blend.Client
import blend.handler.impl.ThemeHandler
import blend.util.interfaces.IAccessor
import net.minecraft.text.Text
import java.awt.Color

@Suppress("MemberVisibilityCanBePrivate", "unused")
object ChatUtil: IAccessor {

    fun info(message: String, prefix: Boolean = true) = info(Text.of(message), prefix)
    fun info(message: Text, prefix: Boolean = true) {
        val content = Text.empty()
        if (prefix) {
            content.append(Text.literal(Client.name).withColor(ThemeHandler.primary.rgb))
            content.append(Text.literal(" » ").withColor(Color(0, 255, 0).rgb))
        }
        content.append(message)
        mc.inGameHud.chatHud.addMessage(content)
    }
    fun warn(message: String, prefix: Boolean = true) = warn(Text.of(message), prefix)
    fun warn(message: Text, prefix: Boolean = true) {
        val content = Text.empty()
        if (prefix) {
            content.append(Text.literal(Client.name).withColor(ThemeHandler.primary.rgb))
            content.append(Text.literal(" » ").withColor(Color(255, 160, 0).rgb))
        }
        content.append(message)
        mc.inGameHud.chatHud.addMessage(content)
    }
    fun error(message: String, prefix: Boolean = true) = error(Text.of(message), prefix)
    fun error(message: Text, prefix: Boolean = true) {
        val content = Text.empty()
        if (prefix) {
            content.append(Text.literal(Client.name).withColor(ThemeHandler.primary.rgb))
            content.append(Text.literal(" » ").withColor(Color(255, 0, 0).rgb))
        }
        content.append(message)
        mc.inGameHud.chatHud.addMessage(content)
    }

}