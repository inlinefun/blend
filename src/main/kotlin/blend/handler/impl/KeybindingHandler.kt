package blend.handler.impl

import blend.event.KeyEvent
import blend.handler.IHandler
import blend.module.ModuleManager
import org.greenrobot.eventbus.Subscribe
import org.lwjgl.glfw.GLFW

object KeybindingHandler: IHandler {

    @Subscribe
    fun onKeyEvent(event: KeyEvent) {
        if (mc.currentScreen != null)
            return
        ModuleManager.modules
            .filter { module ->
                module.bind.get() == event.key
                &&
                if (module.bind.hold)
                    event.action == GLFW.GLFW_RELEASE
                else
                    event.action == GLFW.GLFW_PRESS
            }.forEach { module ->
                module.toggle()
            }
    }

}