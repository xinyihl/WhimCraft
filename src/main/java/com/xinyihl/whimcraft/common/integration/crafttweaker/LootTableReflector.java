package com.xinyihl.whimcraft.common.integration.crafttweaker;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import java.lang.reflect.Method;

public class LootTableReflector {
    private static Method methodGetLootTable;

    static {
        try {
            methodGetLootTable = EntityLiving.class.getDeclaredMethod("func_184647_J"); // getLootTable
            methodGetLootTable.setAccessible(true);
        } catch (NoSuchMethodException e) {
            // ignore
        }
    }

    public static ResourceLocation getLootTable(EntityLiving entity) {
        if (methodGetLootTable == null || entity == null) {
            return null;
        }

        try {
            return (ResourceLocation) methodGetLootTable.invoke(entity);
        } catch (Exception e) {
            // ignore
            return null;
        }
    }
}