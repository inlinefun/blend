package blend.ui.click.impl

import blend.handler.ThemeHandler
import blend.module.AbstractModule
import blend.ui.AbstractUIComponent
import blend.ui.click.impl.values.BooleanValueComponent
import blend.ui.click.impl.values.ColorValueComponent
import blend.ui.click.impl.values.ExpandableValueComponent
import blend.ui.click.impl.values.KeyValueComponent
import blend.ui.click.impl.values.ListValueComponent
import blend.ui.click.impl.values.NumberValueComponent
import blend.util.animation.Animation
import blend.util.animation.ColorAnimation
import blend.util.animation.Easing
import blend.util.render.Alignment
import blend.util.render.ColorUtil.alpha
import blend.util.render.ColorUtil.textColor
import blend.util.render.DrawUtil
import blend.value.AbstractNumberValue
import blend.value.BooleanValue
import blend.value.ColorValue
import blend.value.ExpandableValue
import blend.value.KeyValue
import blend.value.ListValue
import org.lwjgl.glfw.GLFW

class ModuleComponent(
    private val parent: CategoryComponent,
    private val module: AbstractModule
): AbstractUIComponent(
    width = parent.width
), IParentValueComponent {

    private val components = module.values.mapNotNull { value ->
        when(value) {
            is BooleanValue -> BooleanValueComponent(this, value)
            is ColorValue -> ColorValueComponent(this, value)
            is ListValue -> ListValueComponent(this, value)
            is AbstractNumberValue -> NumberValueComponent(this, value)
            is KeyValue -> if (module.canBeEnabled) KeyValueComponent(this, value) else null
            is ExpandableValue -> ExpandableValueComponent(this, value)
            else -> {
                throw IllegalStateException("Could not initialize a component for ${value::class.simpleName}")
            }
        }
    }

    private val initialHeight = 24.0
    private var expanded = false

    val expand = Animation(Easing.linear).also { animation ->
        animation.set(initialHeight)
    }
    private var background by ColorAnimation().also { animation ->
        animation.set(if (module.get()) ThemeHandler.primary else ThemeHandler.background().alpha(0))
    }

    override var height: Double
        get() = expand.get()
        set(target) {
            expand.animate(target)
        }

    override fun render(mouseX: Double, mouseY: Double) {
        val radius = (if (expanded || parent.components.last() != this) 0.0 else 5.0) * expand.progress

        var fr = initialHeight

        with(DrawUtil) {
            intersectScissor(x, y, width, height) {
                roundedRect(x, y, width, initialHeight, doubleArrayOf(0.0, 0.0, radius, radius), background)
                string(module.name, x + (initialHeight / 4), y + (initialHeight / 2), 11, background.textColor, Alignment.CENTER_LEFT)
                roundedRect(x + 2, y + initialHeight + 2, 1.5, height - (initialHeight + if (expanded) 4 else 0), 1, ThemeHandler.primary.alpha(0.5))

                components.forEach { component ->
                    component.position(x + 4, y + fr)
                    component.width = width - 4
                    component.render(mouseX, mouseY)
                    fr += component.height
                }

            }
        }

        background = if (module.get()) ThemeHandler.primary else ThemeHandler.background().alpha(0)
        if (components.filter { it is DynamicAbstractValueComponent }.map { it as DynamicAbstractValueComponent }.any { it.expand.animating }) {
            expand.set(if (expanded) fr else initialHeight)
        } else {
            height = if (expanded) fr else initialHeight
        }
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            when (mouseButton) {
                GLFW.GLFW_MOUSE_BUTTON_LEFT -> {
                    module.toggle()
                }
                GLFW.GLFW_MOUSE_BUTTON_RIGHT -> {
                    expanded = !expanded
                }
            }
        }
        return components.filter { it.isOver(mouseX, mouseY) }.any { it.click(mouseX, mouseY, mouseButton) }
    }

    // boring
    override fun init() = components.forEach { it.init() }
    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int) = components.any { it.release(mouseX, mouseY, mouseButton) }
    override fun keyDown(keyCode: Int, scancode: Int, modifiers: Int) = components.any { it.keyDown(keyCode, scancode, modifiers) }
    override fun close() = components.forEach { it.close() }

}