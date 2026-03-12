package com.xinyihl.whimcraft.common.mixins.extrautils2;

import com.rwtema.extrautils2.api.machine.Machine;
import com.rwtema.extrautils2.api.machine.MachineRegistry;
import com.rwtema.extrautils2.api.machine.XUMachineCrusher;
import com.rwtema.extrautils2.machine.MachineInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

/**
 * Remove Crusher since its whole functionality copied with other crusher.
 * Buff machine capacity and transfer rates.
 */
@Mixin(value = MachineInit.class, remap = false)
public abstract class MachineInitMixin {

    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/rwtema/extrautils2/api/machine/MachineRegistry;register(Lcom/rwtema/extrautils2/api/machine/Machine;)Lcom/rwtema/extrautils2/api/machine/Machine;"
            )
    )
    private static Machine disableCrusher(Machine machine) {
        if (machine != XUMachineCrusher.INSTANCE) {
            MachineRegistry.register(machine);
        }
        return machine;
    }

    @ModifyConstant(
            method = "init",
            constant = {
                    @Constant(intValue = 100),
                    @Constant(intValue = 1000),
                    @Constant(intValue = 1600),
                    @Constant(intValue = 4000),
                    @Constant(intValue = 8000),
                    @Constant(intValue = 100000),
                    @Constant(intValue = 400000),
                    @Constant(intValue = 1000000)
            },
            slice = @Slice(
                    from = @At(
                            value = "NEW",
                            target = "com/rwtema/extrautils2/api/machine/Machine",
                            ordinal = 2,
                            shift = At.Shift.BEFORE
                    )
            )
    )
    private static int buffMachineCapacityAndTransfer(int value) {
        return Math.min(2000000000 / 64, value * 300);
    }
}
