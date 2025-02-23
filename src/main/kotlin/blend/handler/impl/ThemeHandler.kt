package blend.handler.impl

import blend.handler.IHandler
import blend.module.impl.visual.ClientThemeModule
import blend.util.render.ColorUtil.textColor
import java.awt.Color

@Suppress("MemberVisibilityCanBePrivate", "unused")
object ThemeHandler: IHandler {

    val primary get() = ClientThemeModule.accent
    val secondary get() = ClientThemeModule.secondary
    val fontName get() = ClientThemeModule.fontFace.lowercase()
    val staticBackground get() = Color(0, 0, 0)
    val textColor get() = background().textColor

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