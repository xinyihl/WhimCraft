package com.xinyihl.whimcraft.common.utils;

import com.xinyihl.whimcraft.Configurations;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.swing.*;
import java.math.BigInteger;
import java.util.Objects;

public final class Utils {

    public static void checkAuthType() {
        String type = Minecraft.getMinecraft().getVersionType();
        if (!Configurations.GENERAL_CONFIG.loginCheckName.equals(type) && Configurations.GENERAL_CONFIG.loginCheckEnable) {
            int i = JOptionPane.showOptionDialog(null, "你需要使用皮肤站登录才能进入服务器", "客户端未登录", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"退出", "继续"}, "退出");
            if (i == 0) {
                FMLCommonHandler.instance().exitJava(0, true);
            }
            if (i == 1) {
                JOptionPane.showOptionDialog(null, "继续个蛋不准继续，继续也进不了服务器", "退出游戏", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"退出"}, "退出");
                FMLCommonHandler.instance().exitJava(0, true);
            }
        }
    }

    public static String getItemId(ItemStack itemStack) {
        int meta = itemStack.getMetadata();
        String text = "<" + Objects.requireNonNull(itemStack.getItem().getRegistryName()) + (meta == 0 ? "" : ":" + meta) + ">";
        if (itemStack.serializeNBT().hasKey("tag")) {
            String nbt = itemStack.serializeNBT().getTag("tag").toString();
            if (!nbt.isEmpty())
                text += ".withTag(" + nbt + ")";
        }
        return text;
    }

    public static String formatBytes(String bytesStr) {
        try {
            BigInteger bytes = new BigInteger(bytesStr);
            if (bytes.compareTo(BigInteger.ZERO) == 0) {
                return "0B";
            }
            String[] units = {"B", "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q"};
            BigInteger k = BigInteger.valueOf(1024);
            int unitIndex = 0;
            BigInteger value = bytes;
            while (value.compareTo(k) >= 0 && unitIndex < units.length - 1) {
                value = value.divide(k);
                unitIndex++;
            }
            return value + units[unitIndex];
        } catch (Exception e) {
            return "0B";
        }
    }
}
