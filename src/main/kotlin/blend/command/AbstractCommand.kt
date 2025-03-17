package blend.command

abstract class AbstractCommand(
    val aliases: Array<String>,
    val description: String
) {
    val name = aliases.first()

    abstract fun execute(args: Array<String>)
    open fun invalidArgument(args: Array<String>) {}
}