package com.xinyihl.whimcraft.common.title;

import com.warmthdawn.mod.gugu_utils.modularmachenary.MMCompoments;
import com.warmthdawn.mod.gugu_utils.modularmachenary.components.GenericMachineCompoment;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.basic.IGeneratable;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.title.base.TitleMEAspectBus;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TitleMEAspectOutputBus extends TitleMEAspectBus implements IGeneratable<RequirementAspect.RT> {

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(IB.itemMEAspectOutputBus);
    }

    @Nullable
    @Override
    public GenericMachineCompoment<RequirementAspect.RT> provideComponent() {
        return new GenericMachineCompoment<>(this, (ComponentType) MMCompoments.COMPONENT_ASPECT);
    }

    @Override
    public boolean generate(RequirementAspect.RT rt, boolean b) {
        if (!this.getProxy().isPowered() && !this.getProxy().isActive()) {
            rt.setError("ME机械源质输出总线未连接ME网络");
            return false;
        }
        int canAdd = addAspectToME(rt.getAspect(), rt.getAmount(), b);
        rt.setAmount(rt.getAmount() - canAdd);
        return true;
    }
}
