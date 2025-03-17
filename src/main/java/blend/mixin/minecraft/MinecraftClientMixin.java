package blend.mixin.minecraft;

import blend.Client;
import blend.event.EventBus;
import blend.event.InputHandleEvent;
import blend.util.render.DrawUtil;
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
                    value = "NEW",
                    target = "(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/render/item/HeldItemRenderer;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/client/render/BufferBuilderStorage;)Lnet/minecraft/client/render/GameRenderer;"
            )
    )
    private void initializeNanoVGContext(RunArgs args, CallbackInfo ci) {
        DrawUtil.init();
    }
    
    @Inject(
            method = "close",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onClose(CallbackInfo ci) {
        try {
            Client.INSTANCE.shutdown();
            DrawUtil.destroy();
        } catch (Exception ignored) {}
    }

    @Inject(
            method = "handleInputEvents",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onHandleInputs(CallbackInfo ci) {
        EventBus.INSTANCE.post(InputHandleEvent.INSTANCE);
    }
    
}
