package blend.ui

abstract class AbstractUIComponent(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var width: Double = 0.0,
    var height: Double = 0.0
) {
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