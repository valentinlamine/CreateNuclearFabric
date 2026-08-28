package net.nuclearteam.createnuclear.content.radiation.capability;

import net.minecraft.resources.ResourceLocation;

public interface IRadiationCapability {
    double getRadiation();
    void setRadiation(double value);

    long getInventoryHash();
    void setInventoryHash(long hash);

    ResourceLocation getLastBiomeLocation();
    void setLastBiomeLocation(ResourceLocation location);

    double getContagionDose();
    void setContagionDose(double dose);

    int getContagionTicks();
    void setContagionTicks(int ticks);
}
