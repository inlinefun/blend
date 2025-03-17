package blend.mixin.minecraft;

import blend.event.EventBus;
import blend.event.KeyEvent;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(
            method = "onKey",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getInactivityFpsLimiter()Lnet/minecraft/client/option/InactivityFpsLimiter;",
                    shift = At.Shift.AFTER
            )
    )
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        EventBus.INSTANCE.post(new KeyEvent(key, scancode, action, modifiers));
    }

}
