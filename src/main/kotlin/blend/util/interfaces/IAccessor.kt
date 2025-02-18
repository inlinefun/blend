package blend.util.interfaces

import net.minecraft.client.MinecraftClient

interface IAccessor {
    val mc get() = MinecraftClient.getInstance()!!
    val player get() = mc.player!!
    val world get() = mc.world!!
}