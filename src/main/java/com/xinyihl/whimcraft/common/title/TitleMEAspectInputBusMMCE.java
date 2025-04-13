package com.xinyihl.whimcraft.common.title;

import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.title.base.TitleMEAspectBusMMCE;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.tile.machinecomponent.MachineComponentAspectProvider;
import net.minecraft.item.ItemStack;

public class TitleMEAspectInputBusMMCE extends TitleMEAspectBusMMCE {
    @Override
    public MachineComponentAspectProvider provideComponent() {
        return new MachineComponentAspectProvider(new MYAspectProviderCopy(this), IOType.INPUT);
    }

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(IB.itemMEAspectInputBusMMCE);
    }
}
