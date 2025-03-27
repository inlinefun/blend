package blend.ui.click.impl

import blend.handler.ThemeHandler
import blend.module.ModuleCategory
import blend.module.ModuleManager
import blend.ui.AbstractUIComponent
import blend.util.animation.Animation
import blend.util.animation.Easing
import blend.util.render.Alignment
import blend.util.render.ColorUtil.textColor
import blend.util.render.DrawUtil
import net.minecraft.client.MinecraftClient
import org.lwjgl.glfw.GLFW
import java.awt.Color
import kotlin.math.abs

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
    private var scrollVal = 0.0
    private var scroll by Animation()

    private val expand = Animation(Easing.linear).also {
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

                intersectScissor(x, y + initialHeight, width, height - initialHeight) {
                    components.forEach { component ->
                        component.position(x, y + fr - scroll)
                        component.render(mouseX, mouseY)
                        fr += component.height
                    }
                }

            }
        }
        val maxHeight = MinecraftClient.getInstance().window.scaledHeight - 10.0
        val totalHeight = fr
        val maxScroll = (maxHeight - y) - totalHeight
        if (totalHeight < maxHeight) {
            scrollVal = 0.0
        }
        if (components.any { it.expand.animating }) {
            expand.set(if (expanded) fr.coerceAtMost(maxHeight - y) else initialHeight)
        } else {
            height = if (expanded) fr.coerceAtMost(maxHeight - y) else initialHeight
        }
        scroll = scrollVal.coerceIn(0.0, abs(maxScroll))
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            when (mouseButton) {
                GLFW.GLFW_MOUSE_BUTTON_LEFT -> {

                }
                GLFW.GLFW_MOUSE_BUTTON_RIGHT -> {
                    expanded = !expanded
                    scrollVal = 0.0
                }
            }
            return true
        }
        return components.filter { it.isOver(mouseX, mouseY) }.any { it.click(mouseX, mouseY, mouseButton) }
    }

    override fun scroll(mouseX: Double, mouseY: Double, horizontalScroll: Double, verticalScroll: Double): Boolean {
        if (isOver(x, y + initialHeight, width, height - initialHeight, mouseX, mouseY)) {
            scrollVal = (scrollVal - verticalScroll * 10.0).coerceAtLeast(0.0)
            return true
        }
        return false
    }

    // boring
    override fun init() = components.forEach { it.init() }
    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int) = components.any { it.release(mouseX, mouseY, mouseButton) }
    override fun keyDown(keyCode: Int, scancode: Int, modifiers: Int) = components.any { it.keyDown(keyCode, scancode, modifiers) }
    override fun close() = components.forEach { it.close() }

}