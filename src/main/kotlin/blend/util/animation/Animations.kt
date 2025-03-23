package blend.util.animation

import java.awt.Color
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AbstractAnimation<T>(
    var easing: (Double) -> Double,
    var duration: Number
): ReadWriteProperty<Any, T> {

    protected var currentTime = 0L
    protected var startTime = 0L
    protected abstract var initialValue: T
    protected abstract var currentValue: T
    protected abstract var targetValue: T
    var finished = false

    val animating get() = currentValue != targetValue
    val progress get() = ((System.currentTimeMillis() - startTime).toDouble() / duration.toDouble()).coerceIn(0.0, 1.0)

    abstract fun animate(targetValue: T)

    fun reset() {
        startTime = System.currentTimeMillis()
        initialValue = currentValue
        finished = false
    }

    fun get() = currentValue
    fun set(currentValue: T) {
        this.currentValue = currentValue
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = currentValue
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = animate(value)

}

class Animation(
    easing: (Double) -> Double = Easing.sineOut,
    duration: Number = 150.0
): AbstractAnimation<Double>(
    easing, duration
) {

    override var initialValue = 0.0
    override var currentValue = 0.0
    public override var targetValue = 0.0

    override fun animate(targetValue: Double) {
        currentTime = System.currentTimeMillis()
        if (this.targetValue != targetValue) {
            this.targetValue = targetValue
            reset()
        } else {
            finished = (currentTime - startTime) >= duration.toLong()
            if (finished) {
                currentValue = targetValue
                return
            }
        }
        val result = easing(progress)
        currentValue =
            if (currentValue > targetValue) {
                initialValue - (initialValue - targetValue) * result
            } else {
                initialValue + (targetValue - initialValue) * result
            }
    }

}

class ColorAnimation(
    easing: (Double) -> Double = Easing.sineOut,
    duration: Number = 150.0
): AbstractAnimation<Color>(easing, duration) {

    override var initialValue: Color = Color(0, 0, 0, 0)
    override var currentValue: Color = Color(0, 0, 0, 0)
    override var targetValue: Color = Color(0, 0, 0, 0)

    override fun animate(targetValue: Color) {
        currentTime = System.currentTimeMillis()
        if (this.targetValue != targetValue) {
            this.targetValue = targetValue
            reset()
        } else {
            finished = (currentTime - startTime) >= duration.toLong()
            if (finished) {
                currentValue = targetValue
                return
            }
        }
        val result = easing(progress)
        val red = (initialValue.red + (targetValue.red - initialValue.red) * result).toInt().coerceIn(0, 255)
        val green = (initialValue.green + (targetValue.green - initialValue.green) * result).toInt().coerceIn(0, 255)
        val blu = (initialValue.blue + (targetValue.blue - initialValue.blue) * result).toInt().coerceIn(0, 255)
        val alpha = (initialValue.alpha + (targetValue.alpha - initialValue.alpha) * result).toInt().coerceIn(0, 255)

        currentValue = Color(red, green, blu, alpha)
    }
}
