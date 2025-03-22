package blend.util.render

import blend.handler.ThemeHandler
import blend.util.interfaces.IAccessor
import blend.util.misc.MiscUtil
import com.mojang.blaze3d.systems.RenderSystem
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.opengl.GL33
import org.lwjgl.system.MemoryStack
import java.awt.Color
import java.nio.ByteBuffer

@Suppress("MemberVisibilityCanBePrivate", "unused")
object DrawUtil: IAccessor {

    private var context = -1L
    private lateinit var poppins: ByteBuffer
    private lateinit var lato: ByteBuffer

    @JvmStatic
    fun init() {
        context = nvgCreate(NVG_ANTIALIAS or NVG_STENCIL_STROKES)
        if (context == -1L) {
            throw IllegalStateException("DrawUtil: NanoVG context is invalid")
        }
        poppins = MiscUtil.getResourceAsByteBuffer("fonts/poppins.ttf")
        lato = MiscUtil.getResourceAsByteBuffer("fonts/lato.ttf")
        nvgCreateFontMem(context, "poppins", poppins, false)
        nvgCreateFontMem(context, "lato", lato, false)
    }

    @JvmStatic
    fun destroy() {
        if (context != -1L) {
            nvgDelete(context)
        }
    }

    fun render(fr: (DrawUtil) -> Unit) {
        MemoryStack.stackPush().use { _ ->
            preRender()
            begin()
            scale {
                fr(this)
            }
            end()
            postRender()
        }
    }

    fun savedState(fr: () -> Unit) {
        save()
        fr()
        restore()
    }
    fun scale(scale: Number = mc.window.scaleFactor, fr: () -> Unit) {
        save()
        scale(scale)
        fr()
        restore()
    }
    fun translate(x: Number, y: Number, fr: () -> Unit) {
        save()
        translate(x, y)
        fr()
        restore()
    }
    fun scissor(x: Number, y: Number, width: Number, height: Number, fr: () -> Unit) {
        savedState {
            scissor(x, y, width, height)
            fr()
            resetScissor()
        }
    }
    fun intersectScissor(x: Number, y: Number, width: Number, height: Number, fr: () -> Unit) {
        savedState {
            intersectScissor(x, y, width, height)
            fr()
        }
    }

    fun save() = nvgSave(context)
    fun restore() = nvgRestore(context)
    fun scale() = scale(mc.window.scaleFactor)
    fun scale(scale: Number) = scale(scale, scale)
    fun scale(x: Number, y: Number) = nvgScale(context, x.toFloat(), y.toFloat())
    fun translate(x: Number, y: Number) = nvgTranslate(context, x.toFloat(), y.toFloat())
    fun scissor(x: Number, y: Number, width: Number, height: Number) = nvgScissor(context, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
    fun intersectScissor(x: Number, y: Number, width: Number, height: Number) = nvgIntersectScissor(context, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
    fun resetScissor() = nvgResetScissor(context)
    private fun begin() = nvgBeginFrame(context, mc.window.width.toFloat(), mc.window.height.toFloat(), 1.0f)
    private fun end() = nvgEndFrame(context)

    fun rect(x: Number, y: Number, width: Number, height: Number, color: Color, alignment: Alignment = Alignment.TOP_LEFT) {
        path {
            color.nvgColor { nvgColor ->
                val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
                nvgRect(context, position.first, position.second, width.toFloat(), height.toFloat())
                nvgFillColor(context, nvgColor)
                nvgFill(context)
            }
        }
    }
    fun rect(x: Number, y: Number, width: Number, height: Number, gradient: Gradient, alignment: Alignment = Alignment.TOP_LEFT) {
        path {
            val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
            nvgRect(context, position.first, position.second, width.toFloat(), height.toFloat())
            gradient.withPaint(context) { paint ->
                nvgFillPaint(context, paint)
            }
            nvgFill(context)
        }
    }
    fun outlinedRect(x: Number, y: Number, width: Number, height: Number, stroke: Number, color: Color, alignment: Alignment = Alignment.TOP_LEFT) {
        path {
            color.nvgColor { nvgColor ->
                val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
                nvgRect(context, position.first, position.second, width.toFloat(), height.toFloat())
                nvgStrokeWidth(context, stroke.toFloat())
                nvgStrokeColor(context, nvgColor)
                nvgStroke(context)
            }
        }
    }
    fun outlinedRect(x: Number, y: Number, width: Number, height: Number, stroke: Number, gradient: Gradient, alignment: Alignment = Alignment.TOP_LEFT) {
        path {
            val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
            nvgRect(context, position.first, position.second, width.toFloat(), height.toFloat())
            nvgStrokeWidth(context, stroke.toFloat())
            gradient.withPaint(context) { paint ->
                nvgStrokePaint(context, paint)
            }
            nvgStroke(context)
        }
    }
    fun roundedRect(x: Number, y: Number, width: Number, height: Number, cornerRadius: Number, color: Color, alignment: Alignment = Alignment.TOP_LEFT) {
        path {
            color.nvgColor { nvgColor ->
                val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
                nvgRoundedRect(context, position.first, position.second, width.toFloat(), height.toFloat(), cornerRadius.toFloat())
                nvgFillColor(context, nvgColor)
                nvgFill(context)
            }
        }
    }
    fun roundedRect(x: Number, y: Number, width: Number, height: Number, cornerRadius: Number, gradient: Gradient, alignment: Alignment = Alignment.TOP_LEFT) {
        path {
            val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
            nvgRoundedRect(context, position.first, position.second, width.toFloat(), height.toFloat(), cornerRadius.toFloat())
            gradient.withPaint(context) { paint ->
                nvgFillPaint(context, paint)
            }
            nvgFill(context)
        }
    }
    fun roundedRect(x: Number, y: Number, width: Number, height: Number, cornerRadius: DoubleArray, color: Color, alignment: Alignment = Alignment.TOP_LEFT) {
        require(cornerRadius.size == 4) {
            throw IllegalArgumentException("Provided array size isn't 4")
        }
        path {
            color.nvgColor { nvgColor ->
                val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
                nvgRoundedRectVarying(context, position.first, position.second, width.toFloat(), height.toFloat(), cornerRadius[0].toFloat(), cornerRadius[1].toFloat(), cornerRadius[2].toFloat(), cornerRadius[3].toFloat())
                nvgFillColor(context, nvgColor)
                nvgFill(context)
            }
        }
    }
    fun roundedRect(x: Number, y: Number, width: Number, height: Number, cornerRadius: DoubleArray, gradient: Gradient, alignment: Alignment = Alignment.TOP_LEFT) {
        require(cornerRadius.size == 4) {
            throw IllegalArgumentException("Provided array size isn't 4")
        }
        path {
            val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
            nvgRoundedRectVarying(context, position.first, position.second, width.toFloat(), height.toFloat(), cornerRadius[0].toFloat(), cornerRadius[1].toFloat(), cornerRadius[2].toFloat(), cornerRadius[3].toFloat())
            gradient.withPaint(context) { paint ->
                nvgFillPaint(context, paint)
            }
            nvgFill(context)
        }
    }
    fun outlinedRoundedRect(x: Number, y: Number, width: Number, height: Number, cornerRadius: Number, stroke: Number, color: Color, alignment: Alignment = Alignment.TOP_LEFT) {
        path {
            color.nvgColor { nvgColor ->
                val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
                nvgRoundedRect(context, position.first, position.second, width.toFloat(), height.toFloat(), cornerRadius.toFloat())
                nvgStrokeWidth(context, stroke.toFloat())
                nvgStrokeColor(context, nvgColor)
                nvgStroke(context)
            }
        }
    }
    fun outlinedRoundedRect(x: Number, y: Number, width: Number, height: Number, cornerRadius: Number, stroke: Number, gradient: Gradient, alignment: Alignment = Alignment.TOP_LEFT) {
        path {
            val position = alignment.getPosition(width.toFloat(), height.toFloat())(x.toFloat(), y.toFloat())
            nvgRoundedRect(context, position.first, position.second, width.toFloat(), height.toFloat(), cornerRadius.toFloat())
            nvgStrokeWidth(context, stroke.toFloat())
            gradient.withPaint(context) { paint ->
                nvgStrokePaint(context, paint)
            }
            nvgStroke(context)
        }
    }

    /**
     * @return Pair of string width and height
     */
    fun string(text: String, x: Number, y: Number, fontSize: Number, color: Color, alignment: Alignment = Alignment.TOP_LEFT, fontName: String = ThemeHandler.fontName): Pair<Double, Double> {
        val bounds = FloatArray(4)
        path {
            // shadow first
            Color(0, 0, 0, 160).nvgColor { nvgColor ->
                nvgFillColor(context, nvgColor)
            }
            nvgFontFace(context, fontName)
            nvgFontBlur(context, 2.0f)
            nvgFontSize(context, fontSize.toFloat())
            nvgTextAlign(context, alignment.getFontAlignmentFlags())
            nvgText(context, x.toFloat() + 2.0f, y.toFloat() + 2.0f, text)

            color.nvgColor { nvgColor ->
                nvgFillColor(context, nvgColor)
            }
            nvgFontFace(context, fontName)
            nvgFontBlur(context, 0.0f)
            nvgFontSize(context, fontSize.toFloat())
            nvgTextAlign(context, alignment.getFontAlignmentFlags())
            nvgTextBounds(context, x.toFloat(), y.toFloat(), text, bounds)
            nvgText(context, x.toFloat(), y.toFloat(), text)
        }
        return bounds[2].toDouble() - bounds[0].toDouble() to bounds[3].toDouble() - bounds[1].toDouble()
    }

    private fun preRender() {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableDepthTest()
    }
    private fun postRender() {
        RenderSystem.disableCull()
        RenderSystem.enableDepthTest()
        RenderSystem.disableBlend()

        RenderSystem.activeTexture(GL33.GL_TEXTURE0)
        RenderSystem.bindTexture(0)
    }
    private inline fun path(fr: () -> Unit) {
        nvgBeginPath(context)
        fr()
        nvgClosePath(context)
    }
    internal inline fun Color.nvgColor(stuffWithTheNvgColor: (NVGColor) -> Unit) {
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

    fun rainbowBar(x: Number, y: Number, width: Number, height: Number, radius: Number, alignment: Alignment = Alignment.TOP_LEFT) {
        val yay = width.toDouble() / 10.0
        var x = x.toDouble()
        for (hue in 0..9) {
            roundedRect(
                x, y, yay, height,
                doubleArrayOf(
                    if (hue == 0) {
                        radius.toDouble()
                    } else {
                        0.0
                    },
                    if (hue == 9) {
                        radius.toDouble()
                    } else {
                        0.0
                    },
                    if (hue == 9) {
                        radius.toDouble()
                    } else {
                        0.0
                    },
                    if (hue == 0) {
                        radius.toDouble()
                    } else {
                        0.0
                    }
                ),
                LinearGradient(
                    Pair(
                        Color.getHSBColor(hue / 10.0f, 1.0f, 1.0f),
                        Color.getHSBColor((hue / 10.0f) + 0.1f, 1.0f, 1.0f),
                    ),
                    Pair(x, y),
                    Pair(x + yay, y)
                ),
                alignment
            )
            x += yay
        }
    }

}