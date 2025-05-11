package blend.module.impl.combat

import blend.event.MouseTickEvent
import blend.handler.TargetFilter
import blend.handler.TargetHandler
import blend.handler.TargetHandler.sortedBy
import blend.handler.TargetPriority
import blend.module.AbstractModule
import blend.module.ModuleCategory
import blend.util.extensions.getNeededRotations
import blend.util.extensions.isHeld
import blend.util.extensions.position
import net.minecraft.entity.LivingEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.MaceItem
import net.minecraft.item.SwordItem
import net.minecraft.util.math.MathHelper
import org.greenrobot.eventbus.Subscribe

object AimAssistModule: AbstractModule(
    names = arrayOf("Aim Assist"),
    description = "Helps aim at targets.",
    category = ModuleCategory.COMBAT
) {

    private val speed by double("Speed multiplier", 1.0, 0.5, 2.0, 0.1)
    private val range by double("Range", 3.3, 3.0, 4.5, 0.1)
    private val sort by list("Sorting", TargetPriority.all)
    private val holdOnly by boolean("Hold", true)
    private val notBreakingBlock by boolean("Not when mining", true)

    private val targets = parent("Targets")
    private val players by boolean("Players", true, targets)
    private val hostile by boolean("Hostile", true, targets)
    private val neutral by boolean("Neutral", true, targets)
    private val filterAngry by boolean("Filter angry mobs", true, targets) { neutral }
    private val passive by boolean("Passive", true, targets)

    private val weapons = parent("Weapons only")
    private val weaponsOnly by boolean("Enabled", true, weapons)
    private val axe by boolean("Axe", true, weapons) { weaponsOnly }
    private val sword by boolean("Sword", true, weapons) { weaponsOnly }
    private val mace by boolean("Mace", true, weapons) { weaponsOnly }

    private val filter
        get() = TargetFilter(players, hostile, neutral, filterAngry, passive)
    private var target: LivingEntity? = null

    @Subscribe
    @Suppress("unused")
    fun onMouseTick(event: MouseTickEvent) {
        if (mc.currentScreen != null)
            return
        if (holdOnly && !mc.options.attackKey.isHeld())
            return
        if (notBreakingBlock && interactions.isBreakingBlock)
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
        findTarget()
        target?.let {
            val rotations = player.getNeededRotations(it)
            val yawDiff = MathHelper.wrapDegrees(rotations.yaw - player.yaw)
            val pitchDiff = MathHelper.wrapDegrees(rotations.pitch - player.pitch)
            val sensitivity = mc.options.mouseSensitivity.value.toFloat() * 0.6f + 0.2f
            val factor = (sensitivity * sensitivity * sensitivity) * 8.0f
            event.deltaX += (yawDiff / factor) * speed
            event.deltaY += (pitchDiff / factor) * speed
        }
    }

    private fun findTarget() {
        target = TargetHandler.filterTargets(filter)
            .filter { target ->
                player.position.distanceTo(target.position) <= range
            }
            .sortedBy(TargetPriority.from(sort))
            .firstOrNull()
    }

}