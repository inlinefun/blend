package blend.mixin.minecraft.gui.screen;

import blend.event.ChatSendEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @Shadow protected TextFieldWidget chatField;

    @Shadow public abstract void sendMessage(String chatText, boolean addToHistory);

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ChatScreen;sendMessage(Ljava/lang/String;Z)V"),
            cancellable = true
    )
    private void onChatSend(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient.getInstance().setScreen(null);
        final ChatSendEvent event = new ChatSendEvent(this.chatField.getText());
        event.call();
        if (!event.getCancelled()) {
            this.sendMessage(event.getContent(), true);
        }
        cir.setReturnValue(true);
    }

}
