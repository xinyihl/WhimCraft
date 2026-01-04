package com.xinyihl.whimcraft.common.tile;

import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.tile.base.TileMEAspectBusMMCE;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.tile.machinecomponent.MachineComponentAspectProvider;
import net.minecraft.item.ItemStack;

public class TileMEAspectInputBusMMCE extends TileMEAspectBusMMCE {
    @Override
    public MachineComponentAspectProvider provideComponent() {
        return new MachineComponentAspectProvider(new MYAspectProviderCopy(this), IOType.INPUT);
    }

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(IB.itemMEAspectInputBusMMCE);
    }
}
