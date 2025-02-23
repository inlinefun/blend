package blend.ui.clickgui

import blend.Client
import blend.handler.impl.ThemeHandler
import blend.module.impl.visual.ClickGUIModule
import blend.ui.clickgui.impl.ModulesPanel
import blend.util.alignment.Alignment
import blend.util.interfaces.IAccessor
import blend.util.render.DrawUtil
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

object FlatClickGUI: Screen(Text.of("fr")), IAccessor {

    private val panel = ModulesPanel()

    override fun init() {
        panel.init()
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        panel.size(480.0, 270.0)
        panel.position((mc.window.scaledWidth - panel.width) / 2, (mc.window.scaledHeight - panel.height) / 2)
        DrawUtil.render {
            panel.render(mouseX.toDouble(), mouseY.toDouble())
            it.string("${Client.name} v${Client.version}", 10, mc.window.scaledHeight - 10, 12, ThemeHandler.textColor, Alignment.BOTTOM_LEFT)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (panel.click(mouseX, mouseY, button)) {
            return true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (panel.release(mouseX, mouseY, button)) {
            return true
        }
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (panel.keyDown(keyCode, scanCode, modifiers)) {
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun close() {
        ClickGUIModule.set(false)
    }

    override fun shouldPause(): Boolean {
        return false
    }

}