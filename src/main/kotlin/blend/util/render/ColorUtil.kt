package blend.util.render

import org.lwjgl.nanovg.NVGColor
import java.awt.Color

object ColorUtil {

    fun Color.alpha(alpha: Int): Color {
        return Color(this.red, this.green, this.blue, alpha.coerceIn(0, 255))
    }
    fun Color.alpha(alpha: Float): Color {
        return Color(this.red, this.green, this.blue, (alpha / 255f).toInt().coerceIn(0, 255))
    }
    fun Color.nvgColor(stuffWithTheNvgColor: (NVGColor) -> Unit) {
        NVGColor
            .malloc()
            .r((red / 255f).coerceIn(0f, 1f))
            .g((green / 255f).coerceIn(0f, 1f))
            .b((blue / 255f).coerceIn(0f, 1f))
            .a((alpha / 255f).coerceIn(0f, 1f))
            .use { nvgColor ->
                stuffWithTheNvgColor(nvgColor)
            }
    }

}