package blend.mixin.minecraft.option;

import blend.accessors.KeybindingAccessor;
import blend.accessors.MouseAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public abstract class KeybindingMixin implements KeybindingAccessor {

    @Shadow
    private InputUtil.Key boundKey;

    @Override
    public InputUtil.Key blend_getBoundKey() {
        return this.boundKey;
    }

    @Override
    public void blend_simulateAction(boolean pressed) {
        final int action = pressed ? GLFW.GLFW_PRESS : GLFW.GLFW_RELEASE;
        final long handle = MinecraftClient.getInstance().getWindow().getHandle();
        final InputUtil.Type type = boundKey.getCategory();
        switch (type) {
            case KEYSYM -> {
                MinecraftClient.getInstance().keyboard.onKey(handle, boundKey.getCode(), 0, action, 0);
            }
            case MOUSE -> {
                ((MouseAccessor)MinecraftClient.getInstance().mouse).blend_onMouse(handle, boundKey.getCode(), action, 0);
            }
            default -> {

            }
        }
    }

}
