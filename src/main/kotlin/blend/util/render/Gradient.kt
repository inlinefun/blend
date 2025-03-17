package blend.util.render

import blend.util.render.DrawUtil.nvgColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG.nvgLinearGradient
import org.lwjgl.nanovg.NanoVG.nvgRadialGradient
import java.awt.Color

sealed interface Gradient {
    fun withPaint(context: Long, stuffWithNVGPaint: (NVGPaint) -> Unit)
}

data class LinearGradient(
    val colors: Pair<Color, Color>,
    val source: Pair<Number, Number>,
    val destination: Pair<Number, Number>
): Gradient {
    override fun withPaint(context: Long, stuffWithNVGPaint: (NVGPaint) -> Unit) {
        NVGPaint
            .calloc()
            .use { paint ->
                colors.first.nvgColor { first ->
                    colors.second.nvgColor { second ->
                        nvgLinearGradient(
                            context,
                            source.first.toFloat(), source.second.toFloat(),
                            destination.first.toFloat(), destination.second.toFloat(),
                            first, second,
                            paint
                        )
                    }
                }
            }
    }
}

data class RadialGradient(
    val colors: Pair<Color, Color>,
    val origin: Pair<Number, Number>,
    val radius: Pair<Number, Number>
): Gradient {
    override fun withPaint(context: Long, stuffWithNVGPaint: (NVGPaint) -> Unit) {
        NVGPaint
            .calloc()
            .use { paint ->
                colors.first.nvgColor { first ->
                    colors.second.nvgColor { second ->
                        nvgRadialGradient(
                            context,
                            origin.first.toFloat(), origin.second.toFloat(),
                            radius.first.toFloat(), radius.second.toFloat(),
                            first, second,
                            paint
                        )
                    }
                }
            }
    }
}