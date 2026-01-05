package com.xinyihl.whimcraft.common.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public final class ItemStackSerde {

    public static String toBase64(ItemStack stack) throws Exception {
        NBTTagCompound tag = new NBTTagCompound();
        stack.writeToNBT(tag);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CompressedStreamTools.writeCompressed(tag, baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static ItemStack fromBase64(String base64) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        NBTTagCompound tag = CompressedStreamTools.readCompressed(bais);
        return new ItemStack(tag);
    }
}
