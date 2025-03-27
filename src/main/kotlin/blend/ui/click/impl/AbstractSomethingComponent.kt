package blend.ui.click.impl

import blend.ui.AbstractUIComponent
import blend.util.animation.Animation
import blend.util.animation.Easing
import blend.value.AbstractValue

interface IParentValueComponent {
    var width: Double
}

abstract class AbstractValueComponent(
    parent: IParentValueComponent,
    protected open val value: AbstractValue<*>,
    height: Double = 18.0
): AbstractUIComponent(
    width = parent.width,
    height = height
)

abstract class DynamicAbstractValueComponent(
    parent: IParentValueComponent,
    override val value: AbstractValue<*>,
    height: Double = 18.0
): AbstractValueComponent(
    parent, value, height
) {
    open val initialHeight = height
    val expand = Animation(Easing.linear).also { animation ->
        animation.set(initialHeight)
    }
    override var height: Double
        get() = expand.get()
        set(value) {
            expand.animate(value)
        }
    var expanded = false
}
