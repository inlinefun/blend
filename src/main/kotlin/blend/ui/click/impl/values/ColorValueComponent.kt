package blend.ui.click.impl.values

import blend.handler.ThemeHandler
import blend.ui.click.impl.AbstractValueComponent
import blend.ui.click.impl.ModuleComponent
import blend.util.render.Alignment
import blend.util.render.DrawUtil
import blend.value.ColorValue

class ColorValueComponent(
    parent: ModuleComponent,
    override val value: ColorValue
): AbstractValueComponent(
    parent, value
) {

    override fun render(mouseX: Double, mouseY: Double) {
        with(DrawUtil) {
            string(value.name, x + 4, y + (height / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
        }
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        return false
    }

}