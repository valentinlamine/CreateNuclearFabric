package net.nuclearteam.createnuclear.content.logistics;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

/** Unit boundary between Forge balance data (millibuckets) and Fabric Transfer (droplets). */
public final class FluidUnits {
    public static final long DROPLETS_PER_MILLIBUCKET = FluidConstants.BUCKET / 1_000L;

    private FluidUnits() {
    }

    public static long milliBucketsToDroplets(long milliBuckets) {
        return Math.multiplyExact(milliBuckets, DROPLETS_PER_MILLIBUCKET);
    }
}
