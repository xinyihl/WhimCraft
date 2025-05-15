package com.xinyihl.whimcraft.common.mixins.mmce;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.SecurityPermissions;
import appeng.api.features.ILocatable;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.features.IWirelessTermRegistry;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.security.ISecurityGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.core.localization.PlayerMessages;
import appeng.helpers.WirelessTerminalGuiObject;
import appeng.items.tools.powered.ToolWirelessTerminal;
import appeng.me.helpers.PlayerSource;
import appeng.tile.misc.TileSecurityStation;
import appeng.util.item.AEItemStack;
import baubles.api.BaublesApi;
import com.xinyihl.whimcraft.Configurations;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.network.PktAssemblyReport;
import hellfirepvp.modularmachinery.common.util.ItemUtils;
import ink.ikx.mmce.common.assembly.MachineAssembly;
import ink.ikx.mmce.common.utils.StructureIngredient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(value = MachineAssembly.class, remap = false)
public abstract class MachineAssemblyMixin {
    @Shadow
    @Final
    private EntityPlayer player;

    @Shadow
    private StructureIngredient ingredient;

    @Shadow
    public static List<StructureIngredient.FluidIngredient> buildFluidIngredients(List<ItemStack> inventory, List<StructureIngredient.FluidIngredient> fluidIngredients) {
        return null;
    }

    @Shadow
    public static void searchAndRemoveContainFluid(final List<ItemStack> inventory, final List<StructureIngredient.FluidIngredient> fluidIngredients) {
    }

    @Shadow
    private static List<List<ItemStack>> getItemStackIngList(final List<StructureIngredient.ItemIngredient> itemIngredientList) {
        return Collections.emptyList();
    }

    @Shadow
    private static List<List<FluidStack>> getFluidStackIngList(final List<StructureIngredient.FluidIngredient> fluidIngredientList) {
        return Collections.emptyList();
    }

    @Unique
    private static void whimCraft$searchAndRemoveContainItem(EntityPlayer player, List<ItemStack> inventory, List<StructureIngredient.ItemIngredient> itemIngredients) {
        Iterator<StructureIngredient.ItemIngredient> itemIngredientIter = itemIngredients.iterator();
        while (itemIngredientIter.hasNext()) {
            StructureIngredient.ItemIngredient ingredient = itemIngredientIter.next();
            for (Tuple<ItemStack, IBlockState> tuple : ingredient.ingredientList()) {
                ItemStack required = tuple.getFirst();
                if (required.isEmpty() || whimCraft$consumeItem(player, false, required, inventory)) {
                    itemIngredientIter.remove();
                    break;
                }
            }
        }
    }

    @Unique
    private static boolean whimCraft$consumeItem(EntityPlayer player, boolean consume, ItemStack required, List<ItemStack> inventory) {
        for (ItemStack invStack : inventory) {
            if (ItemUtils.matchStacks(invStack, required)) {
                invStack.shrink(required.getCount());
                return true;
            }
        }
        if (!Configurations.MMCE_CONFIG.assemblyAESupport) return false;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack item = player.inventory.getStackInSlot(i);
            if (item.getItem() instanceof ToolWirelessTerminal) {
                return whimCraft$takeItemFromWirelessTerminal(consume, item, player, required, new BlockPos(i, 0, Integer.MIN_VALUE));
            }
        }
        if (Loader.isModLoaded("baubles")) {
            return whimCraft$readBaubles(player, consume, required);
        }
        return false;
    }

    @Unique
    @Optional.Method(modid = "baubles")
    private static boolean whimCraft$readBaubles(EntityPlayer player, boolean consume, ItemStack required) {
        for (int i = 0; i < BaublesApi.getBaublesHandler(player).getSlots(); i++) {
            ItemStack item = BaublesApi.getBaublesHandler(player).getStackInSlot(i);
            if (item.getItem() instanceof ToolWirelessTerminal) {
                return whimCraft$takeItemFromWirelessTerminal(consume, item, player, required, new BlockPos(i, 1, Integer.MIN_VALUE));
            }
        }
        return false;
    }

    @Unique
    private static boolean whimCraft$takeItemFromWirelessTerminal(boolean consume, ItemStack item, EntityPlayer player, ItemStack required, BlockPos pos) {
        ToolWirelessTerminal wt = (ToolWirelessTerminal) item.getItem();
        IWirelessTermRegistry registry = AEApi.instance().registries().wireless();
        if (!registry.isWirelessTerminal(item)) {
            player.sendMessage(PlayerMessages.DeviceNotWirelessTerminal.get());
            return false;
        }
        IWirelessTermHandler handler = registry.getWirelessTerminalHandler(item);
        String unparsedKey = handler.getEncryptionKey(item);
        if (unparsedKey.isEmpty()) {
            player.sendMessage(PlayerMessages.DeviceNotLinked.get());
            return false;
        }
        long parsedKey = Long.parseLong(unparsedKey);
        ILocatable securityStation = AEApi.instance().registries().locatable().getLocatableBy(parsedKey);
        if (securityStation instanceof TileSecurityStation) {
            TileSecurityStation t = (TileSecurityStation) securityStation;
            if (!handler.hasPower(player, 1000F, item)) {
                player.sendMessage(PlayerMessages.DeviceNotPowered.get());
                return false;
            }
            WirelessTerminalGuiObject obj = new WirelessTerminalGuiObject(wt, item, player, player.world, pos.getX(), pos.getY(), pos.getZ());
            if (!obj.rangeCheck()) {
                player.sendMessage(PlayerMessages.OutOfRange.get());
            } else {
                IGridNode gridNode = obj.getActionableNode();
                IGrid grid = gridNode.getGrid();
                if (whimCraft$securityCheck(player, grid)) {
                    IStorageGrid storageGrid = grid.getCache(IStorageGrid.class);
                    IActionSource source = new PlayerSource(player, t);
                    IMEMonitor<IAEItemStack> monitor = storageGrid.getInventory(AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class));
                    IAEItemStack canExtract = monitor.extractItems(AEItemStack.fromItemStack(required).setStackSize(1), Actionable.SIMULATE, source);
                    if (canExtract != null && canExtract.getStackSize() == 1) {
                        if (consume) {
                            monitor.extractItems(canExtract, Actionable.MODULATE, source);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Unique
    private static boolean whimCraft$securityCheck(final EntityPlayer player, IGrid gridNode) {
        final ISecurityGrid sg = gridNode.getCache(ISecurityGrid.class);
        return sg.hasPermission(player, SecurityPermissions.EXTRACT);
    }

    /**
     * @author xin_yi_hl
     * @reason 为什么这么喜欢 static
     */
    @Overwrite
    public static boolean checkAllItems(EntityPlayer player, StructureIngredient ingredient) {
        List<ItemStack> inventory = player.inventory.mainInventory.stream().map(ItemStack::copy).collect(Collectors.toList());
        List<StructureIngredient.ItemIngredient> itemIngredientList = ingredient.itemIngredient();
        List<StructureIngredient.FluidIngredient> fluidIngredientList = ingredient.fluidIngredient();
        whimCraft$searchAndRemoveContainItem(player, inventory, itemIngredientList);
        searchAndRemoveContainFluid(inventory, fluidIngredientList);
        if (itemIngredientList.isEmpty() && fluidIngredientList.isEmpty()) {
            return true;
        } else {
            List<List<ItemStack>> itemStackIngList = getItemStackIngList(itemIngredientList);
            List<List<FluidStack>> fluidStackIngList = getFluidStackIngList(fluidIngredientList);
            PktAssemblyReport pkt = new PktAssemblyReport(itemStackIngList, fluidStackIngList);
            if (player instanceof EntityPlayerMP) {
                ModularMachinery.NET_CHANNEL.sendTo(pkt, (EntityPlayerMP) player);
            }
            return false;
        }
    }

    /**
     * @author xin_yi_hl
     * @reason 为什么这么喜欢 static
     */
    @Overwrite
    public void buildIngredients(boolean consumeInventory) {
        List<ItemStack> inventory = this.player.inventory.mainInventory;
        if (!consumeInventory) {
            inventory = inventory.stream().map(ItemStack::copy).collect(Collectors.toList());
        }
        List<StructureIngredient.ItemIngredient> newItemIngredients = whimCraft$buildItemIngredients(inventory, this.ingredient.itemIngredient());
        List<StructureIngredient.FluidIngredient> newFluidIngredients = buildFluidIngredients(inventory, this.ingredient.fluidIngredient());
        this.ingredient = new StructureIngredient(newItemIngredients, newFluidIngredients);
    }

    @Redirect(
            method = "assemblyItemBlocks",
            at = @At(
                    value = "INVOKE",
                    target = "Link/ikx/mmce/common/assembly/MachineAssembly;consumeInventoryItem(Lnet/minecraft/item/ItemStack;Ljava/util/List;)Z"
            )
    )
    private boolean injected(ItemStack required, List<ItemStack> inventory) {
        return whimCraft$consumeItem(player, true, required, inventory);
    }

    @Unique
    public List<StructureIngredient.ItemIngredient> whimCraft$buildItemIngredients(List<ItemStack> inventory, List<StructureIngredient.ItemIngredient> itemIngredients) {
        List<StructureIngredient.ItemIngredient> result = new ArrayList<>();
        Iterator<StructureIngredient.ItemIngredient> iterator = itemIngredients.iterator();
        while (iterator.hasNext()) {
            StructureIngredient.ItemIngredient ingredient = iterator.next();
            for (Tuple<ItemStack, IBlockState> tuple : ingredient.ingredientList()) {
                ItemStack required = tuple.getFirst();
                if (whimCraft$consumeItem(player, false, required, inventory)) {
                    result.add(new StructureIngredient.ItemIngredient(ingredient.pos(), Collections.singletonList(tuple), ingredient.nbt()));
                    iterator.remove();
                    break;
                }
            }
        }
        return result;
    }
}
