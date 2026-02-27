package com.xinyihl.whimcraft.common.integration.crafttweaker;

import com.mojang.authlib.GameProfile;
import com.xinyihl.whimcraft.common.items.cell.*;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBiome;
import crafttweaker.api.world.IWorld;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.aspects.Aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ZenRegister
@ZenClass("mods.whimcraft.WhimCraftUtils")
public class WhimCraftUtils {
    private static final GameProfile GAME_PROFILE = new GameProfile(UUID.fromString("7E1D8024-D2EF-4077-AD6F-636F16F43BB6"), "[WhimCraft]");
    public static Map<Integer, Short> generateBiomeAuraBase;
    private static Method getLootTable;

    /**
     * 获取实体掉落物
     *
     * @param ientity  目标实体
     * @param isPlayer 是否玩家击杀
     * @param luck     幸运(不是抢夺)
     */
    @ZenMethod
    public static IItemStack[] getDrops(IEntity ientity, boolean isPlayer, int luck) {
        Entity entity = CraftTweakerMC.getEntity(ientity);
        if (entity.world.isRemote || !(entity instanceof EntityLiving)) {
            return null;
        }
        EntityLiving entityliving = (EntityLiving) entity;
        WorldServer worldServer = (WorldServer) entityliving.world;
        if (getLootTable == null) {
            getLootTable = ObfuscationReflectionHelper.findMethod(EntityLiving.class, "getLootTable", ResourceLocation.class);
        }
        ResourceLocation lootTableLocation;
        try {
            lootTableLocation = (ResourceLocation) getLootTable.invoke(entityliving);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if (lootTableLocation == null) {
            return null;
        }
        LootTableManager lootTableManager = worldServer.getLootTableManager();
        LootTable lootTable = lootTableManager.getLootTableFromLocation(lootTableLocation);
        LootContext.Builder contextBuilder = new LootContext.Builder(worldServer).withLuck(luck);
        if (isPlayer) {
            EntityPlayer killer = FakePlayerFactory.get(worldServer, GAME_PROFILE);
            if (killer != null) {
                contextBuilder.withPlayer(killer);
            }
        }
        LootContext context = contextBuilder.build();
        List<ItemStack> drops = lootTable.generateLootForPools(entityliving.getRNG(), context);
        return CraftTweakerMC.getIItemStacks(drops.toArray(new ItemStack[0]));
    }


    /**
     * 获取实体掉落物
     *
     * @param iworld   世界
     * @param location 目标实体战利品表id
     * @param isPlayer 是否玩家击杀
     * @param luck     幸运(不是抢夺)
     */
    @ZenMethod
    public static IItemStack[] getDrops(IWorld iworld, String location, boolean isPlayer, int luck) {
        World world = CraftTweakerMC.getWorld(iworld);
        if (world.isRemote || !(world instanceof WorldServer)) {
            return null;
        }
        WorldServer worldServer = (WorldServer) world;
        ResourceLocation lootTableLocation = new ResourceLocation(location);
        LootTableManager lootTableManager = worldServer.getLootTableManager();
        LootTable lootTable = lootTableManager.getLootTableFromLocation(lootTableLocation);
        LootContext.Builder contextBuilder = new LootContext.Builder(worldServer).withLuck(luck);
        if (isPlayer) {
            EntityPlayer killer = FakePlayerFactory.get(worldServer, GAME_PROFILE);
            contextBuilder.withPlayer(killer);
        }
        LootContext context = contextBuilder.build();
        List<ItemStack> drops = lootTable.generateLootForPools(worldServer.rand, context);
        return CraftTweakerMC.getIItemStacks(drops.toArray(new ItemStack[0]));
    }

    /**
     * 设置群系神秘区块灵气基础值
     *
     * @param ibiome 要修改的群系
     * @param value  目标值
     */
    @ZenMethod
    public static void setBiomeAuraBase(IBiome ibiome, short value) {
        Biome biome = CraftTweakerMC.getBiome(ibiome);
        generateBiomeAuraBase.put(Biome.getIdForBiome(biome), value);
    }

    @ZenMethod
    public static void registerInfinityListItemCell(IItemStack[] recs) {
        if (recs == null || recs.length == 0) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListItemCell: recs is empty.");
            return;
        }
        List<ItemStack> records = new java.util.ArrayList<>();
        for (IItemStack rec : recs) {
            if (rec == null) {
                continue;
            }
            ItemStack mcStack = CraftTweakerMC.getItemStack(rec);
            if (mcStack.isEmpty() || mcStack.getItem() == Items.AIR) {
                continue;
            }
            records.add(mcStack.copy());
        }
        if (records.isEmpty()) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListItemCell: no valid entries.");
            return;
        }
        ItemStack cell = InfinityListItemCell.createWithRecords(records);
        if (cell.isEmpty()) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListItemCell: infinity list item cell is not available.");
            return;
        }
        InfinityListItemCellRegistry.registerJeiStack(cell);
    }

    @ZenMethod
    public static void registerInfinityListFluidCell(ILiquidStack[] recs) {
        if (recs == null || recs.length == 0) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListFluidCell: recs is empty.");
            return;
        }
        List<FluidStack> records = new java.util.ArrayList<>();
        for (ILiquidStack rec : recs) {
            if (rec == null) {
                continue;
            }
            FluidStack fluidStack = CraftTweakerMC.getLiquidStack(rec);
            if (fluidStack == null || fluidStack.getFluid() == null) {
                continue;
            }
            records.add(fluidStack.copy());
        }
        if (records.isEmpty()) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListFluidCell: no valid entries.");
            return;
        }
        ItemStack cell = InfinityListFluidCell.createWithRecords(records);
        if (cell.isEmpty()) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListFluidCell: infinity list fluid cell is not available.");
            return;
        }
        InfinityListItemCellRegistry.registerJeiStack(cell);
    }

    @ZenMethod
    @Optional.Method(modid = "mekeng")
    public static void registerInfinityListGasCell(String[] recs) {
        if (!Loader.isModLoaded("mekeng")) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListGasCell: mekeng is not loaded.");
            return;
        }
        if (recs == null || recs.length == 0) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListGasCell: recs is empty.");
            return;
        }
        List<GasStack> records = new java.util.ArrayList<>();
        for (String rec : recs) {
            if (rec == null || rec.isEmpty()) {
                continue;
            }
            Gas gas = GasRegistry.getGas(rec);
            if (gas == null) {
                continue;
            }
            records.add(new GasStack(gas, 1));
        }
        if (records.isEmpty()) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListGasCell: no valid gas entries.");
            return;
        }
        ItemStack cell = InfinityListGasCell.createWithRecords(records);
        if (cell.isEmpty()) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListGasCell: infinity list gas cell is not available.");
            return;
        }
        InfinityListItemCellRegistry.registerJeiStack(cell);
    }

    @ZenMethod
    @Optional.Method(modid = "thaumicenergistics")
    public static void registerInfinityListEssentiaCell(String[] recs) {
        if (!Loader.isModLoaded("thaumicenergistics")) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListEssentiaCell: thaumicenergistics is not loaded.");
            return;
        }
        if (recs == null || recs.length == 0) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListEssentiaCell: recs is empty.");
            return;
        }
        List<Aspect> records = new java.util.ArrayList<>();
        for (String rec : recs) {
            if (rec == null || rec.isEmpty()) {
                continue;
            }
            Aspect aspect = Aspect.getAspect(rec);
            if (aspect != null) {
                records.add(aspect);
            }
        }
        if (records.isEmpty()) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListEssentiaCell: no valid aspect entries.");
            return;
        }
        ItemStack cell = InfinityListEssentiaCell.createWithRecords(records);
        if (cell.isEmpty()) {
            CraftTweakerAPI.logWarning("[WhimCraft] registerInfinityListEssentiaCell: infinity list essentia cell is not available.");
            return;
        }
        InfinityListItemCellRegistry.registerJeiStack(cell);
    }
}
