package blend.mixin.minecraft.option;

import blend.accessors.KeybindingAccessor;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public abstract class KeybindingMixin implements KeybindingAccessor {

    @Shadow
    private InputUtil.Key boundKey;

    public InputUtil.Key blend_getBoundKey() {
        return this.boundKey;
    }

}
