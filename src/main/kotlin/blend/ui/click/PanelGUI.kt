package blend.ui.click

import blend.Client
import blend.handler.ThemeHandler
import blend.module.impl.visual.ClickGUIModule
import blend.ui.click.impl.ModulesPanel
import blend.util.interfaces.IAccessor
import blend.util.render.Alignment
import blend.util.render.DrawUtil
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

object PanelGUI: Screen(Text.of("fr")), IAccessor {

    private val panel = ModulesPanel()

    override fun init() {
        panel.init()
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        panel.size(480.0, 270.0)
        panel.position((mc.window.scaledWidth - panel.width) / 2, (mc.window.scaledHeight - panel.height) / 2)
        DrawUtil.render {
            panel.render(mouseX.toDouble(), mouseY.toDouble())
            it.string("${Client.NAME} v${Client.VERSION}", 10, mc.window.scaledHeight - 10, 12, ThemeHandler.textColor, Alignment.BOTTOM_LEFT)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return panel.click(mouseX, mouseY, button)
    }
    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return panel.release(mouseX, mouseY, button)
    }
    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        return panel.scroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return panel.keyDown(keyCode, scanCode, modifiers)
    }

    override fun close() = ClickGUIModule.set(false)
}