package net.nuclearteam.createnuclear.content.multiblock.frame;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Block entity for {@link ReactorFrame}. It only stores a reference to the
 * reactor controller that owns this frame so the client-side renderer
 * ({@link ReactorFrameRenderer}) can fetch the reactor's current fluid and
 * draw it dynamically inside the frame window.
 *
 * <p>The controller position is assigned by
 * {@link net.nuclearteam.createnuclear.content.multiblock.ReactorAssembler}
 * during assembly and persisted/synced through {@link SmartBlockEntity}.</p>
 */
public class ReactorFrameEntity extends SmartBlockEntity {

    @Nullable
    private BlockPos controller;

    public ReactorFrameEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
        setController(pos);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) { }

    /** Assigns the owning reactor controller and syncs the change to clients. */
    public void setController(@Nullable BlockPos controllerPos) {
        if (java.util.Objects.equals(this.controller, controllerPos)) return;
        this.controller = controllerPos;
        notifyUpdate();
    }

    @Nullable
    public BlockPos getController() {
        return controller;
    }

    /**
     * Resolves the controller block entity, returning {@code null} when no
     * controller is assigned or the referenced block is no longer a controller.
     */
    @Nullable
    public ReactorControllerBlockEntity getControllerEntity() {
        if (controller == null || getLevel() == null) return null;
        if (getLevel().getBlockEntity(controller) instanceof ReactorControllerBlockEntity controllerEntity)
            return controllerEntity;
        return null;
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (tag.contains("Controller")) {
            this.controller = BlockPos.of(tag.getLong("Controller"));
        } else {
            this.controller = null;
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        if (controller != null) {
            tag.putLong("Controller", controller.asLong());
        }
    }
}
