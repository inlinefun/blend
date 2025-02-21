package blend.command

import net.minecraft.text.Text

abstract class AbstractCommand(
    val identifiers: Array<String>,
    val description: String,
    val syntax: Array<Text>,
) {
    val name = identifiers.first()
    abstract fun execute(arguments: Array<String>)
}