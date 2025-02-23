package blend.ui.clickgui.impl

import blend.handler.impl.ThemeHandler
import blend.module.ModuleCategory
import blend.ui.AbstractUIComponent
import blend.util.alignment.Alignment
import blend.util.animations.ColorAnimation
import blend.util.animations.Easing
import blend.util.render.ColorUtil.textColor
import blend.util.render.DrawUtil

class CategoryComponent(
    private val parent: ModulesPanel,
    private val category: ModuleCategory
): AbstractUIComponent() {

    private val backgroundAnim = ColorAnimation(Easing.sineOut, 200)
    private val outlineAnim = ColorAnimation(Easing.sineOut, 200)

    override fun init() {
        backgroundAnim.set(ThemeHandler.staticBackground)
        outlineAnim.set(ThemeHandler.background(0.15))
    }

    override fun render(mouseX: Double, mouseY: Double) {
        val textBounds: Pair<Double, Double>
        val background = backgroundAnim.get()
        val outline = outlineAnim.get()
        with(DrawUtil) {
            roundedRect(x, y, width, height, height / 2, background)
            outlinedRoundedRect(x, y, width, height, height / 2, 1, outline)
            textBounds = string(category.formattedName, x + (width / 2), y + (height / 2), 10, background.textColor, Alignment.CENTER)
            width = textBounds.first + 12
            height = textBounds.second + 7
        }
        backgroundAnim.animate(if (parent.categoryFilter != null && parent.categoryFilter == category) ThemeHandler.primary else ThemeHandler.staticBackground)
        outlineAnim.animate(if (parent.categoryFilter != null && parent.categoryFilter == category) ThemeHandler.staticBackground else ThemeHandler.background(0.3))
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(mouseX, mouseY)) {
            parent.categoryFilter = if (parent.categoryFilter == null || parent.categoryFilter != category) category else null
        }
        return false
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        return false
    }

    override fun scroll(mouseX: Double, mouseY: Double, horizontalScroll: Double, verticalScroll: Double): Boolean {
        return false
    }

    override fun keyDown(keyCode: Int, scancode: Int, modifiers: Int): Boolean {
        return false
    }

    override fun close() {

    }

}