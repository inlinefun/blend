package blend.mixin.minecraft.util;

import blend.accessors.InputUtilKeyAccessor;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InputUtil.Key.class)
public abstract class InputUtilKeyMixin implements InputUtilKeyAccessor {

    @Shadow
    @Final
    private InputUtil.Type type;

    @Override
    public InputUtil.Type blend_getType() {
        return type;
    }

}
