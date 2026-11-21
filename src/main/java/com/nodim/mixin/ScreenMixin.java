package com.nodim.mixin;

import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Cancels the dark gradient that Minecraft draws behind in-world screens
 * (inventory, chests, crafting, etc.).
 *
 * Both method names are listed because the rendering methods were renamed in
 * some 26.x versions. The injection only needs one of them to match.
 */
@Mixin(Screen.class)
public class ScreenMixin {
    private static boolean nodim$logged = false;

    @Inject(
        method = { "renderTransparentBackground", "extractTransparentBackground" },
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void nodim$skipDim(CallbackInfo ci) {
        if (!nodim$logged) {
            nodim$logged = true;
            System.out.println("[nodim] Screen background method was called and cancelled");
        }
        ci.cancel();
    }
}
