package blend.ui.click.impl

import blend.module.ModuleCategory
import blend.ui.AbstractUIComponent

class CategoryComponent(
    val category: ModuleCategory
): AbstractUIComponent() {

    override fun render(mouseX: Double, mouseY: Double) {

    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        return false
    }

}