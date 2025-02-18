package blend.handler.impl

import blend.event.KeyEvent
import blend.handler.IHandler
import blend.module.ModuleManager
import org.greenrobot.eventbus.Subscribe
import org.lwjgl.glfw.GLFW

object KeybindingHandler: IHandler {

    @Subscribe
    fun onKeyEvent(event: KeyEvent) {
        ModuleManager.modules
            .filter { module ->
                if (module.bind.hold)
                    event.action == GLFW.GLFW_RELEASE
                else
                    event.action == GLFW.GLFW_PRESS
            }.forEach { module ->
                module.toggle()
            }
    }

}