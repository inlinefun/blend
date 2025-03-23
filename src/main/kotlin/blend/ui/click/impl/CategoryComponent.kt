package blend.ui.click.impl

import blend.handler.ThemeHandler
import blend.module.ModuleCategory
import blend.module.ModuleManager
import blend.ui.AbstractUIComponent
import blend.util.animation.Animation
import blend.util.render.Alignment
import blend.util.render.ColorUtil.textColor
import blend.util.render.DrawUtil
import org.lwjgl.glfw.GLFW

class CategoryComponent(
    val category: ModuleCategory
): AbstractUIComponent(
    width = 120.0,
) {

    internal val components = ModuleManager.modules.filter { module ->
        module.category == category
    }.map { module ->
        ModuleComponent(this, module)
    }
    private val initialHeight = 24.0
    private var expanded = true

    private val expand = Animation().also {
        it.set(initialHeight)
    }

    override var height: Double
        get() = expand.get()
        set(value) {
            expand.animate(value)
        }

    override fun render(mouseX: Double, mouseY: Double) {
        val background = ThemeHandler.background();
        var fr = initialHeight

        with(DrawUtil) {
            scissor(x, y, width, height) {
                roundedRect(x, y, width, height, 5, background)
                string(category.formattedName, x + (initialHeight / 4), y + (initialHeight / 2), 12, background.textColor, Alignment.CENTER_LEFT)

                components.forEach { component ->
                    component.position(x, y + fr)
                    component.render(mouseX, mouseY)
                    fr += component.height
                }

            }
        }

        if (components.any { it.expand.animating }) {
            expand.set(if (expanded) fr else initialHeight)
        } else {
            height = if (expanded) fr else initialHeight
        }
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            when (mouseButton) {
                GLFW.GLFW_MOUSE_BUTTON_LEFT -> {

                }
                GLFW.GLFW_MOUSE_BUTTON_RIGHT -> {
                    expanded = !expanded
                }
            }
            return true
        }
        return components.filter { it.isOver(mouseX, mouseY) }.any { it.click(mouseX, mouseY, mouseButton) }
    }

    // boring
    override fun init() = components.forEach { it.init() }
    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int) = components.any { it.release(mouseX, mouseY, mouseButton) }
    override fun keyDown(keyCode: Int, scancode: Int, modifiers: Int) = components.any { it.keyDown(keyCode, scancode, modifiers) }
    override fun scroll(mouseX: Double, mouseY: Double, horizontalScroll: Double, verticalScroll: Double) = components.any { it.scroll(mouseX, mouseY, horizontalScroll, verticalScroll) }
    override fun close() = components.forEach { it.close() }

}