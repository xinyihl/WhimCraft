package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Configurations;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum Mixins {
    PatternEncoder("PatternEncoder", false, () -> Configurations.AEMOD_CONFIG.patternEncoder, "ae2fc", "appliedenergistics2"),
    CellTotalTypes("CellTotalTypes", false, null, "appliedenergistics2", "mekeng", "nae2"),
    IgnoreParallel("IgnoreParallel", true, () -> Configurations.MMCE_CONFIG.isIgnoreParallel, "appliedenergistics2", "modularmachinery"),
    PerkLevelManager("PerkLevelManager", true, null, "astralsorcery"),
    ManaRecipeWrapper("ManaRecipeWrapper", true, () -> Configurations.BOTANIA_CONFIG.showMana, "botania"),
    LinkFlowerPool("LinkFlowerPool", true, () -> Configurations.BOTANIA_CONFIG.linkFlowerToPool, "botania"),
    FixItemIngredientsCrash("FixItemIngredientsCrash", true, null, "extrautils2"),
    RainbowGenerator("RainbowGenerator", true, null, "extrautils2"),
    GuguMEAspectBus("GuguMEAspectBus", true, null, "gugu-utils", "modularmachinery", "appliedenergistics2", "thaumcraft"),
    MmceMEAspectBus("MmceMEAspectBus", true, null, "modularmachinery", "appliedenergistics2", "thaumcraft"),
    FurnaceXp("FurnaceXp", true, () -> Configurations.IC2_CONFIG.changeXPOrb, "ic2"),
    MmceMain("MmceMain", true, null, "modularmachinery"),
    MobUtilsXpSupport("MobUtilsXpSupport", true, () -> Configurations.MOBUTILS_CONFIG.otherXpSupport, "mob_grinding_utils"),
    ModTweakerApplyAction("ModTweakerApplyAction", true, () -> Configurations.MODTWMOD_CONFIG.loadCompleteMixinEnable, "modtweaker"),
    AuraChunkUpdate("AuraChunkUpdate", true, () -> !Configurations.NATURE_CONFIG.auraChunkUpdateEnable, "naturesaura"),
    FixTileMelterCrash("FixTileMelterCrash", true, null, "tcomplement"),
    EntityMobFarmDrop("EntityMobFarmDrop", true, null, "tinymobfarm"),
    PassKeyToSearchInAeGui("PassKeyToSearchInAeGui", true, () -> Configurations.AEMOD_CONFIG.searchInGui, "appliedenergistics2"),
    RecipeTransferOrderAe2("RecipeTransferOrder.appliedenergistics2", true, () -> Configurations.GENERAL_CONFIG.JeiTransferMixinEnable, "appliedenergistics2", "modularmachinery"),
    RecipeTransferOrderNee("RecipeTransferOrder.neenergistics", true, () -> Configurations.GENERAL_CONFIG.JeiTransferMixinEnable, "appliedenergistics2", "modularmachinery", "neenergistics"),
    RecipeTransferOrderAe2Fc("RecipeTransferOrder.ae2fc", true, () -> Configurations.GENERAL_CONFIG.JeiTransferMixinEnable, "appliedenergistics2", "modularmachinery", "ae2fc"),
    GenerateBiomeAuraBase("GenerateBiomeAuraBase", true, () -> Configurations.GENERAL_CONFIG.generateBiomeAuraBaseEnable, "thaumcraft"),
    GuguStarlight("GuguStarlight", true, () -> Configurations.ASMOD_CONFIG.starLightHatchFix, "gugu-utils", "astralsorcery")
    ;

    final String mixinName;
    final String[] modId;
    final boolean needAllLoad;
    final Supplier<Boolean> otherConfig;

    Mixins(String mixinName, boolean needAllLoad, Supplier<Boolean> otherConfig, String... modId) {
        this.mixinName = mixinName;
        this.otherConfig = otherConfig;
        this.needAllLoad = needAllLoad;
        this.modId = modId;
    }

    public static List<String> getMixinConfigs() {
        List<String> mixinConfigs = new ArrayList<>();
        Arrays.stream(Mixins.values()).forEach(value -> {
            if (value.needAllLoad) {
                if (Arrays.stream(value.modId).allMatch(Loader::isModLoaded)) {
                    if (value.otherConfig == null || value.otherConfig.get()) {
                        mixinConfigs.add("mixins/mixin.whimcraft." + value.mixinName + ".json");
                    }
                }
            } else {
                if (value.otherConfig == null || value.otherConfig.get()) {
                    mixinConfigs.addAll(Arrays.stream(value.modId).filter(Loader::isModLoaded).map(id -> "mixins/mixin.whimcraft." + value.mixinName + "." + id + ".json").collect(Collectors.toList()));
                }
            }
        });
        return mixinConfigs;
    }
}
