package com.xinyihl.whimcraft.common.title;

import com.warmthdawn.mod.gugu_utils.modularmachenary.MMCompoments;
import com.warmthdawn.mod.gugu_utils.modularmachenary.components.GenericMachineCompoment;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.basic.IConsumable;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.basic.ICraftNotifier;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.title.base.TitleMEAspectBus;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aura.AuraHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TitleMEAspectInputBus extends TitleMEAspectBus implements IAspectSource, ITickable, IConsumable<RequirementAspect.RT>, ICraftNotifier<RequirementAspect.RT> {

    private final AspectList recipeEssentia = new AspectList();
    public AspectList essentia = new AspectList();
    private int existTime;

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(IB.itemMEAspectInputBus);
    }

    @Nullable
    @Override
    public GenericMachineCompoment<RequirementAspect.RT> provideComponent() {
        return new GenericMachineCompoment<>(this, this, (ComponentType) MMCompoments.COMPONENT_ASPECT);
    }

    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.essentia.readFromNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.essentia.writeToNBT(compound);
        return compound;
    }

    @Override
    public void update() {
        if (!this.world.isRemote) {
            this.existTime++;
            if (this.recipeEssentia.size() > 0) {
                if (this.getProxy().isPowered() && this.getProxy().isActive()) {
                    for (Aspect aspect : this.recipeEssentia.getAspectsSortedByName()) {
                        int a = this.recipeEssentia.getAmount(aspect) - this.essentia.getAmount(aspect);
                        if (a > 0) {
                            int canTake = takeAspectFromME(aspect, a, true);
                            this.essentia.add(aspect, canTake);
                        }
                    }
                }
            }

            if (this.existTime % 20 == 0) {
                this.sync();
                this.markDirty();
            }
        }
    }

    public void spillAll() {
        int vs = this.essentia.visSize();
        AuraHelper.polluteAura(this.world, this.getPos(), (float) vs * 0.25F, true);
        int f = this.essentia.getAmount(Aspect.FLUX);
        if (f > 0) {
            AuraHelper.polluteAura(this.world, this.getPos(), (float) f * 0.75F, false);
        }
        this.essentia.aspects.clear();
    }

    public void sync() {
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public boolean consume(RequirementAspect.RT rt, boolean b) {
        if (this.recipeEssentia.getAmount(rt.getAspect()) <= 0) {
            this.recipeEssentia.add(rt.getAspect(), rt.getAmount());
        }
        int consume = Math.min(rt.getAmount(), this.essentia.getAmount(rt.getAspect()));
        rt.setAmount(rt.getAmount() - consume);
        return consume > 0;
    }

    @Override
    public void startCrafting(RequirementAspect.RT outputToken) {
        this.recipeEssentia.add(outputToken.getAspect(), outputToken.getAmount());
    }

    public void finishCrafting(RequirementAspect.RT outputToken) {
        for (Aspect aspect : this.recipeEssentia.aspects.keySet()) {
            this.essentia.remove(aspect);
        }
        this.recipeEssentia.aspects.clear();
        sync();
        markDirty();
    }

    @Override
    public AspectList getAspects() {
        return this.essentia;
    }

    @Override
    public void setAspects(AspectList aspectList) {
        this.essentia = aspectList;
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return true;
    }

    @Override
    public int addToContainer(Aspect aspect, int i) {
        int ce = this.recipeEssentia.getAmount(aspect) - this.essentia.getAmount(aspect);
        if (ce <= 0) {
            return i;
        }
        int add = Math.min(ce, i);
        this.essentia.add(aspect, add);
        this.sync();
        this.markDirty();
        return i - add;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return this.essentia.getAmount(aspect) >= i;
    }

    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        return false;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return 0;
    }

    @Override
    public boolean isBlocked() {
        return false;
    }
}
