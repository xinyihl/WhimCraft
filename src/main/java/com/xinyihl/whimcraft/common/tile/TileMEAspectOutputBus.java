package com.xinyihl.whimcraft.common.tile;

import com.warmthdawn.mod.gugu_utils.modularmachenary.MMCompoments;
import com.warmthdawn.mod.gugu_utils.modularmachenary.components.GenericMachineCompoment;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.basic.IGeneratable;
import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.tile.base.TileMEAspectBus;
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

public class TileMEAspectOutputBus extends TileMEAspectBus implements IAspectSource, ITickable, IGeneratable<RequirementAspect.RT> {

    public AspectList essentia = new AspectList();
    private int tickCounter = 0;

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(IB.itemMEAspectOutputBus);
    }

    @Nullable
    @Override
    public GenericMachineCompoment<RequirementAspect.RT> provideComponent() {
        return new GenericMachineCompoment<>(this, (ComponentType) MMCompoments.COMPONENT_ASPECT);
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
                    if (this.essentia.size() > 0) {
                        if (this.getProxy().isPowered() && this.getProxy().isActive()) {
                            for (Aspect aspect : this.essentia.getAspects()) {
                                int canInsert = addAspectToME(aspect, this.essentia.getAmount(aspect), true);
                                this.essentia.remove(aspect, canInsert);
                            }
                        }
                    }
                    this.sync();
                }
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
    public boolean generate(RequirementAspect.RT rt, boolean b) {
        synchronized (this) {
            int generated = Math.min(rt.getAmount(), Configurations.GENERAL_CONFIG.aspectOutputHatchMaxStorage - this.essentia.visSize());
            rt.setAmount(rt.getAmount() - generated);
            if (b && generated > 0) {
                this.essentia.add(rt.getAspect(), generated);
            }
            return generated > 0;
        }
    }

    public int addToContainer(Aspect aspect, int i) {
        return i;
    }

    public boolean takeFromContainer(Aspect aspect, int i) {
        synchronized (this) {
            if (this.essentia.getAmount(aspect) >= i) {
                this.essentia.remove(aspect, i);
                this.sync();
                this.markDirty();
                return true;
            } else {
                return false;
            }
        }
    }

    @Deprecated
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
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
