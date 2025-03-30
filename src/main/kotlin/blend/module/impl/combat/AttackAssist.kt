package blend.module.impl.combat

import blend.module.AbstractModule
import blend.module.ModuleCategory

object AttackAssist: AbstractModule(
    names = arrayOf("Attack Assist", "Trigger Bot"),
    description = "Helps attack at very precise time.",
    category = ModuleCategory.COMBAT
) {

    private val holdOnly by boolean("Hold only", true)

    override fun onEnable() {

    }

    override fun onDisable() {

    }

}