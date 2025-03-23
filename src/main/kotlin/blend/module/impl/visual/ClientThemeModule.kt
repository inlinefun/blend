package blend.module.impl.visual

import blend.module.AbstractModule
import blend.module.ModuleCategory
import blend.util.render.DrawUtil
import java.awt.Color

object ClientThemeModule: AbstractModule(
    names = arrayOf("Theme", "Client theme"),
    description = "Customize the client to your liking.",
    category = ModuleCategory.VISUAL,
    canBeEnabled = false
) {

    val accent by color("Accent", Color(0, 160, 255))
    val secondary by color("Secondary", Color(160, 0, 255))
    val tint by boolean("Tint", true)
    val fontFace by list("Font face", DrawUtil.fonts)

}