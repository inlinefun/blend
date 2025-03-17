package blend.util.interfaces

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld

interface IAccessor {
    val mc: MinecraftClient
        get() = MinecraftClient.getInstance()
    val player: ClientPlayerEntity
        get() = MinecraftClient.getInstance().player!!
    val world: ClientWorld
        get() = MinecraftClient.getInstance().world!!
}