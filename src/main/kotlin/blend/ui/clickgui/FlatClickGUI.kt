package blend.ui.clickgui

import blend.util.alignment.Alignment
import blend.util.render.DrawUtil
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.awt.Color

object FlatClickGUI: Screen(Text.of("fr")) {

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        DrawUtil.render {
            DrawUtil.rect(50, 50, 50, 50, Color.WHITE, Alignment.CENTER)
            DrawUtil.string("fr", 10, 10, 12, Color.WHITE)
        }
    }

    override fun shouldPause(): Boolean {
        return false
    }

}