package blend.module.impl.movement

import blend.module.AbstractModule
import blend.module.ModuleCategory

object SprintModule: AbstractModule(
    names = arrayOf("Sprint"),
    description = "Makes the player sprint...",
    category = ModuleCategory.MOVEMENT,
    defaultEnabled = true
) {

}