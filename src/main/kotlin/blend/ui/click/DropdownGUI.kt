package blend.ui.click

import blend.Client
import blend.handler.ThemeHandler
import blend.module.ModuleCategory
import blend.module.impl.visual.ClickGUIModule
import blend.ui.click.impl.CategoryComponent
import blend.util.interfaces.IAccessor
import blend.util.render.Alignment
import blend.util.render.DrawUtil
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

object DropdownGUI: Screen(Text.of("fr")), IAccessor {

    private val components: List<CategoryComponent>

    init {
        var x = 20.0
        components = ModuleCategory.entries.map { category ->
            CategoryComponent(category).also {
                it.x = x
                it.y = 20.0
                x += it.width + 10.0
            }
        }
    }

    override fun init() {
        components.forEach { it.init() }
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        with(DrawUtil) {
            render {
                components.forEach {
                    it.render(mouseX.toDouble(), mouseY.toDouble())
                }
                string("${Client.NAME} v${Client.VERSION}", 10, mc.window.scaledHeight - 10, 12, ThemeHandler.textColor, Alignment.BOTTOM_LEFT)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return components.any { it.click(mouseX, mouseY, button) }
    }
    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return components.any { it.release(mouseX, mouseY, button) }
    }
    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        return components.any { it.scroll(mouseX, mouseY, horizontalAmount, verticalAmount) }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (components.any { it.keyDown(keyCode, scanCode, modifiers) })
            return true
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun close() {
        components.forEach { it.close() }
        ClickGUIModule.set(false)
    }
}