package net.nuclearteam.createnuclear.content.explosion;

import net.minecraft.world.entity.Entity;

public abstract class CNAdvancedEntityModel<T extends Entity> extends CNBasicEntityModel<T> {
    private float movementScale = 1.0F;
    public int texWidth = 32;
    public int texHeight = 32;

    public void updateDefaultPose() {
        this.getAllParts().forEach(CNAdvancedModelBox::updateDefaultPose);
    }

    public void resetToDefaultPose() {
        this.getAllParts().forEach(CNAdvancedModelBox::resetToDefaultPose);
    }

    public float getMovementScale() {
        return this.movementScale;
    }

    public void setRotateAngle(CNAdvancedModelBox model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public abstract Iterable<CNAdvancedModelBox> getAllParts();
}
