package blend.command

import blend.command.impl.ConfigCommand
import blend.util.interfaces.Initializable

object CommandManager: Initializable {

    lateinit var commands: Array<AbstractCommand>

    override fun init() {
        commands = listOf(
            ConfigCommand()
        ).sortedBy {
            it.name
        }.toTypedArray()
    }

}