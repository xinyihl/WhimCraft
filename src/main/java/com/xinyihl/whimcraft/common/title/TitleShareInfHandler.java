package com.xinyihl.whimcraft.common.title;

import com.xinyihl.whimcraft.common.api.IHasProbeInfo;
import github.kasuminova.mmce.common.tile.MEPatternProvider;
import github.kasuminova.mmce.common.util.InfItemFluidHandler;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.lib.ComponentTypesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public class TitleShareInfHandler extends TileColorableMachineComponent implements MachineComponentTile, IHasProbeInfo {
    private BlockPos bp;
    @Nullable
    @Override
    public MachineComponent<?> provideComponent() {
        if (bp != null) {
            TileEntity tileEntity = this.world.getTileEntity(bp);
            if (tileEntity instanceof MEPatternProvider){
                return new MachineComponent<InfItemFluidHandler>(IOType.INPUT) {
                    public ComponentType getComponentType() {
                        return ComponentTypesMM.COMPONENT_ITEM_FLUID_GAS;
                    }
                    public InfItemFluidHandler getContainerProvider() {
                        return ((MEPatternProvider) tileEntity).getInfHandler();
                    }
                };
            }
        }
        return null;
    }

    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        if(compound.hasKey("share_inf_handler_bp")){
            this.bp = BlockPos.fromLong(compound.getLong("share_inf_handler_bp"));
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        if (bp != null){
            compound.setLong("share_inf_handler_bp", bp.toLong());
        }
    }

    public void setBlockPos(EntityPlayer player, BlockPos blockPos){
        if (!(this.world.getTileEntity(blockPos) instanceof MEPatternProvider)){
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.titleshareinfhandler.error.noShare"), true);
        } else {
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.titleshareinfhandler.suc"), true);
            this.bp = blockPos;
        }
    }

    @Override
    public void addProbeInfo(Consumer<String> consumer, Function<String, String> loc) {
        if(bp == null){
            consumer.accept(loc.apply("blockshareinfhandler.offline"));
        }else {
            if (!(this.world.getTileEntity(bp) instanceof MEPatternProvider)) {
                consumer.accept(loc.apply("blockshareinfhandler.error"));
            }else {
                consumer.accept(loc.apply("blockshareinfhandler.online"));
                consumer.accept("Bop: " + bp.toString());
            }
        }
    }
}
