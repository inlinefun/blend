package blend.ui.clickgui

import blend.module.impl.visual.ClickGUIModule
import blend.util.render.DrawUtil
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

object FlatClickGUI: Screen(Text.of("fr")) {

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        DrawUtil.renderTest()
    }

    override fun close() {
        ClickGUIModule.set(false)
    }

    override fun shouldPause(): Boolean {
        return false
    }

}