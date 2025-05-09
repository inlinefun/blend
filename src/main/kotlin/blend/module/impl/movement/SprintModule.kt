package blend.module.impl.movement

import blend.event.InputHandleEvent
import blend.module.AbstractModule
import blend.module.ModuleCategory
import blend.util.extensions.isHeld
import org.greenrobot.eventbus.Subscribe

object SprintModule: AbstractModule(
    names = arrayOf("Sprint"),
    description = "Makes the player sprint(fr)",
    category = ModuleCategory.MOVEMENT,
    defaultEnabled = true
) {

    override fun onDisable() {
        mc.options.sprintKey.isPressed = mc.options.sprintKey.isHeld()
    }

    @Subscribe
    @Suppress("unused")
    fun onHandleEvent(@Suppress("unused") event: InputHandleEvent) {
        mc.options.sprintKey.isPressed = true
    }

}