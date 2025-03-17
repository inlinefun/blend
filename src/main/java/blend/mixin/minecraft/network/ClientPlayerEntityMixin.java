package blend.mixin.minecraft.network;

import blend.event.EventBus;
import blend.event.PlayerTickEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(
            method = "tick",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onClientPlayerUpdate(CallbackInfo ci) {
        EventBus.INSTANCE.post(PlayerTickEvent.INSTANCE);
    }

}
