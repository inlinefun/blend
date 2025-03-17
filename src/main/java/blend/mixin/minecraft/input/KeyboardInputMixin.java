package blend.mixin.minecraft.input;

import blend.event.PlayerInputEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "NEW",
                    target = "(ZZZZZZZ)Lnet/minecraft/util/PlayerInput;"
            )
    )
    private @NotNull PlayerInput modifyKeyboardInput(PlayerInput original) {
        final PlayerInputEvent event = new PlayerInputEvent(original);
        event.call();
        return event.getAsPlayerInput();
    }

}
