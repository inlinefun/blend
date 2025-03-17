package blend.util.animation

import kotlin.math.*

object Easing {

    val linear: (Double) -> Double = { x -> x}

    val sineIn: (Double) -> Double = { x -> 1.0 - cos((x * PI) / 2.0) }
    val sineOut: (Double) -> Double = { x -> sin((x * PI) / 2.0)}
    val sineInOut: (Double) -> Double = { x -> -(cos(PI * x) - 1.0) / 2.0}

    val cubicOut: (Double) -> Double = {x -> 1.0 - (1.0 - x).pow(3.0) }
    val circOut: (Double) -> Double = { x -> sqrt(1.0 - (x - 1.0).pow(2.0)) }

}