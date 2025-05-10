package blend.mixin.minecraft;

import blend.accessors.MouseAccessor;
import blend.event.MouseTickEvent;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin implements MouseAccessor {

    @Shadow
    protected abstract void onMouseButton(long window, int button, int action, int mods);
    @Shadow
    private double cursorDeltaX;
    @Shadow
    private double cursorDeltaY;

    @Override
    public void blend_onMouse(long window, int button, int action, int modifiers) {
        onMouseButton(window, button, action, modifiers);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onMouseTick(CallbackInfo ci) {
        final MouseTickEvent event = new MouseTickEvent(cursorDeltaX, cursorDeltaY);
        event.call();
        this.cursorDeltaX = event.getDeltaX();
        this.cursorDeltaY = event.getDeltaY();
    }

}
