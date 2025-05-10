package blend.module.impl.combat

import blend.event.InputHandleEvent
import blend.module.AbstractModule
import blend.module.ModuleCategory
import blend.util.extensions.isHeld
import blend.util.extensions.simulateAction
import net.minecraft.item.AxeItem
import net.minecraft.item.MaceItem
import net.minecraft.item.SwordItem
import net.minecraft.util.hit.EntityHitResult
import org.greenrobot.eventbus.Subscribe

object AttackAssistModule: AbstractModule(
    names = arrayOf("Attack Assist", "Trigger Bot"),
    description = "Helps you attack at of the item cooldown.",
    category = ModuleCategory.COMBAT
) {

    private val offset by double("Offset", 0.0, 0.0, 5.0, 0.25)
    private val holdOnly by boolean("Hold only", true)
    private val weaponsOnly by boolean("Weapons only", true)
    private val crits by boolean("Smart crits", true) // not very smart :(
    private val weapons = parent("Weapons") {
        weaponsOnly
    }
    private val axe by boolean("Axe", true, weapons)
    private val sword by boolean("Sword", true, weapons)
    private val mace by boolean("Mace", true, weapons)

    @Subscribe
    @Suppress("unused")
    fun onInputEvent(@Suppress("unused") unused: InputHandleEvent) {
        if (mc.crosshairTarget == null || mc.crosshairTarget !is EntityHitResult || mc.currentScreen != null)
            return
        if (holdOnly && !mc.options.attackKey.isHeld())
            return
        if (mc.interactionManager!!.isBreakingBlock)
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
        val isFalling = !player.isOnGround && player.velocity.y <= 0.2
        val isGroundedAttack = player.isOnGround
        val canNotCrit = player.isInLava || player.isTouchingWater || player.isSprinting || !crits

        if (isCooldownOver && (isGroundedAttack || (isFalling && !canNotCrit))) {
            mc.options.attackKey.simulateAction(true)
        }
    }

}