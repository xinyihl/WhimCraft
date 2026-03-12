package com.xinyihl.whimcraft.common.mixins.extrautils2;

import com.rwtema.extrautils2.network.XUPacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Modify Terraformer tooltip display
 */
@Mixin(targets = "com.rwtema.extrautils2.tile.TileTerraformer$ContainerTerraformer$3", remap = false)
public abstract class TileTerraformerContainerMixin {

    @Redirect(
            method = "constructText",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/rwtema/extrautils2/utils/helpers/StringHelper;format(I)Ljava/lang/String;",
                    ordinal = 0
            )
    )
    private static String outputSuccessTxt(int value) {
        return value == 0 ? "\u16B7" : "\u2714";
    }

    @Redirect(
            method = "constructText",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/rwtema/extrautils2/utils/helpers/StringHelper;format(I)Ljava/lang/String;",
                    ordinal = 1
            )
    )
    private static String negateSecondStringFormatAppend(int value) {
        return "";
    }

    @ModifyConstant(method = "constructText", constant = @Constant(stringValue = "/"))
    private String modifyTFTooltipMiddle(String value) {
        return "";
    }

    @Redirect(
            method = "constructText",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/rwtema/extrautils2/network/XUPacketBuffer;readInt()I",
                    ordinal = 4
            )
    )
    private int processTFSuccess(XUPacketBuffer packet) {
        int cur = packet.readInt();
        int max = packet.readInt();
        return cur < max ? 0 : 1;
    }

    @Redirect(
            method = "constructText",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/rwtema/extrautils2/network/XUPacketBuffer;readInt()I",
                    ordinal = 5
            )
    )
    private int negateSecondTFCall(XUPacketBuffer packet) {
        return 0;
    }
}
