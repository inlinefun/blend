package blend.util.render

import java.awt.Color

object ColorUtil {

    val Color.textColor: Color get() = if (0.2126 * (red / 255) + 0.7152 * (green / 255) + 0.0722 * (blue / 255) > 0.5) Color.BLACK else Color.WHITE
    val Color.otherTextColor: Color get() {
        val luminance = 0.2126 * (red / 255.0) + 0.7152 * (green / 255.0) + 0.0722 * (blue / 255.0)
        val invertedLuminance = 1.0 - luminance // Invert the luminance
        val grayValue = (invertedLuminance * 255).toInt()
        return Color(grayValue, grayValue, grayValue)
    }

    fun Color.alpha(alpha: Int): Color {
        return Color(this.red, this.green, this.blue, alpha.coerceIn(0, 255))
    }
    fun Color.alpha(alpha: Double): Color {
        return Color(this.red, this.green, this.blue, (255 * alpha).toInt().coerceIn(0, 255))
    }
    fun Color.alpha(alpha: Float): Color {
        return Color(this.red, this.green, this.blue, (255 * alpha).toInt().coerceIn(0, 255))
    }
    fun Color.darkerBy(factor: Double): Color {
        return Color(
            (this.red * factor.coerceIn(0.0, 1.0)).toInt().coerceIn(0, 255),
            (this.green * factor.coerceIn(0.0, 1.0)).toInt().coerceIn(0, 255),
            (this.blue * factor.coerceIn(0.0, 1.0)).toInt().coerceIn(0, 255),
            this.alpha
        )
    }
    fun Color.mixWith(another: Color, factor: Double): Color {
        val otherFactor = 1.0 - factor
        val redFactor = (red * otherFactor + another.red * factor).toInt().coerceIn(0, 255)
        val greenFactor = (green * otherFactor + another.green * factor).toInt().coerceIn(0, 255)
        val blueFactor = (blue * otherFactor + another.blue * factor).toInt().coerceIn(0, 255)
        return Color(redFactor, greenFactor, blueFactor)
    }
    fun mixColors(primary: Color, secondary: Color, factor: Double): Color {
        val otherFactor = 1.0 - factor
        val redFactor = (primary.red * otherFactor + secondary.red * factor).toInt().coerceIn(0, 255)
        val greenFactor = (primary.green * otherFactor + secondary.green * factor).toInt().coerceIn(0, 255)
        val blueFactor = (primary.blue * otherFactor + secondary.blue * factor).toInt().coerceIn(0, 255)
        return Color(redFactor, greenFactor, blueFactor)
    }

}