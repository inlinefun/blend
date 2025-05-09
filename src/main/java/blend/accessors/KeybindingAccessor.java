package blend.accessors;

import net.minecraft.client.util.InputUtil;

public interface KeybindingAccessor {
    InputUtil.Key blend_getBoundKey();
    void blend_simulateAction(boolean pressed);
}
