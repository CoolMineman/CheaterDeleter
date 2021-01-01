package io.github.coolmineman.cheaterdeleter.trackers.data;

import java.util.concurrent.atomic.AtomicLong;

public class PlayerHitGroundData implements Data {
    public AtomicLong lastInAir = new AtomicLong(Long.MIN_VALUE);
}
