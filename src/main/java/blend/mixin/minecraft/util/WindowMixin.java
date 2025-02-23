package blend.mixin.minecraft.util;

import blend.util.render.DrawUtil;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {

    @Inject(
            method = "onWindowSizeChanged",
            at = @At(
                    value = "RETURN"
            )
    )
    private void updateSkiaSurface(long window, int width, int height, CallbackInfo ci) {
        DrawUtil.updateSurface();
    }

}
