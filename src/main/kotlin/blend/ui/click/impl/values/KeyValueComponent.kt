package blend.ui.click.impl.values

import blend.handler.ThemeHandler
import blend.ui.click.impl.DynamicAbstractValueComponent
import blend.ui.click.impl.IParentValueComponent
import blend.ui.click.impl.ModuleComponent
import blend.util.animation.Animation
import blend.util.animation.ColorAnimation
import blend.util.misc.KeyMapResolver
import blend.util.render.Alignment
import blend.util.render.ColorUtil.mixWith
import blend.util.render.ColorUtil.textColor
import blend.util.render.DrawUtil
import blend.value.KeyValue
import org.lwjgl.glfw.GLFW

class KeyValueComponent(
    parent: IParentValueComponent,
    override val value: KeyValue
): DynamicAbstractValueComponent(
    parent, value
) {

    private var background by ColorAnimation().also { color ->
        color.set(ThemeHandler.background())
    }
    private var toggle = Animation()
    private var click by Animation()
    private var locked = false

    override fun render(mouseX: Double, mouseY: Double) {
        val h = 10.0
        val w = h * 2
        val r = h + click - 4.0
        val keyName = if (value.get() == GLFW.GLFW_KEY_UNKNOWN) "None" else KeyMapResolver.getName(value.get())

        with(DrawUtil) {
            intersectScissor(x, y, width, height) {
                string(value.name, x + 4, y + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
                if (locked) {
                    string("Press a key", x + width - 4, y + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_RIGHT)
                } else {
                    string("Key: $keyName", x + width - 4, y + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_RIGHT)
                }

                string("Hold", x + 4, y + initialHeight + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
                roundedRect(x + width - 5, y + initialHeight + (initialHeight / 2), w, h, h / 2, background, Alignment.CENTER_RIGHT)
                roundedRect(x + width - (h + (w / 2) * toggle.get()), y + initialHeight + (initialHeight / 2), r, r, r / 2, background.textColor, Alignment.CENTER)
            }
        }

        height = if (value.get() == GLFW.GLFW_KEY_UNKNOWN) initialHeight else initialHeight * 2
        background = if (value.hold) ThemeHandler.primary else ThemeHandler.staticBackground.mixWith(ThemeHandler.primary, 0.25)
        toggle.animate(if (value.hold) 0.0 else 1.0)
        click = if (toggle.finished) 0.0 else 2.0
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            locked = !locked
            return true
        }
        if (value.get() != GLFW.GLFW_KEY_UNKNOWN && isOver(x, y + initialHeight, width, initialHeight, mouseX, mouseY)) {
            value.hold(!value.hold)
            return true
        }
        return false
    }

    override fun keyDown(keyCode: Int, scancode: Int, modifiers: Int): Boolean {
        if (locked) {
            if (keyCode == GLFW.GLFW_KEY_UNKNOWN || keyCode == GLFW.GLFW_KEY_ESCAPE) {
                value.set(GLFW.GLFW_KEY_UNKNOWN)
            } else {
                value.set(keyCode)
            }
            locked = false
            return true
        }
        return false
    }

    override fun init() {
        locked = false
    }

}