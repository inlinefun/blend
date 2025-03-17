package blend.ui

import blend.util.interfaces.IScreen

abstract class AbstractUIComponent(
    open var x: Double = 0.0,
    open var y: Double = 0.0,
    open var width: Double = 0.0,
    open var height: Double = 0.0
): IScreen {
    fun position(x: Double, y: Double) {
        this.x = x
        this.y = y
    }
    fun size(width: Double, height: Double) {
        this.width = width
        this.height = height
    }
    fun isOver(mouseX: Double, mouseY: Double): Boolean {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height
    }
}