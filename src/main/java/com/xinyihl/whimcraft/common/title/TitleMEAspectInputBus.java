package com.xinyihl.whimcraft.common.title;

import com.warmthdawn.mod.gugu_utils.modularmachenary.MMCompoments;
import com.warmthdawn.mod.gugu_utils.modularmachenary.components.GenericMachineCompoment;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.basic.IConsumable;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.basic.ICraftNotifier;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.title.base.TitleMEAspectBus;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
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
    private int tickCounter = 0;

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
            this.tickCounter = (this.tickCounter + 1) % 20;
            if (this.tickCounter % 20 == 0) {
                synchronized (this) {
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
                }
                this.sync();
            }
        }
    }

    public void spillAll() {
        synchronized (this) {
            int vs = this.essentia.visSize();
            AuraHelper.polluteAura(this.world, this.getPos(), (float) vs * 0.25F, true);
            int f = this.essentia.getAmount(Aspect.FLUX);
            if (f > 0) {
                AuraHelper.polluteAura(this.world, this.getPos(), (float) f * 0.75F, false);
            }
            this.essentia.aspects.clear();
        }
    }

    @Override
    public boolean consume(RequirementAspect.RT rt, boolean b) {
        synchronized (this) {
            if (this.recipeEssentia.getAmount(rt.getAspect()) <= 0) {
                this.recipeEssentia.add(rt.getAspect(), rt.getAmount());
            }
            int consume = Math.min(rt.getAmount(), this.essentia.getAmount(rt.getAspect()));
            rt.setAmount(rt.getAmount() - consume);

            return consume > 0;
        }
    }

    @Override
    public void startCrafting(RequirementAspect.RT outputToken) {
        synchronized (this) {
            this.recipeEssentia.add(outputToken.getAspect(), outputToken.getAmount());
        }
    }

    public void finishCrafting(RequirementAspect.RT outputToken) {
        synchronized (this) {
            Aspect aspect = outputToken.getAspect();
            int amount = outputToken.getAmount();
            this.recipeEssentia.remove(aspect, amount);
            this.essentia.remove(aspect, amount);
            sync();
        }
    }

    @Override
    public AspectList getAspects() {
        synchronized (this) {
            return this.essentia;
        }
    }

    @Override
    public void setAspects(AspectList aspectList) {
        synchronized (this) {
            this.essentia = aspectList;
        }
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return true;
    }

    public int addToContainer(Aspect aspect, int i) {
        synchronized (this) {
            int ce = this.recipeEssentia.getAmount(aspect) - this.essentia.getAmount(aspect);
            if (ce <= 0) {
                return i;
            } else {
                int add = Math.min(ce, i);
                this.essentia.add(aspect, add);
                this.sync();
                this.markDirty();
                return i - add;
            }
        }
    }

    public boolean takeFromContainer(Aspect aspect, int i) {
        return false;
    }

    @Override
    @Deprecated
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        synchronized (this) {
            return this.essentia.getAmount(aspect) >= i;
        }
    }

    @Override
    @Deprecated
    public boolean doesContainerContain(AspectList aspectList) {
        return false;
    }

    @Override
    public int containerContains(Aspect aspect) {
        synchronized (this) {
            return this.essentia.getAmount(aspect);
        }
    }

    @Override
    public boolean isBlocked() {
        return false;
    }
}
