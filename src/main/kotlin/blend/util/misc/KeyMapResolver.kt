package blend.util.misc

import org.lwjgl.glfw.GLFW

object KeyMapResolver {

    private val keyNames = mapOf(
        GLFW.GLFW_KEY_LEFT_SHIFT to "Left Shift",
        GLFW.GLFW_KEY_RIGHT_SHIFT to "Right Shift",
        GLFW.GLFW_KEY_LEFT_CONTROL to "Left Ctrl",
        GLFW.GLFW_KEY_RIGHT_CONTROL to "Right Ctrl",
        GLFW.GLFW_KEY_LEFT_ALT to "Left Alt",
        GLFW.GLFW_KEY_RIGHT_ALT to "Right Alt",
        GLFW.GLFW_KEY_ENTER to "Enter",
        GLFW.GLFW_KEY_ESCAPE to "Escape",
        GLFW.GLFW_KEY_TAB to "Tab",
        GLFW.GLFW_KEY_BACKSPACE to "Backspace",
        GLFW.GLFW_KEY_DELETE to "Delete",
        GLFW.GLFW_KEY_UP to "Arrow Up",
        GLFW.GLFW_KEY_DOWN to "Arrow Down",
        GLFW.GLFW_KEY_LEFT to "Arrow Left",
        GLFW.GLFW_KEY_RIGHT to "Arrow Right",
        GLFW.GLFW_KEY_F1 to "F1",
        GLFW.GLFW_KEY_F2 to "F2",
        GLFW.GLFW_KEY_F3 to "F3",
        GLFW.GLFW_KEY_F4 to "F4",
        GLFW.GLFW_KEY_F5 to "F5",
        GLFW.GLFW_KEY_F6 to "F6",
        GLFW.GLFW_KEY_F7 to "F7",
        GLFW.GLFW_KEY_F8 to "F8",
        GLFW.GLFW_KEY_F9 to "F9",
        GLFW.GLFW_KEY_F10 to "F10",
        GLFW.GLFW_KEY_F11 to "F11",
        GLFW.GLFW_KEY_F12 to "F12",
        GLFW.GLFW_KEY_LEFT_SUPER to "Left Super",
        GLFW.GLFW_KEY_RIGHT_SUPER to "Right Super"
    )

    fun getName(keycode: Int): String {
        return GLFW.glfwGetKeyName(keycode, 0) ?: keyNames[keycode] ?: "ERR0R"
    }

}