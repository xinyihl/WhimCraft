package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.container.implementations.ContainerPatternEncoder;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.core.AELog;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketJEIRecipe;
import appeng.core.sync.packets.PacketValueConfig;
import appeng.helpers.ItemStackHelper;
import appeng.util.Platform;
import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.integration.crafttweaker.PatternTransfer;
import com.xinyihl.whimcraft.common.items.Order;
import hellfirepvp.modularmachinery.common.item.ItemBlockController;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(targets = "appeng.integration.modules.jei.RecipeTransferHandler", remap = false)
public abstract class RecipeTransferHandlerMixin {
    @Inject(
            method = "transferRecipe",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public <T extends Container> void injected(T container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer, CallbackInfoReturnable<IRecipeTransferError> cir) {
        String recipeType = recipeLayout.getRecipeCategory().getUid();
        if (doTransfer) {
            if (PatternTransfer.listInput.containsKey(recipeType) || PatternTransfer.listOutput.containsKey(recipeType)) {
                if (container instanceof ContainerPatternEncoder) {
                    try {
                        if (((ContainerPatternEncoder) container).isCraftingMode()) {
                            NetworkHandler.instance().sendToServer(new PacketValueConfig("PatternTerminal.CraftMode", "0"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = recipeLayout.getItemStacks().getGuiIngredients();
                NBTTagCompound recipe = new NBTTagCompound();
                NBTTagList outputs = new NBTTagList();
                AtomicInteger slotIndex = new AtomicInteger();

                PatternTransfer.listInput.forEach((key, value) -> {
                    if (key.equals(recipeType)) {
                        for (Slot slot : container.inventorySlots) {
                            if ((slot instanceof SlotCraftingMatrix || slot instanceof SlotFakeCraftingMatrix) && slot.getSlotIndex() == slotIndex.get()) {
                                NBTTagList tags = new NBTTagList();
                                NBTTagCompound tag = ItemStackHelper.stackToNBT(value);
                                tags.appendTag(tag);
                                recipe.setTag("#" + slot.getSlotIndex(), tags);
                                break;
                            }
                        }
                        slotIndex.incrementAndGet();
                    }
                });

                PatternTransfer.listOutput.forEach((key, value) -> {
                    if (key.equals(recipeType)) {
                        NBTTagCompound tag = ItemStackHelper.stackToNBT(value);
                        outputs.appendTag(tag);
                    }
                });

                for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> ingredientEntry : ingredients.entrySet()) {
                    IGuiIngredient<ItemStack> ingredient = ingredientEntry.getValue();
                    if (!ingredient.isInput()) {
                        ItemStack output = ingredient.getDisplayedIngredient();
                        if (output != null) {
                            NBTTagCompound tag = ItemStackHelper.stackToNBT(output);
                            outputs.appendTag(tag);
                        }
                    } else {
                        for (Slot slot : container.inventorySlots) {
                            if ((slot instanceof SlotCraftingMatrix || slot instanceof SlotFakeCraftingMatrix) && slot.getSlotIndex() == slotIndex.get()) {
                                NBTTagList tags = new NBTTagList();
                                List<ItemStack> list = new ArrayList<>();
                                ItemStack displayed = ingredient.getDisplayedIngredient();
                                if (displayed != null && !displayed.isEmpty()) {
                                    list.add(displayed);
                                }

                                for (ItemStack stack : ingredient.getAllIngredients()) {
                                    if (stack != null) {
                                        if (Platform.isRecipePrioritized(stack)) {
                                            list.add(0, stack);
                                        } else {
                                            list.add(stack);
                                        }
                                    }
                                }

                                for (ItemStack is : list) {
                                    NBTTagCompound tag = ItemStackHelper.stackToNBT(is);
                                    tags.appendTag(tag);
                                }

                                recipe.setTag("#" + slot.getSlotIndex(), tags);
                                break;
                            }
                        }

                        slotIndex.incrementAndGet();
                    }
                }

                recipe.setTag("outputs", outputs);

                try {
                    NetworkHandler.instance().sendToServer(new PacketJEIRecipe(recipe));
                } catch (IOException e) {
                    AELog.debug(e);
                }

                cir.setReturnValue(null);
            }
            if (recipeType.equals("modularmachinery.preview") && Configurations.GENERAL_CONFIG.JeiTransferOrderEnable) {
                if (container instanceof ContainerPatternEncoder) {
                    try {
                        if (((ContainerPatternEncoder) container).isCraftingMode()) {
                            NetworkHandler.instance().sendToServer(new PacketValueConfig("PatternTerminal.CraftMode", "0"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = recipeLayout.getItemStacks().getGuiIngredients();
                NBTTagCompound recipe = new NBTTagCompound();
                NBTTagList outputs = new NBTTagList();
                int slotIndex = 0;
                for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> ingredientEntry : ingredients.entrySet()) {
                    IGuiIngredient<ItemStack> ingredient = ingredientEntry.getValue();
                    if (!ingredient.isInput()) {
                        ItemStack output = ingredient.getDisplayedIngredient();
                        if (output != null) {
                            if (output.getItem() instanceof ItemBlockController) {
                                ItemStack order = new ItemStack(IB.itemOrder);
                                Order.setMarkedItem(order, output);
                                NBTTagCompound tag = ItemStackHelper.stackToNBT(order);
                                outputs.appendTag(tag);
                                for (Slot slot : container.inventorySlots) {
                                    if ((slot instanceof SlotCraftingMatrix || slot instanceof SlotFakeCraftingMatrix) && slot.getSlotIndex() == slotIndex) {
                                        NBTTagList tags = new NBTTagList();
                                        List<ItemStack> list = new ArrayList<>();
                                        if (!output.isEmpty()) {
                                            list.add(output);
                                        }
                                        for (ItemStack stack : ingredient.getAllIngredients()) {
                                            if (stack != null) {
                                                if (Platform.isRecipePrioritized(stack)) {
                                                    list.add(0, stack);
                                                } else {
                                                    list.add(stack);
                                                }
                                            }
                                        }
                                        for (ItemStack is : list) {
                                            NBTTagCompound tag1 = ItemStackHelper.stackToNBT(is);
                                            tags.appendTag(tag1);
                                        }
                                        recipe.setTag("#" + slot.getSlotIndex(), tags);
                                        break;
                                    }
                                }
                                ++slotIndex;
                            } else {
                                NBTTagCompound tag = ItemStackHelper.stackToNBT(output);
                                outputs.appendTag(tag);
                            }
                        }
                    } else {
                        for (Slot slot : container.inventorySlots) {
                            if ((slot instanceof SlotCraftingMatrix || slot instanceof SlotFakeCraftingMatrix) && slot.getSlotIndex() == slotIndex) {
                                NBTTagList tags = new NBTTagList();
                                List<ItemStack> list = new ArrayList<>();
                                ItemStack displayed = ingredient.getDisplayedIngredient();
                                if (displayed != null && !displayed.isEmpty()) {
                                    list.add(displayed);
                                }
                                for (ItemStack stack : ingredient.getAllIngredients()) {
                                    if (stack != null) {
                                        if (Platform.isRecipePrioritized(stack)) {
                                            list.add(0, stack);
                                        } else {
                                            list.add(stack);
                                        }
                                    }
                                }
                                for (ItemStack is : list) {
                                    NBTTagCompound tag = ItemStackHelper.stackToNBT(is);
                                    tags.appendTag(tag);
                                }
                                recipe.setTag("#" + slot.getSlotIndex(), tags);
                                break;
                            }
                        }
                        ++slotIndex;
                    }
                }
                recipe.setTag("outputs", outputs);
                try {
                    NetworkHandler.instance().sendToServer(new PacketJEIRecipe(recipe));
                } catch (IOException e) {
                    AELog.debug(e);
                }
                cir.setReturnValue(null);
            }
        }
    }
}
