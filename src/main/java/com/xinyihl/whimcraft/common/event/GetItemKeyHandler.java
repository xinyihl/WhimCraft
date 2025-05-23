package com.xinyihl.whimcraft.common.event;

import com.xinyihl.whimcraft.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class GetItemKeyHandler {

    public static final KeyBinding KEY_GET_ITEM_ID = new KeyBinding("key.whimcraft.getItemId", Keyboard.KEY_K, "key.whimcraft.desc");
    public static final KeyBinding KEY_GET_ITEM_ID_LIST = new KeyBinding("key.whimcraft.getItemIdList", Keyboard.KEY_L, "key.whimcraft.desc");
    public static final KeyBinding KEY_GET_ITEM_ID_LIST_OUT = new KeyBinding("key.whimcraft.getItemIdListOut", Keyboard.KEY_J, "key.whimcraft.desc");
    private static final List<String> itemList = new ArrayList<>();
    private static boolean keyDown1 = false;
    private static boolean keyDown2 = false;

    public GetItemKeyHandler() {
        ClientRegistry.registerKeyBinding(KEY_GET_ITEM_ID);
        ClientRegistry.registerKeyBinding(KEY_GET_ITEM_ID_LIST);
        ClientRegistry.registerKeyBinding(KEY_GET_ITEM_ID_LIST_OUT);
    }

    public static void setSysClipboardText(String writeMe) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(writeMe), null);
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
