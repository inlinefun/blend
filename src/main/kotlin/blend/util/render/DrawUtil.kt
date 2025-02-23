package blend.util.render

import blend.util.interfaces.IAccessor
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.BufferRenderer
import org.jetbrains.skia.*
import org.jetbrains.skia.Canvas.SaveLayerRec
import org.lwjgl.opengl.GL33
import org.lwjgl.system.MemoryStack
import javax.swing.Spring.height


@Suppress("MemberVisibilityCanBePrivate", "unused")
object DrawUtil: IAccessor {

    private lateinit var context: DirectContext
    private lateinit var target: BackendRenderTarget
    private lateinit var surface: Surface
    val canvas get() = surface.canvas

    @JvmStatic
    fun init() {
        context = DirectContext.makeGL()
        updateSurface()
    }

    @JvmStatic
    fun updateSurface() {
        if (this::surface.isInitialized)
            surface.close()
        if (this::target.isInitialized)
            target.close()

        target = BackendRenderTarget.makeGL(
            width = mc.window.width,
            height = mc.window.height,
            sampleCnt = 0,
            stencilBits = 8,
            fbId = mc.framebuffer.fbo,
            fbFormat = FramebufferFormat.GR_GL_RGBA8
        )
        surface = Surface.makeFromBackendRenderTarget(
            context = context,
            rt = target,
            origin = SurfaceOrigin.BOTTOM_LEFT,
            colorFormat = SurfaceColorFormat.RGBA_8888,
            colorSpace = ColorSpace.sRGB
        )!!
    }

    @JvmStatic
    fun destroy() {
        surface.close()
        target.close()
        context.close()
    }

    fun renderTest() {
        MemoryStack.stackPush().use { _ ->
            
            RenderSystem.pixelStore(GL33.GL_UNPACK_ROW_LENGTH, 0)
            RenderSystem.pixelStore(GL33.GL_UNPACK_SKIP_PIXELS, 0)
            RenderSystem.pixelStore(GL33.GL_UNPACK_SKIP_ROWS, 0)
            RenderSystem.pixelStore(GL33.GL_UNPACK_ALIGNMENT, 4)
            
            context.resetGLAll()

            val snapshot = surface.makeImageSnapshot()
            Paint().apply {
                isAntiAlias = true
                imageFilter = ImageFilter.makeBlur(10f, 10f, FilterTileMode.CLAMP)
                blendMode = BlendMode.SRC
            }.use { paint ->
                canvas.drawImage(snapshot, 0f, 0f, paint)
            }

            Paint()
                .apply {
                    isAntiAlias = true
                    setARGB(150, 255, 255, 255)
                }.use { paint ->
                    canvas.drawRRect(RRect.makeXYWH(50f, 50f, 100f, 50f, 5f), paint)
                }

            context.flush(surface)
            BufferRenderer.reset()
            GL33.glBindSampler(0, 0)
            RenderSystem.disableBlend()
            GL33.glDisable(GL33.GL_BLEND)
            RenderSystem.blendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE)
            GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE)
            RenderSystem.blendEquation(GL33.GL_FUNC_ADD)
            GL33.glBlendEquation(GL33.GL_FUNC_ADD)
            RenderSystem.colorMask(true, true, true, true)
            GL33.glColorMask(true, true, true, true)
            RenderSystem.depthMask(true)
            GL33.glDepthMask(true)
            RenderSystem.disableScissor()
            GL33.glDisable(GL33.GL_SCISSOR_TEST)
            GL33.glDisable(GL33.GL_STENCIL_TEST)
            RenderSystem.disableDepthTest()
            GL33.glDisable(GL33.GL_DEPTH_TEST)
            GL33.glActiveTexture(GL33.GL_TEXTURE0)
            RenderSystem.activeTexture(GL33.GL_TEXTURE0)
            RenderSystem.disableCull()
        }
    }

}