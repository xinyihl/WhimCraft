package com.xinyihl.whimcraft.common.integration.crafttweaker;

import com.mojang.authlib.GameProfile;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@ZenRegister
@ZenClass("mods.whimcraft.WhimCraftUtils")
public class WhimCraftUtils {
    private static final GameProfile GAME_PROFILE = new GameProfile(UUID.fromString("7E1D8024-D2EF-4077-AD6F-636F16F43BB6"), "[WhimCraft]");
    private static Method getLootTable;

    /**
     * 获取实体掉落物
     *
     * @param ientity     目标实体
     * @param isPlayer    是否玩家击杀
     * @param luck        幸运(不是抢夺)
     */
    @ZenMethod
    public static IItemStack[] getDrops(IEntity ientity, boolean isPlayer, int luck) {
        Entity entity = CraftTweakerMC.getEntity(ientity);
        if (entity.world.isRemote || !(entity instanceof EntityLiving)){
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
        } catch (IllegalAccessException | InvocationTargetException e){
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
     * @param iworld      世界
     * @param location    目标实体战利品表id
     * @param isPlayer    是否玩家击杀
     * @param luck        幸运(不是抢夺)
     */
    @ZenMethod
    public static IItemStack[] getDrops(IWorld iworld, String location, boolean isPlayer, int luck) {
        World world = CraftTweakerMC.getWorld(iworld);
        if (world.isRemote || !(world instanceof WorldServer)){
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
}
