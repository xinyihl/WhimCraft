package com.xinyihl.whimcraft.common.api;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IHasProbeInfo {
    void addProbeInfo(Consumer<String> consumer, Function<String, String> loc);
}
