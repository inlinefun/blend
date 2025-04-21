package blend.command

object CommandManager {

    private val _commands = mutableListOf<AbstractCommand>()
    val commands get() = _commands.toList()

    fun initialize() {

    }

}