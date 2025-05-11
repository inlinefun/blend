package blend.module.impl.combat

import blend.event.InputHandleEvent
import blend.handler.TargetFilter
import blend.module.AbstractModule
import blend.module.ModuleCategory
import blend.util.extensions.isHeld
import blend.util.extensions.simulateAction
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.AxeItem
import net.minecraft.item.MaceItem
import net.minecraft.item.SwordItem
import net.minecraft.util.hit.EntityHitResult
import org.greenrobot.eventbus.Subscribe
import kotlin.math.ceil

object AttackAssistModule: AbstractModule(
    names = arrayOf("Attack Assist", "Trigger Bot"),
    description = "Helps you attack at of the item cooldown.",
    category = ModuleCategory.COMBAT
) {

    private val offset by double("Offset", 0.0, 0.0, 5.0, 0.25)
    private val holdOnly by boolean("Hold only", true)
    private val crits by boolean("Smart crits", true)

    private val targets = parent("Targets only")
    private val targetsOnly by boolean("Enabled", true, targets)
    private val players by boolean("Players", true, targets) { targetsOnly }
    private val hostile by boolean("Hostile", true, targets) { targetsOnly }
    private val neutral by boolean("Neutral", true, targets) { targetsOnly }
    private val filterAngry by boolean("Filter angry mobs", true, targets) { targetsOnly && neutral }
    private val passive by boolean("Passive", true, targets) { targetsOnly }

    private val weapons = parent("Weapons only")
    private val weaponsOnly by boolean("Enabled", true, weapons)
    private val axe by boolean("Axe", true, weapons) { weaponsOnly }
    private val sword by boolean("Sword", true, weapons) { weaponsOnly }
    private val mace by boolean("Mace", true, weapons) { weaponsOnly }

    private val filter
        get() = TargetFilter(players, hostile, neutral, filterAngry, passive)

    @Subscribe
    @Suppress("unused")
    fun onInputEvent(@Suppress("unused") unused: InputHandleEvent) {
        if (mc.crosshairTarget == null || mc.crosshairTarget !is EntityHitResult || (mc.crosshairTarget as EntityHitResult).entity !is LivingEntity || mc.currentScreen != null)
            return
        if (targetsOnly && !filter.isValidTarget((mc.crosshairTarget as EntityHitResult).entity as LivingEntity)) {
            return
        }
        if (holdOnly && !mc.options.attackKey.isHeld())
            return
        if (interactions.isBreakingBlock)
            return
        if (weaponsOnly) {
            val item = player.mainHandStack.item ?: return
            val allowed = when(item) {
                is AxeItem -> axe
                is SwordItem -> sword
                is MaceItem -> mace
                else -> false
            }
            if (!allowed)
                return
        }
        val isCooldownOver = player.getAttackCooldownProgress(offset.toFloat()) >= 1.0
        if (isCooldownOver && !shouldWaitForCrit()) {
            mc.options.attackKey.simulateAction(true)
        }
    }

    fun shouldWaitForCrit(): Boolean {
        if (!crits) return false

        if (mc.options.jumpKey.isHeld()) {
            if (player.isClimbing || player.isTouchingWater || player.hasStatusEffect(StatusEffects.BLINDNESS) || player.hasVehicle()) {
                return false
            }
            if (player.velocity.y < -0.08)
                return false

            val attackCooldownTicks = ceil(1.0 / player.attackCooldownProgressPerTick).toInt()
            if (attackCooldownTicks <= 15) {
                return true // Wait until crit conditions are met
            }
        }

        return false // If not holding jump or no need to wait
    }

}