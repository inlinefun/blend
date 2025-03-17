@file:Suppress("MemberVisibilityCanBePrivate", "unused")
package blend.handler

import blend.command.CommandManager
import blend.event.ChatSendEvent
import blend.event.EventBus
import blend.event.KeyEvent
import blend.event.Subscriber
import blend.module.ModuleManager
import blend.module.impl.visual.ClientThemeModule
import blend.util.interfaces.IAccessor
import blend.util.render.ColorUtil.textColor
import blend.util.render.LinearGradient
import org.greenrobot.eventbus.Subscribe
import org.lwjgl.glfw.GLFW
import java.awt.Color

sealed interface Handler: Subscriber, IAccessor {
    fun init() = EventBus.register(this)
}

object KeybindHandler: Handler {
    @Subscribe
    fun onKey(event: KeyEvent) {
        if (mc.currentScreen != null)
            return
        ModuleManager.modules
            .filter { module ->
                event.key == module.bind.get() && ((module.bind.hold && event.action == GLFW.GLFW_RELEASE) || event.action == GLFW.GLFW_PRESS)
            }.forEach {
                it.toggle()
            }
    }
}

object ChatInputHandler: Handler {
    @Subscribe
    fun onChatSend(event: ChatSendEvent) {
        // not empty(wouldn't be), longer than just a dot, has a dot, isn't just ... or something
        if (event.content.isNotBlank() && event.content.length > 1 && event.content.startsWith(".") && !event.content.startsWith("..")) {
            val args = event.content.removePrefix(".").split(" ").toMutableList()
            if (args.isNotEmpty()) {
                CommandManager.commands.firstOrNull { command ->
                    command.aliases.any {
                        it.equals(args.first(), true)
                    }
                }?.let { command ->
                    event.cancel()
                    args.removeFirst()
                    command.execute(args.toTypedArray())
                    mc.inGameHud.chatHud.addToMessageHistory(event.content)
                }
            }
        }
    }
}

object ThemeHandler: Handler {

    val primary get() = ClientThemeModule.accent
    val secondary get() = ClientThemeModule.secondary
    val fontName get() = ClientThemeModule.fontFace.lowercase()
    val staticBackground get() = Color(0, 0, 0)
    val textColor get() = background().textColor
    val linearGradient get() = LinearGradient(
        colors = Pair(primary, secondary),
        source = Pair(0, 0),
        destination = Pair(mc.window.scaledWidth, mc.window.scaledHeight)
    )

    fun background(ratio: Double = 0.10): Color {
        return if (ClientThemeModule.tint) {
            val fr = ratio.coerceIn(0.0, 1.0)
            fun lerp(start: Int, end: Int, yes: Double) = ((1 - yes) * start + yes * end).toInt().coerceIn(0, 255)
            return Color(
                lerp(staticBackground.red, primary.red, fr),
                lerp(staticBackground.green, primary.green, fr),
                lerp(staticBackground.blue, primary.blue, fr)
            )
        } else {
            staticBackground
        }
    }
}
