package blend.util.extensions

import blend.Client
import blend.accessors.KeybindingAccessor
import blend.handler.ThemeHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import java.awt.Color

inline val mc: MinecraftClient
    get() = MinecraftClient.getInstance()

// Keybinding
val KeyBinding.keycode: Int
    get() {
        return (this as KeybindingAccessor).blend_getBoundKey().code
    }

fun KeyBinding.isKeyDown(): Boolean {
    return InputUtil.isKeyPressed(mc.window.handle, keycode)
}

// Text
fun Text.addChatInfo(prefix: Boolean = true) {
    val content = Text.empty()
    if (prefix) {
        content.append(Text.literal(Client.NAME).withColor(ThemeHandler.primary.rgb))
        content.append(Text.literal(" » ").withColor(Color(0, 255, 25).rgb))
    }
    content.append(this)
    mc.inGameHud.chatHud.addMessage(content)
}
fun Text.addChatWarn(prefix: Boolean = true) {
    val content = Text.empty()
    if (prefix) {
        content.append(Text.literal(Client.NAME).withColor(ThemeHandler.primary.rgb))
        content.append(Text.literal(" » ").withColor(Color(255, 150, 0).rgb))
    }
    content.append(this)
    mc.inGameHud.chatHud.addMessage(content)
}
fun Text.addChatError(prefix: Boolean = true) {
    val content = Text.empty()
    if (prefix) {
        content.append(Text.literal(Client.NAME).withColor(ThemeHandler.primary.rgb))
        content.append(Text.literal(" » ").withColor(Color(255, 25, 25).rgb))
    }
    content.append(this)
    mc.inGameHud.chatHud.addMessage(content)
}
