package blend.module.impl.visual

import blend.module.AbstractModule
import blend.module.ModuleCategory
import org.lwjgl.glfw.GLFW

object ClickGUIModule: AbstractModule(
    names = arrayOf("ClickGUI", "GUI"),
    description = "Shows a GUI for the user to customise the client's features.",
    category = ModuleCategory.VISUAL,
    defaultKey = GLFW.GLFW_KEY_RIGHT_SHIFT
) {

    override fun onEnable() {

    }

    override fun onDisable() {
        mc.setScreen(null)
    }

}