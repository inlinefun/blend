package blend.mixin.minecraft;

import blend.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(
            method = "<init>",
            at = @At(
                    value = "RETURN"
            )
    )
    private void initializeNanoVG(RunArgs args, CallbackInfo ci) {

    }

    @Inject(
            method = "stop",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V")
    )
    private void shutdown(CallbackInfo ci) {
        Client.INSTANCE.shutdown();
    }

}
