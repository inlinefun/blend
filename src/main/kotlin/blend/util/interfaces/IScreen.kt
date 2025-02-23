package blend.util.interfaces

interface IScreen {
    fun init()
    fun render(mouseX: Double, mouseY: Double)
    fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean
    fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean
    fun scroll(mouseX: Double, mouseY: Double, horizontalScroll: Double, verticalScroll: Double): Boolean
    fun keyDown(keyCode: Int, scancode: Int, modifiers: Int): Boolean
    fun close()
}