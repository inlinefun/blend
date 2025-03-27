package blend.ui.click.impl.values

import blend.handler.ThemeHandler
import blend.ui.click.impl.AbstractValueComponent
import blend.ui.click.impl.ModuleComponent
import blend.util.animation.Animation
import blend.util.render.Alignment
import blend.util.render.ColorUtil.darkerBy
import blend.util.render.DrawUtil
import blend.value.AbstractNumberValue
import blend.value.DoubleValue
import blend.value.IntValue
import kotlin.math.roundToInt

class NumberValueComponent(
    parent: ModuleComponent,
    override val value: AbstractNumberValue<*>
): AbstractValueComponent(
    parent,
    value,
    height = 34.0
) {

    private var held = false
    private var holderR by Animation()

    override fun render(mouseX: Double, mouseY: Double) {
        val relativeMouse = (mouseX - (x + 4)) / (((x + 4) + (width - 8)) - (x + 4))
        val relativeValue = (value.get().toDouble() - value.minimum.toDouble()) / (value.maximum.toDouble() - value.minimum.toDouble())
        val steps = ((value.maximum.toDouble() - value.minimum.toDouble()) / value.incrementBy.toDouble()).roundToInt()
        val fill = ((width - 9) * relativeValue).coerceAtLeast(4.0)
        val eachStep = (width - 8) / steps
        if (held) {
            when (value) {
                is IntValue -> {
                    value.set((value.minimum + relativeMouse * (value.maximum - value.minimum)).roundToInt())
                }
                is DoubleValue -> {
                    value.set(value.minimum + relativeMouse * (value.maximum - value.minimum))
                }
                else -> {}
            }
        }
        with(DrawUtil) {
            string(value.name, x + 4, y + 8, 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
            string(value.get().toString(), x + width - 4, y + 8, 9, ThemeHandler.textColor, Alignment.CENTER_RIGHT)
            val y = y + 16.0

            roundedRect(x + 4, y + 5, width - 8, 4, 2, ThemeHandler.textColor.darkerBy(0.5), Alignment.CENTER_LEFT)
            roundedRect(x + 4, y + 5, fill, 4, 2, ThemeHandler.primary, Alignment.CENTER_LEFT)
            roundedRect(x + 4 + fill, y + 5, 8, 8, 4, ThemeHandler.textColor, Alignment.CENTER)
            roundedRect(x + 4 + fill, y + 5, holderR, holderR, holderR / 2, ThemeHandler.primary, Alignment.CENTER)

            if (steps <= 20) {
                for (step in 0..steps) {
                    val color = if ((value.minimum.toDouble() + (value.incrementBy.toDouble() * step)).roundToInt() == value.get()) ThemeHandler.primary else ThemeHandler.textColor
                    rect(x + 4 + (step * eachStep), y + 10, 1, 3, color)
                }
            }

        }
        holderR = if (held) 6.0 else 5.0
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(mouseX, mouseY)) {
            held = true
            return true
        }
        return false
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        held = false
        return false
    }

}
