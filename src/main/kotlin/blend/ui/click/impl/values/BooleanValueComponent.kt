package blend.ui.click.impl.values

import blend.handler.ThemeHandler
import blend.ui.click.impl.AbstractValueComponent
import blend.ui.click.impl.ModuleComponent
import blend.util.animation.Animation
import blend.util.animation.ColorAnimation
import blend.util.render.Alignment
import blend.util.render.ColorUtil.mixWith
import blend.util.render.ColorUtil.textColor
import blend.util.render.DrawUtil
import blend.value.BooleanValue

class BooleanValueComponent(
    parent: ModuleComponent,
    override val value: BooleanValue
): AbstractValueComponent(
    parent, value
) {

    private var background by ColorAnimation().also { color ->
        color.set(ThemeHandler.background())
    }
    private var toggle = Animation()
    private var click by Animation()

    override fun render(mouseX: Double, mouseY: Double) {
        val h = 10.0
        val w = h * 2
        val r = h + click - 4.0

        with(DrawUtil) {
            string(value.name, x + 4, y + (height / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
            roundedRect(x + width - 5, y + (height / 2), w, h, h / 2, background, Alignment.CENTER_RIGHT)
            roundedRect(x + width - (h + (w / 2) * toggle.get()), y + height / 2, r, r, r / 2, background.textColor, Alignment.CENTER)
        }
        background = if (value.get()) ThemeHandler.primary else ThemeHandler.staticBackground.mixWith(ThemeHandler.primary, 0.25)
        toggle.animate(if (value.get()) 0.0 else 1.0)
        click = if (toggle.finished) 0.0 else 2.0
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        value.toggle()
        return true
    }

}