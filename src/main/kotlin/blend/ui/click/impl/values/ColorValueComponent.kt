package blend.ui.click.impl.values

import blend.handler.ThemeHandler
import blend.ui.click.impl.DynamicAbstractValueComponent
import blend.ui.click.impl.ModuleComponent
import blend.util.animation.Animation
import blend.util.render.Alignment
import blend.util.render.ColorUtil.alpha
import blend.util.render.ColorUtil.textColor
import blend.util.render.DrawUtil
import blend.util.render.LinearGradient
import blend.value.ColorValue
import java.awt.Color

class ColorValueComponent(
    parent: ModuleComponent,
    override val value: ColorValue
): DynamicAbstractValueComponent(
    parent, value
) {

    private var pickerR by Animation()
    private var hueR by Animation()
    private var transparencyR by Animation()

    private var picker = false
    private var transparency = false
    private var hue = false

    override fun render(mouseX: Double, mouseY: Double) {
        var fr = initialHeight
        val rgbW = 8.0
        val tW = if (value.translucent) 8.0 else 0.0
        val rgbX = x + (width - (4.0 + rgbW + if (tW == 0.0) 0.0 else tW + 4.0))
        val tX = x + (width - 4.0 - tW)
        val size = width - (4.0 + rgbW + 4.0 + if (tW == 0.0) 4.0 else tW + 8.0)
        val relativeX = ((mouseX - (x + 4.0)) / ((x + 4.0 + size) - (x + 4.0))).coerceIn(0.0, 1.0)
        val relativeY = ((mouseY - (y + initialHeight + 4)) / ((y + initialHeight + 4 + size) - (y + initialHeight + 4))).coerceIn(0.0, 1.0)

        if (picker) {
            value.saturation = relativeX.toFloat()
            value.brightness = (1.0 - relativeY).toFloat()
        }

        if (hue) {
            value.hue = relativeY.toFloat()
        }
        if (transparency) {
            value.alpha = (1.0 - relativeY).toFloat()
        }

        with(DrawUtil) {
            intersectScissor(x, y, width, height) {

                string(value.name, x + 4, y + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
                roundedRect(x + (width - 4), y + (initialHeight / 2), 24, 12, 2, value.get(), Alignment.CENTER_RIGHT)
                outlinedRoundedRect(x + (width - 4), y + (initialHeight / 2), 24, 12, 2, 1, ThemeHandler.textColor, Alignment.CENTER_RIGHT)

                roundedRect(x + 4, y + initialHeight + 4, size, size, 4, LinearGradient(
                    Pair(Color.WHITE, Color.getHSBColor(value.hue, 1.0f, 1.0f)),
                    Pair(x + 4, y + initialHeight + 4),
                    Pair(x + 4 + size, y + initialHeight + 4 + size)
                ))
                roundedRect(x + 4, y + initialHeight + 4, size, size, 4, LinearGradient(
                    Pair(Color.BLACK.alpha(0), Color.BLACK),
                    Pair(x + 4 + size, y + initialHeight + 4),
                    Pair(x + 4 + size, y + initialHeight + 4 + size)
                ))
                rainbowBar(rgbX, y + initialHeight + 4, rgbW, size, 2)
                if (tW != 0.0)
                    roundedRect(tX, y + initialHeight + 4, tW, size, 2, LinearGradient(
                        Pair(value.get(), value.get().textColor.textColor),
                        Pair(tX, y + initialHeight + 4),
                        Pair(tX, y + initialHeight + 4 + size)
                    ))

                roundedRect(x + 4 + (size * value.saturation), y + initialHeight + 4 + (size * (1.0 - value.brightness)), pickerR, pickerR, 3, ThemeHandler.textColor, Alignment.CENTER)
                roundedRect(x + 4 + (size * value.saturation), y + initialHeight + 4 + (size * (1.0 - value.brightness)), 2 * (pickerR - 6), 2 * (pickerR - 6), 3, value.get(), Alignment.CENTER)

                roundedRect(rgbX + (rgbW / 2), y + initialHeight + 4 + (size * value.hue), hueR, hueR, 3, ThemeHandler.textColor, Alignment.CENTER)
                roundedRect(rgbX + (rgbW / 2), y + initialHeight + 4 + (size * value.hue), 2 * (hueR - 6), 2 * (hueR - 6), 2, Color.getHSBColor(value.hue, 1.0f, 1.0f), Alignment.CENTER)

                if (value.translucent) {
                    roundedRect(tX + (tW / 2), y + initialHeight + 4 + (size * (1.0 - value.alpha)), transparencyR, transparencyR, 3, ThemeHandler.textColor, Alignment.CENTER)
                    roundedRect(tX + (tW / 2), y + initialHeight + 4 + (size * (1.0 - value.alpha)), 2 * (transparencyR - 6), 2 * (transparencyR - 6), 2, LinearGradient(
                        Pair(value.get(), value.get().textColor.textColor),
                        Pair(tX, y + initialHeight + 4),
                        Pair(tX, y + initialHeight + 4 + size)
                    ), Alignment.CENTER)
                }

                fr += size + 4

                string(String.format("#%06X", (0xFFFFFF and value.get().rgb)), x + 4, y + fr + (initialHeight / 2), 8, ThemeHandler.textColor, Alignment.CENTER_LEFT)
                fr += initialHeight
            }
        }

        height = if (expanded) fr else initialHeight
        hueR = if (hue) 8.0 else 6.0
        pickerR = if (picker) 8.0 else 6.0
        transparencyR = if (transparency) 8.0 else 6.0
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        val rgbW = 8.0
        val tW = if (value.translucent) 8.0 else 0.0
        val rgbX = x + (width - (4.0 + rgbW + if (tW == 0.0) 0.0 else tW + 4.0))
        val tX = x + (width - 4.0 - tW)
        val size = width - (4.0 + rgbW + 4.0 + if (tW == 0.0) 4.0 else tW + 8.0)
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            expanded = !expanded
            return true
        }
        if (isOver(x + 4, y + initialHeight + 4, size, size, mouseX, mouseY)) {
            picker = true
            return true
        }
        if (isOver(rgbX, y + initialHeight + 4, rgbW, size, mouseX, mouseY)) {
            hue = true
            return true
        }
        if (value.translucent && isOver(tX, y + initialHeight + 4, tW, size, mouseX, mouseY)) {
            transparency = true
            return true
        }
        return false
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        picker = false
        transparency = false
        hue = false
        return false
    }

}