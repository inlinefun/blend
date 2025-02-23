package blend.ui.clickgui.impl

import blend.handler.impl.ThemeHandler
import blend.module.ModuleCategory
import blend.module.ModuleManager
import blend.ui.clickgui.AbstractPanel
import blend.util.animations.Animation
import blend.util.animations.Easing
import blend.util.render.DrawUtil

class ModulesPanel: AbstractPanel() {

    var categoryFilter: ModuleCategory? = null

    private val categories = ModuleCategory.entries.map { category -> CategoryComponent(this, category) }
    private val modules = ModuleManager.modules.map { module -> ModuleComponent(this, module) }
    private val scrollAnim = Animation(Easing.linear, 200)
    private var scrollOffset: Double
        get() = scrollAnim.get()
        set(value) = scrollAnim.set(value)

    override fun init() {
        categories.forEach { it.init() }
        modules.forEach { it.init() }
    }

    override fun render(mouseX: Double, mouseY: Double) {
        val background = ThemeHandler.background()
        with(DrawUtil) {
            roundedRect(x, y, width, height, 5, background)
            outlinedRoundedRect(x, y, width, height, 5, 1, ThemeHandler.primary)
            scissor(x, y, width, height) {
                // categories
                also {
                    var x = this@ModulesPanel.x + 7
                    categories.forEach {
                        it.position(x, y + 7)
                        it.render(mouseX, mouseY)
                        x += it.width + 7
                    }
                }
                // modules
                also {
                    var y = this@ModulesPanel.y + 7 + categories.first().height + 7 + scrollOffset

                    modules.forEach {
                        it.x = if (categoryFilter == null || it.module.category == categoryFilter) x + 7 else this@ModulesPanel.x + width
                        if (categoryFilter == null || it.module.category == categoryFilter) {
                            it.y = y
                        }
                        it.width = this@ModulesPanel.width - 14 // 7 from both sides
                        it.render(mouseX, mouseY)
                        if (categoryFilter == null || it.module.category == categoryFilter) {
                            y += 5 + if (modules.any { e -> e.heightAnim.animating }) it.targetHeight else it.height
                        }
                    }
                }
            }
        }
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (categories.any { it.click(mouseX, mouseY, mouseButton) }) {
            return true
        }
        if (modules.any { it.click(mouseX, mouseY, mouseButton) }) {
            return true
        }
        return false
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (categories.any { it.release(mouseX, mouseY, mouseButton) }) {
            return true
        }
        if (modules.any { it.release(mouseX, mouseY, mouseButton) }) {
            return true
        }
        return false
    }

    override fun scroll(mouseX: Double, mouseY: Double, horizontalScroll: Double, verticalScroll: Double): Boolean {
        if (categories.any { it.scroll(mouseX, mouseY, horizontalScroll, verticalScroll) }) {
            return true
        }
        if (modules.any { it.scroll(mouseX, mouseY, horizontalScroll, verticalScroll) }) {
            return true
        }
        return false
    }

    override fun keyDown(keyCode: Int, scancode: Int, modifiers: Int): Boolean {
        if (categories.any { it.keyDown(keyCode, scancode, modifiers) }) {
            return true
        }
        if (modules.any { it.keyDown(keyCode, scancode, modifiers) }) {
            return true
        }
        return false
    }

    override fun close() {
        categories.forEach { it.close() }
        modules.forEach { it.close() }
    }

}