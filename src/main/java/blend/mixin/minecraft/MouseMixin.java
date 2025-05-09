package blend.mixin.minecraft;

import blend.accessors.MouseAccessor;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mouse.class)
public abstract class MouseMixin implements MouseAccessor {

    @Shadow
    protected abstract void onMouseButton(long window, int button, int action, int mods);

    @Override
    public void blend_onMouse(long window, int button, int action, int modifiers) {
        onMouseButton(window, button, action, modifiers);
    }

}
