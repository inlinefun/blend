package blend.util.extensions

import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Vec3d

val LivingEntity.position
    get() = Vec3d(x, y, z)
