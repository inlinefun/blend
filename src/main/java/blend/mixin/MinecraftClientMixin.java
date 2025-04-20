package blend.mixin;

import blend.Client;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(
            method = "close",
            at = @At(
                    value = "HEAD"
            )
    )
    private void shutdown(CallbackInfo ci) {
        try {
            Client.INSTANCE.shutdown();
        } catch(Exception ignored) {}
    }

}
