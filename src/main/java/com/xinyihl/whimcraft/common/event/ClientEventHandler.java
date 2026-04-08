package com.xinyihl.whimcraft.common.event;

import appeng.container.implementations.ContainerMEMonitorable;
import appeng.util.IConfigManagerHost;
import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.mixins.appliedenergistics2.accessor.GuiMEMonitorableAccessor;
import com.xinyihl.whimcraft.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler {

    private static final List<String> itemList = new ArrayList<>();
    public static KeyBinding KEY_FOUND_ITEM_AE;
    public static KeyBinding KEY_GET_ITEM_ID;
    public static KeyBinding KEY_GET_ITEM_ID_LIST;
    public static KeyBinding KEY_GET_ITEM_ID_LIST_OUT;
    public static KeyBinding KEY_GET_ITEM_DISPLAY_NAME;
    public static KeyBinding KEY_GET_ITEM_ID_WITH_DISPLAY_NAME;
    public static KeyBinding KEY_GET_INVENTORY_ITEM_ID_WITH_DISPLAY_NAME;
    private static boolean keyDown1 = false;
    private static boolean keyDown2 = false;
    private static boolean keyDown3 = false;
    private static boolean keyDown4 = false;
    private static boolean keyDown5 = false;

    public ClientEventHandler() {
        KEY_GET_ITEM_ID = new KeyBinding("key.whimcraft.getItemId", Keyboard.KEY_K, "key.whimcraft.desc");
        ClientRegistry.registerKeyBinding(KEY_GET_ITEM_ID);
        KEY_GET_ITEM_ID_LIST = new KeyBinding("key.whimcraft.getItemIdList", Keyboard.KEY_L, "key.whimcraft.desc");
        ClientRegistry.registerKeyBinding(KEY_GET_ITEM_ID_LIST);
        KEY_GET_ITEM_ID_LIST_OUT = new KeyBinding("key.whimcraft.getItemIdListOut", Keyboard.KEY_J, "key.whimcraft.desc");
        ClientRegistry.registerKeyBinding(KEY_GET_ITEM_ID_LIST_OUT);
        KEY_GET_ITEM_DISPLAY_NAME = new KeyBinding("key.whimcraft.getItemDisplayName", Keyboard.KEY_V, "key.whimcraft.desc");
        ClientRegistry.registerKeyBinding(KEY_GET_ITEM_DISPLAY_NAME);
        KEY_GET_ITEM_ID_WITH_DISPLAY_NAME = new KeyBinding("key.whimcraft.getItemIdWithDisplayName", Keyboard.KEY_N, "key.whimcraft.desc");
        ClientRegistry.registerKeyBinding(KEY_GET_ITEM_ID_WITH_DISPLAY_NAME);
        KEY_GET_INVENTORY_ITEM_ID_WITH_DISPLAY_NAME = new KeyBinding("key.whimcraft.getInventoryItemIdWithDisplayName", Keyboard.KEY_M, "key.whimcraft.desc");
        ClientRegistry.registerKeyBinding(KEY_GET_INVENTORY_ITEM_ID_WITH_DISPLAY_NAME);
        if (Configurations.AEMOD_CONFIG.searchInGui) {
            KEY_FOUND_ITEM_AE = new KeyBinding("key.whimcraft.foundItemAe", Keyboard.KEY_F, "key.whimcraft.desc");
            ClientRegistry.registerKeyBinding(KEY_FOUND_ITEM_AE);
        }
    }

    public static void setSysClipboardText(String writeMe) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(writeMe), null);
    }

    private static String getDisplayName(ItemStack itemStack) {
        String displayName = itemStack.getDisplayName();
        String formattedName = TextFormatting.getTextWithoutFormattingCodes(displayName);
        return formattedName == null ? displayName : formattedName;
    }

    private static String getItemIdWithDisplayName(ItemStack itemStack) {
        return Utils.getItemId(itemStack) + " | " + getDisplayName(itemStack);
    }

    private static List<String> getInventoryItemIdWithDisplayNames(EntityPlayer player) {
        List<String> itemInfos = new ArrayList<>();
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (!itemStack.isEmpty()) {
                itemInfos.add(getItemIdWithDisplayName(itemStack));
            }
        }
        return itemInfos;
    }

    @SubscribeEvent
    public void onRenderTooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();

        if (player == null) return;

        if (keyDown1) {
            keyDown1 = false;
            String item = Utils.getItemId(itemStack);
            player.sendMessage(new TextComponentString("Item: " + item));
            setSysClipboardText(item);
        }

        if (keyDown2) {
            keyDown2 = false;
            String item = Utils.getItemId(itemStack);
            itemList.add(item);
            player.sendMessage(new TextComponentString("ItemList Add: " + item));
        }

        if (keyDown4) {
            keyDown4 = false;
            String displayName = getDisplayName(itemStack);
            player.sendMessage(new TextComponentString("Item Name: " + displayName));
            setSysClipboardText(displayName);
        }

        if (keyDown5) {
            keyDown5 = false;
            String itemInfo = getItemIdWithDisplayName(itemStack);
            player.sendMessage(new TextComponentString("Item Info: " + itemInfo));
            setSysClipboardText(itemInfo);
        }
    }

    @SubscribeEvent
    @Optional.Method(modid = "appliedenergistics2")
    public void jei(ItemTooltipEvent event) {
        if (!Configurations.AEMOD_CONFIG.searchInGui) return;
        EntityPlayer player = event.getEntityPlayer();
        if (player == null) return;
        if (keyDown3) {
            keyDown3 = false;
            Container container = player.openContainer;
            if (container instanceof ContainerMEMonitorable) {
                ItemStack itemStack = event.getItemStack();
                String name = itemStack.getDisplayName();
                IConfigManagerHost gui = ((ContainerMEMonitorable) container).getGui();
                if (gui instanceof GuiMEMonitorableAccessor) {
                    GuiMEMonitorableAccessor accessor = (GuiMEMonitorableAccessor) gui;
                    accessor.getSearchField().setText(name);
                    accessor.getSearchField().selectAll();
                    accessor.getRepo().setSearchString(name);
                    accessor.invokeSetScrollBar();
                }
            }
        }
    }

    @SubscribeEvent
    public void onKeyPressed(GuiScreenEvent.KeyboardInputEvent.Post event) {
        //char typedChar = Keyboard.getEventCharacter();
        int keyCode = Keyboard.getEventKey();
        boolean isPressed = Keyboard.getEventKeyState();
        if (keyCode != 0 /*&& typedChar >= 32*/) {
            if (isPressed) { // 按下
                if (keyCode == KEY_GET_ITEM_ID.getKeyCode()) {
                    keyDown1 = true;
                }
                if (keyCode == KEY_GET_ITEM_ID_LIST.getKeyCode()) {
                    keyDown2 = true;
                }
                if (Configurations.AEMOD_CONFIG.searchInGui && keyCode == KEY_FOUND_ITEM_AE.getKeyCode()) {
                    keyDown3 = true;
                }
                if (keyCode == KEY_GET_ITEM_DISPLAY_NAME.getKeyCode()) {
                    keyDown4 = true;
                }
                if (keyCode == KEY_GET_ITEM_ID_WITH_DISPLAY_NAME.getKeyCode()) {
                    keyDown5 = true;
                }
                if (keyCode == KEY_GET_INVENTORY_ITEM_ID_WITH_DISPLAY_NAME.getKeyCode()) {
                    EntityPlayerSP player = Minecraft.getMinecraft().player;
                    if (player != null) {
                        List<String> itemInfos = getInventoryItemIdWithDisplayNames(player);
                        if (itemInfos.isEmpty()) {
                            player.sendMessage(new TextComponentString("Inventory: empty"));
                            setSysClipboardText("");
                        } else {
                            setSysClipboardText(String.join(System.lineSeparator(), itemInfos));
                            player.sendMessage(new TextComponentString("Inventory copied: " + itemInfos.size() + " items"));
                        }
                    }
                }
                if (keyCode == KEY_GET_ITEM_ID_LIST_OUT.getKeyCode()) {
                    EntityPlayerSP player = Minecraft.getMinecraft().player;
                    if (player != null) {
                        player.sendMessage(new TextComponentString("ItemList: " + itemList));
                        setSysClipboardText(itemList.toString());
                        itemList.clear();
                    }
                }
            }
            if (!isPressed) { // 抬起
                if (keyCode == KEY_GET_ITEM_ID.getKeyCode()) {
                    keyDown1 = false;
                }
                if (keyCode == KEY_GET_ITEM_ID_LIST.getKeyCode()) {
                    keyDown2 = false;
                }
                if (Configurations.AEMOD_CONFIG.searchInGui && keyCode == KEY_FOUND_ITEM_AE.getKeyCode()) {
                    keyDown3 = false;
                }
                if (keyCode == KEY_GET_ITEM_DISPLAY_NAME.getKeyCode()) {
                    keyDown4 = false;
                }
                if (keyCode == KEY_GET_ITEM_ID_WITH_DISPLAY_NAME.getKeyCode()) {
                    keyDown5 = false;
                }
            }
        }
    }

/*
    @SubscribeEvent
    public void onGuiKeyboardEvent(GuiScreenEvent.KeyboardInputEvent.Post event) {
        if (handleKeyEvent(event)) {
            event.setCanceled(true);
        }
    }

    private boolean handleKeyEvent(GuiScreenEvent.KeyboardInputEvent.Post event) {
        char typedChar = Keyboard.getEventCharacter();
        int eventKey = Keyboard.getEventKey();
        return eventKey != 0 && typedChar >= 32 && Keyboard.getEventKeyState() && handleKeyDown(event, eventKey);
    }

    private boolean handleKeyDown(GuiScreenEvent.KeyboardInputEvent.Post event, int eventKey) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        GuiScreen gui = event.getGui();
        if (eventKey == KEY_GET_ITEM_ID.getKeyCode()) {
            if (player != null && gui instanceof GuiContainer) {
                GuiContainer guiContainer = (GuiContainer) gui;
                ItemStack hoveredStack = guiContainer.getSlotUnderMouse() != null ? guiContainer.getSlotUnderMouse().getStack() : ItemStack.EMPTY;
                if (!hoveredStack.isEmpty()) {
                    String item = getItemId(hoveredStack);
                    player.sendMessage(new TextComponentString("Item: " + item));
                    setSysClipboardText(item);
                    return true;
                }
            }
        }
        if (eventKey == KEY_GET_ITEM_ID_LIST.getKeyCode()) {
            if (player != null && gui instanceof GuiContainer) {
                GuiContainer guiContainer = (GuiContainer) gui;
                ItemStack hoveredStack = guiContainer.getSlotUnderMouse() != null ? guiContainer.getSlotUnderMouse().getStack() : ItemStack.EMPTY;
                if (!hoveredStack.isEmpty()) {
                    String item = getItemId(hoveredStack);
                    itemList.add(item);
                    player.sendMessage(new TextComponentString("ItemList Add: " + item));
                    return true;
                } else {
                    player.sendMessage(new TextComponentString("ItemList: " + itemList));
                    setSysClipboardText(itemList.toString());
                    itemList.clear();
                }
            }
        }
        return false;
    }
*/
}
