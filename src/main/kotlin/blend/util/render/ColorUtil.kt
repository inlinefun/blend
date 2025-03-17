package blend.util.render

import java.awt.Color

object ColorUtil {

    val Color.textColor: Color get() = if (0.2126 * (red / 255) + 0.7152 * (green / 255) + 0.0722 * (blue / 255) > 0.5) Color.BLACK else Color.WHITE

    fun Color.alpha(alpha: Int): Color {
        return Color(this.red, this.green, this.blue, alpha.coerceIn(0, 255))
    }
    fun Color.alpha(alpha: Float): Color {
        return Color(this.red, this.green, this.blue, (alpha / 255f).toInt().coerceIn(0, 255))
    }
    fun Color.darkerBy(factor: Double): Color {
        return Color(
            (this.red * factor.coerceIn(0.0, 1.0)).toInt().coerceIn(0, 255),
            (this.green * factor.coerceIn(0.0, 1.0)).toInt().coerceIn(0, 255),
            (this.blue * factor.coerceIn(0.0, 1.0)).toInt().coerceIn(0, 255),
            this.alpha
        )
    }

}