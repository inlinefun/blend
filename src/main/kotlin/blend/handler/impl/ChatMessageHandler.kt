package blend.handler.impl

import blend.command.CommandManager
import blend.event.ChatSendEvent
import blend.handler.IHandler
import blend.util.misc.ChatUtil
import net.minecraft.text.Text
import org.greenrobot.eventbus.Subscribe

object ChatMessageHandler: IHandler {

    @Subscribe
    fun onChatSend(event: ChatSendEvent) {
        if (event.message.isEmpty() || event.message.length == 1 || !event.message.startsWith(".") || event.message.startsWith(".."))
            return
        event.cancel()
        val args = event.message.removePrefix(".")
            .split(" ").toMutableList()
        val command = CommandManager.commands
            .find { command ->
                command.identifiers.any { identifier ->
                    identifier == args.first()
                }
            }
        if (command == null) {
            ChatUtil.error(
                Text.empty()
                    .append(Text.literal(args.first()).withColor(ThemeHandler.secondary.rgb))
                    .append(" is not a valid command.")
            )
        } else {
            command.execute(args.also { it.removeFirst() }.toTypedArray())
        }
        mc.inGameHud.chatHud.addToMessageHistory(event.message)
    }

}