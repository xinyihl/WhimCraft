package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Configurations;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum Mixins {
    PerkLevelManager(true, null, "astralsorcery"),
    ManaRecipeWrapper(true, () -> Configurations.BOTANIA_CONFIG.showMana, "botania"),
    LinkFlowerPool(true, () -> Configurations.BOTANIA_CONFIG.linkFlowerToPool, "botania"),
    FixItemIngredientsCrash(true, null, "extrautils2"),
    RainbowGenerator(true, null, "extrautils2"),
    FurnaceXp(true, () -> Configurations.IC2_CONFIG.changeXPOrb, "ic2"),
    MmceMain(true, null, "modularmachinery"),
    MobUtilsXpSupport(true, () -> Configurations.MOBUTILS_CONFIG.otherXpSupport, "mob_grinding_utils"),
    ModTweakerApplyAction(true, () -> Configurations.MODTWMOD_CONFIG.loadCompleteMixinEnable, "modtweaker"),
    AuraChunkUpdate(true, () -> !Configurations.NATURE_CONFIG.auraChunkUpdateEnable, "naturesaura"),
    FixTileMelterCrash(true, null, "tcomplement"),
    EntityMobFarmDrop(true, null, "tinymobfarm"),
    GenerateBiomeAuraBase(true, () -> Configurations.GENERAL_CONFIG.generateBiomeAuraBaseEnable, "thaumcraft"),
    GuguStarlight(true, () -> Configurations.ASMOD_CONFIG.starLightHatchFix, "gugu-utils", "astralsorcery"),
    CelestialCrystals(true, () -> Configurations.ASMOD_CONFIG.celestialCrystalsEnable, "astralsorcery"),
    AltarProgression(true, () -> Configurations.ASMOD_CONFIG.altarProgressionEnable, "astralsorcery"),
    //AeOrderAutoComplete(false, () -> Configurations.GENERAL_CONFIG.orderEnable, "appliedenergistics2"),
    GaiaSkill(false, () -> Configurations.BOTANIA_CONFIG.gaiaSkillEnable, "botania", "extrabotany"),
    OrechidIgnem(true, () -> Configurations.BOTANIA_CONFIG.orechidIgnemEnable, "botania"),
    GaiaCrashFix(true, () -> Configurations.BOTANIA_CONFIG.gaiaCrashFixEnable, "botania"),
    ModBrewsDuration(true, () -> Configurations.BOTANIA_CONFIG.modBrewsDurationEnable, "botania"),
    ExtraBotanyPureDaisyPendant(true, () -> Configurations.EXTRABOTANY_CONFIG.pureDaisyPendantBlacklistEnable, "extrabotany"),
    ExtraBotanyGaiaIIIEmptyPlayersCrashFix(true, () -> Configurations.EXTRABOTANY_CONFIG.gaiaIIIEmptyPlayersCrashFixEnable, "extrabotany"),
    GendustryQueenImprinting(true, () -> Configurations.GENDUSTRY_CONFIG.allowQueenImprinting, "gendustry"),
    GendustryQueenMateGenome(true, () -> Configurations.GENDUSTRY_CONFIG.ensureQueenMateGenome, "gendustry"),
    GendustryApiaryBonk(true, () -> Configurations.GENDUSTRY_CONFIG.apiaryBonkEnable, "gendustry"),
    EnderIOAlloySmelter(true, () -> Configurations.ENDERIO_CONFIG.disableAlloySyntheticRecipes, "enderio"),
    EnderIOTankJEI(true, () -> Configurations.ENDERIO_CONFIG.disableTankJEIRecipes, "enderio"),
    ExtraUtils2Tweaks(true, () -> Configurations.EXTRA_CONFIG.extraUtils2TweaksEnable, "extrautils2"),
    ForestryTweaks(true, () -> Configurations.FORESTRY_CONFIG.disableSqueezerCapsuleRecipes || Configurations.FORESTRY_CONFIG.disableInvalidFermenterRecipes, "forestry"),
    ForestryRainTank(true, () -> Configurations.FORESTRY_CONFIG.buffRainTank, "forestry"),
    ForestryTubeSpeed(true, () -> Configurations.FORESTRY_CONFIG.buffTubeSpeed, "forestry"),
    IC2UuIndex(true, () -> Configurations.IC2_CONFIG.disableUuIndex, "ic2"),
    IEManualRecipes(true, () -> Configurations.IE_CONFIG.disableManualCraftingRecipes, "immersiveengineering"),
    SmelteryIO(true, () -> Configurations.SMELTERYIO_CONFIG.disableJEICategories || Configurations.SMELTERYIO_CONFIG.fixBlockProperties, "mctsmelteryio"),
    MekanismTweaks(true, () -> Configurations.MEKANISM_CONFIG.disableLogRecipes || Configurations.MEKANISM_CONFIG.fixNBTCrash, "mekanism"),
    TConCastingCooldown(true, () -> Configurations.TCONSTRUCT_CONFIG.fasterCastingCooldown, "tconstruct"),
    ThermalExpansionTweaks(true, () -> Configurations.THERMAL_EXPANSION_CONFIG.thermalExpansionTweaksEnable, "thermalexpansion"),
    ThermalFoundationTools(true, () -> Configurations.THERMAL_FOUNDATION_CONFIG.removeThermalTools, "thermalfoundation");

    final String mixinName;
    final String[] modId;
    final boolean needAllLoad;
    final Supplier<Boolean> otherConfig;

    Mixins(String mixinName, boolean needAllLoad, Supplier<Boolean> config, String... modId) {
        this.mixinName = mixinName;
        this.otherConfig = config;
        this.needAllLoad = needAllLoad;
        this.modId = modId;
    }

    Mixins(boolean needAllLoad, Supplier<Boolean> config, String... modId) {
        this.mixinName = this.name();
        this.otherConfig = config;
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
