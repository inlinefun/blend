package blend.ui.click.impl

import blend.handler.ThemeHandler
import blend.module.AbstractModule
import blend.ui.AbstractUIComponent
import blend.util.animation.Animation
import blend.util.animation.Easing
import blend.util.render.ColorUtil.darkerBy
import blend.util.render.ColorUtil.textColor
import blend.util.render.DrawUtil
import org.lwjgl.glfw.GLFW

class ModuleComponent(
    private val parent: ModulesPanel,
    val module: AbstractModule
): AbstractUIComponent() {

    override var x: Double
        get() = xPosAnim.get()
        set(value) = xPosAnim.animate(value)
    override var y: Double
        get() = yPosAnim.get()
        set(value) = yPosAnim.animate(value)
    override var height: Double
        get() = heightAnim.get()
        set(value) = heightAnim.animate(value)

    private val xPosAnim = Animation(Easing.cubicOut, 250)
    private val yPosAnim = Animation(Easing.cubicOut, 250)
    private var expanded = false

    val heightAnim = Animation(Easing.sineOut, 200)
    var targetHeight = 0.0

    override fun init() {

    }

    override fun render(mouseX: Double, mouseY: Double) {
        val background = ThemeHandler.background(0.2)
        with(DrawUtil) {
            intersectScissor(x, y, width, height) {
                roundedRect(x, y, width, height, 3, background)
                val nameBounds = string(module.name, x + 5, y + 5, 12, background.textColor)
                val descBounds = string(module.description, x + 5, y + 5 + nameBounds.second + 5, 8, background.textColor.darkerBy(0.75))
                targetHeight = if (expanded) 80.0 else nameBounds.second + descBounds.second + 15
            }
        }
        height = targetHeight
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(mouseX, mouseY)) {
            if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                expanded = !expanded
                return true
            }
        }
        return false
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        return false
    }

    override fun scroll(mouseX: Double, mouseY: Double, horizontalScroll: Double, verticalScroll: Double): Boolean {
        return false
    }

    override fun keyDown(keyCode: Int, scancode: Int, modifiers: Int): Boolean {
        return false
    }

    override fun close() {

    }

}
