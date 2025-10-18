package com.eightsidedsquare.angling.mixin;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {

    @Inject(method = "shouldRenderSide", at = @At("HEAD"), cancellable = true)
    private static void shouldRenderSide(FluidState fluid, BlockState state, Direction side, FluidState fluidFromSide, CallbackInfoReturnable<Boolean> cir) {
        if(fluid.isIn(FluidTags.WATER)) {
            if(state.isIn(ConventionalBlockTags.GLASS_BLOCKS)) {
                cir.setReturnValue(false);
            }
        }
    }

}
