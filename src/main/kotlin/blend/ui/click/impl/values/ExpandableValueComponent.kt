package blend.ui.click.impl.values

import blend.handler.ThemeHandler
import blend.ui.click.impl.DynamicAbstractValueComponent
import blend.ui.click.impl.IParentValueComponent
import blend.ui.click.impl.ModuleComponent
import blend.util.render.Alignment
import blend.util.render.ColorUtil.alpha
import blend.util.render.DrawUtil
import blend.value.AbstractNumberValue
import blend.value.BooleanValue
import blend.value.ColorValue
import blend.value.ExpandableValue
import blend.value.KeyValue
import blend.value.ListValue

class ExpandableValueComponent(
    parent: ModuleComponent,
    override val value: ExpandableValue
): DynamicAbstractValueComponent(
    parent,
    value
), IParentValueComponent {

    val components = value.values.map { value ->
        when(value) {
            is BooleanValue -> BooleanValueComponent(this, value)
            is ColorValue -> ColorValueComponent(this, value)
            is ListValue -> ListValueComponent(this, value)
            is AbstractNumberValue -> NumberValueComponent(this, value)
            is KeyValue -> KeyValueComponent(this, value)
            else -> {
                throw IllegalStateException("Could not initialize a component for ${value::class.simpleName}")
            }
        }
    }

    override fun render(mouseX: Double, mouseY: Double) {
        var fr = initialHeight

        with(DrawUtil) {
            intersectScissor(x, y, width, height) {
                string(value.name, x + (initialHeight / 4), y + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
                string(if (expanded) "+" else "-", x + width - (initialHeight / 2), y + (initialHeight / 2), 9, ThemeHandler.textColor, Alignment.CENTER_LEFT)
                roundedRect(x + 2, y + initialHeight + 2, 1.5, height - (initialHeight + if (expanded) 4 else 0), 1, ThemeHandler.secondary.alpha(0.5))

                components.filter { component ->
                    component.value.visibility()
                }.forEach { component ->
                    component.position(x + 4, y + fr)
                    component.width = width - 4
                    component.render(mouseX, mouseY)
                    fr += component.height
                }

            }
        }

        if (components.filter { it is DynamicAbstractValueComponent }.map { it as DynamicAbstractValueComponent }.any { it.expand.animating }) {
            expand.set(if (expanded) fr else initialHeight)
        } else {
            height = if (expanded) fr else initialHeight
        }
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            expanded = !expanded
            return true
        }
        return components.filter { it.value.visibility() && it.isOver(mouseX, mouseY) }.any { it.click(mouseX, mouseY, mouseButton) }
    }

    // boring
    override fun init() = components.forEach { it.init() }
    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int) = components.any { it.release(mouseX, mouseY, mouseButton) }
    override fun keyDown(keyCode: Int, scancode: Int, modifiers: Int) = components.any { it.keyDown(keyCode, scancode, modifiers) }
    override fun close() = components.forEach { it.close() }
}