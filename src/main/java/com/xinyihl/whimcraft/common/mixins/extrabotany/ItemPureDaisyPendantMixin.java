package com.xinyihl.whimcraft.common.mixins.extrabotany;

import com.meteor.extrabotany.common.item.equipment.bauble.ItemPureDaisyPendant;
import com.xinyihl.whimcraft.Configurations;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.recipe.RecipePureDaisy;

@Mixin(value = ItemPureDaisyPendant.class, remap = false)
public abstract class ItemPureDaisyPendantMixin {

    @Inject(method = "findRecipe", at = @At("RETURN"), cancellable = true)
    private void filterBlacklistedRecipe(World world, BlockPos coords, CallbackInfoReturnable<RecipePureDaisy> cir) {
        RecipePureDaisy recipe = cir.getReturnValue();
        if (recipe == null) {
            return;
        }

        Block block = recipe.getOutputState().getBlock();
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) {
            return;
        }

        for (String blacklistedBlock : Configurations.EXTRABOTANY_CONFIG.pureDaisyPendantOutputBlacklist) {
            if (registryName.toString().equals(blacklistedBlock)) {
                cir.setReturnValue(null);
                return;
            }
        }
    }
}