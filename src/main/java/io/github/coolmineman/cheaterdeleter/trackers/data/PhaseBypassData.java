package io.github.coolmineman.cheaterdeleter.trackers.data;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongLists;

public class PhaseBypassData implements Data {
    public final LongList bypassPos = LongLists.synchronize(new LongArrayList());
    public volatile long lastUpdated = System.currentTimeMillis();
}
