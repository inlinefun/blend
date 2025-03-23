package blend.ui.click.impl.values

import blend.handler.ThemeHandler
import blend.ui.click.impl.DynamicAbstractValueComponent
import blend.ui.click.impl.ModuleComponent
import blend.util.animation.Animation
import blend.util.render.Alignment
import blend.util.render.ColorUtil.alpha
import blend.util.render.DrawUtil
import blend.value.ListValue

class ListValueComponent(
    parent: ModuleComponent,
    override val value: ListValue
): DynamicAbstractValueComponent(
    parent, value
) {

    private val yAnim = Animation()

    override fun render(mouseX: Double, mouseY: Double) {
        var fr = initialHeight
        val currentIndex = value.availableOptions.indexOf(value.get())

        with(DrawUtil) {
            intersectScissor(x, y, width, height) {
                string(value.name, x + 4, y + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
                string(value.get(), x + width - 4, y + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_RIGHT)

                intersectScissor(x, y + initialHeight, width, height - initialHeight) {
                    roundedRect(x + 4, y + initialHeight + 2, 1.5, height - (initialHeight + 4), 1, ThemeHandler.textColor.alpha(0.3))
                    value.availableOptions.forEach { value ->
                        val isSelected = this@ListValueComponent.value.get() == value
                        if (isSelected)
                            roundedRect(x + 4, yAnim.get(), 1.5, initialHeight - 4, 1, ThemeHandler.primary.alpha(0.75))
                        if (this@ListValueComponent.value.name == "Font face") {
                            string(value, x + 8, y + fr + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT, value.lowercase())
                        } else {
                            string(value, x + 8, y + fr + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
                        }
                        fr += initialHeight
                    }
                }
            }

        }

        yAnim.animate(y + ((currentIndex + 1) * initialHeight) + 2)
        height = if (expanded) fr else initialHeight
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            expanded = !expanded
            return true
        }
        var fr = initialHeight
        value.availableOptions.forEach { value ->
            if (isOver(x, y + fr, width, height, mouseX, mouseY)) {
                this.value.set(value)
            }
            fr += initialHeight
        }
        return true
    }

}