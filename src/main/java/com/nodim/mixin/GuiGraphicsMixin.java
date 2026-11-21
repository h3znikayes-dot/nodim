package com.nodim.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fallback for clients (like Lunar) that may draw the dim themselves.
 * Skips any full-screen gradient that uses the exact vanilla dim colors.
 *
 * The target is given as a string so the project still compiles if the class
 * name differs. require = 0 means it does nothing (instead of crashing) if the
 * class or method signature does not match this Minecraft version.
 */
@Mixin(targets = "net.minecraft.client.gui.GuiGraphicsExtractor")
public class GuiGraphicsMixin {
    private static boolean nodim$logged = false;

    @Inject(
        method = "fillGradient(IIIIII)V",
        at = @At("HEAD"),
        cancellable = true,
        require = 0
    )
    private void nodim$skipDimGradient(int x0, int y0, int x1, int y1, int colorFrom, int colorTo, CallbackInfo ci) {
        // -1072689136 and -804253680 are the vanilla dim colors (0xC0101010 and 0xD0101010)
        if (colorFrom == -1072689136 && colorTo == -804253680) {
            if (!nodim$logged) {
                nodim$logged = true;
                System.out.println("[nodim] Cancelled a vanilla-colored dim gradient via fillGradient");
            }
            ci.cancel();
        }
    }
}
