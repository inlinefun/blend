package blend.util.render

import blend.util.interfaces.IAccessor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*

object DrawUtil: IAccessor {

    private var context = -1L

    @JvmStatic
    fun init() {
        context = nvgCreate(NVG_ANTIALIAS or NVG_STENCIL_STROKES)
        if (context == -1L) {
            throw IllegalStateException("DrawUtil: NanoVG context is invalid")
        }
    }

    @JvmStatic
    fun destroy() {
        nvgDelete(context)
    }

    fun render() {

    }

}