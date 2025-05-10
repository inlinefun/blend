package blend.util.extensions

import blend.util.data.Rotation
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.LivingEntity
import kotlin.math.atan2
import kotlin.math.sqrt

fun ClientPlayerEntity.getNeededRotations(target: LivingEntity): Rotation {
    val deltaX = target.x - this.x
    val deltaY = target.eyeY - this.eyeY
    val deltaZ = target.z - this.z
    val distanceXZ = sqrt(deltaX * deltaX + deltaZ * deltaZ)
    val yaw = Math.toDegrees(atan2(deltaZ, deltaX)) - 90
    val pitch = -Math.toDegrees(atan2(deltaY, distanceXZ))
    return Rotation(yaw, pitch)
}
