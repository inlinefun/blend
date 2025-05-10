package blend.handler

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PiglinEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity

object TargetHandler: Handler {

    fun filterTargets(filter: TargetFilter): List<LivingEntity> {
        return world.entities.filter { entity ->
            entity is LivingEntity && entity != player
        }.map { livingEntity ->
            livingEntity as LivingEntity
        }.filter { entity ->
            when(entity) {
                is PlayerEntity -> filter.players
                is Angerable, is PiglinEntity -> { // positioned above since PiglinEntity is of type HostileEntity
                    if (filter.filterNeutral) {
                        (entity as MobEntity).isAttacking
                    } else {
                        filter.neutral
                    }
                }
                is PassiveEntity -> filter.passive
                is HostileEntity -> filter.hostile
                else -> false
            }
        }
    }

}

data class TargetFilter(
    val players: Boolean,
    val hostile: Boolean,
    val neutral: Boolean,
    val filterNeutral: Boolean,
    val passive: Boolean
)
