package blend.command

import blend.command.impl.ModulesCommand

object CommandManager {

    lateinit var commands: List<AbstractCommand>

    fun init() {
        commands = listOf(
            ModulesCommand()
        )
    }

}