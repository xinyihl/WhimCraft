package com.xinyihl.whimcraft.common.storage;

import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.fluids.util.AEFluidStack;
import appeng.util.item.AEItemStack;
import com.mekeng.github.common.me.data.IAEGasStack;
import com.mekeng.github.common.me.data.impl.AEGasStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.integration.appeng.AEEssentiaStack;

import javax.annotation.Nullable;

public class StorageDataManager {
    @Nullable
    public static IAEStack<?> readExternalStack(String type, NBTTagCompound tag) {
        if (type == null || tag == null) {
            return null;
        }
        switch (type) {
            case Types.ITEM:
                return readItemStack(tag);
            case Types.FLUID:
                return readFluidStack(tag);
            case Types.GAS:
                return readGasStack(tag);
            case Types.ESSENTIA:
                return readEssentiaStack(tag);
            default:
                return null;
        }
    }

    public static boolean writeExternalStack(IAEStack<?> stack, NBTTagCompound itemTag) {
        if (stack == null || itemTag == null) {
            return false;
        }

        // item
        if (writeItemStack(stack, itemTag)) {
            return true;
        }

        // fluid
        if (writeFluidStack(stack, itemTag)) {
            return true;
        }

        // gas
        if (Loader.isModLoaded("mekeng")) {
            if (writeGasStack(stack, itemTag)) {
                return true;
            }
        }

        // essentia
        if (Loader.isModLoaded("thaumicenergistics")) {
            return writeEssentiaStack(stack, itemTag);
        }

        return false;
    }

    private static IAEStack<?> readItemStack(NBTTagCompound tag) {
        return AEItemStack.fromNBT(tag);
    }

    private static boolean writeItemStack(IAEStack<?> stack, NBTTagCompound itemTag) {
        if (!(stack instanceof IAEItemStack)) {
            return false;
        }
        NBTTagCompound payload = new NBTTagCompound();
        stack.writeToNBT(payload);
        itemTag.setString("type", Types.ITEM);
        itemTag.setTag("stack", payload);
        return true;
    }

    private static IAEStack<?> readFluidStack(NBTTagCompound tag) {
        return AEFluidStack.fromNBT(tag);
    }

    private static boolean writeFluidStack(IAEStack<?> stack, NBTTagCompound itemTag) {
        if (!(stack instanceof IAEFluidStack)) {
            return false;
        }
        NBTTagCompound payload = new NBTTagCompound();
        stack.writeToNBT(payload);
        itemTag.setString("type", Types.FLUID);
        itemTag.setTag("stack", payload);
        return true;
    }

    @Optional.Method(modid = "mekeng")
    private static IAEStack<?> readGasStack(NBTTagCompound tag) {
        return AEGasStack.of(tag);
    }

    @Optional.Method(modid = "mekeng")
    private static boolean writeGasStack(IAEStack<?> stack, NBTTagCompound itemTag) {
        if (!(stack instanceof IAEGasStack)) {
            return false;
        }
        NBTTagCompound payload = new NBTTagCompound();
        stack.writeToNBT(payload);
        itemTag.setString("type", Types.GAS);
        itemTag.setTag("stack", payload);
        return true;
    }

    @Optional.Method(modid = "thaumicenergistics")
    private static IAEStack<?> readEssentiaStack(NBTTagCompound tag) {
        return AEEssentiaStack.fromNBT(tag);
    }

    @Optional.Method(modid = "thaumicenergistics")
    private static boolean writeEssentiaStack(IAEStack<?> stack, NBTTagCompound itemTag) {
        if (!(stack instanceof IAEEssentiaStack)) {
            return false;
        }
        NBTTagCompound payload = new NBTTagCompound();
        stack.writeToNBT(payload);
        itemTag.setString("type", Types.ESSENTIA);
        itemTag.setTag("stack", payload);
        return true;
    }

    public static final class Types {
        public static final String ITEM = "item";
        public static final String FLUID = "fluid";
        public static final String GAS = "gas";
        public static final String ESSENTIA = "essentia";
    }
}
