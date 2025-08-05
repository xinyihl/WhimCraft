package com.xinyihl.whimcraft.common.integration.crafttweaker;

import com.mojang.authlib.GameProfile;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
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
     * @param luck        幸运
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
            getLootTable = ReflectionHelper.findMethod(EntityLiving.class, "getLootTable", "func_184647_J");
        }
        ResourceLocation lootTableLocation;
        try {
            lootTableLocation = (ResourceLocation) getLootTable.invoke(entityliving);
        } catch (IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(e);
        }

        LootTableManager lootTableManager = worldServer.getLootTableManager();
        if (lootTableLocation == null) {
            return null;
        }
        LootTable lootTable = lootTableManager.getLootTableFromLocation(lootTableLocation);
        LootContext.Builder contextBuilder = new LootContext.Builder(worldServer)
                .withLootedEntity(entityliving)
                .withLuck(luck);
        DamageSource damageSource = DamageSource.GENERIC;
        if (isPlayer) {
            EntityPlayer killer = FakePlayerFactory.get(worldServer, GAME_PROFILE);
            if (killer != null) {
                contextBuilder.withPlayer(killer);
                damageSource = DamageSource.causePlayerDamage(killer);
            }
        }
        contextBuilder.withDamageSource(damageSource);
        LootContext context = contextBuilder.build();

        Map<Item, ItemStack> outMap = new HashMap<>();
        List<ItemStack> drops = lootTable.generateLootForPools(entityliving.getRNG(), context);

        for (ItemStack item : drops){
            if(outMap.containsKey(item.getItem())){
                ItemStack stack = outMap.get(item.getItem());
                if (stack.getCount() >= 16) {
                    continue;
                }
                stack.setCount(stack.getCount() + 1);
            } else {
                outMap.put(item.getItem(), item);
            }
        }

        return CraftTweakerMC.getIItemStacks(outMap.values().toArray(new ItemStack[0]));
    }
}
