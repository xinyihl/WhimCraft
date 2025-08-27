package com.xinyihl.whimcraft.common.mixins.appliedenergistics2.accessor;

import appeng.client.gui.implementations.GuiMEMonitorable;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.client.me.ItemRepo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = GuiMEMonitorable.class, remap = false)
public interface GuiMEMonitorableAccessor {
    @Accessor
    MEGuiTextField getSearchField();
    @Accessor
    ItemRepo getRepo();
    @Invoker
    void invokeSetScrollBar();
}
