package net.nuclearteam.createnuclear.content.multiblock.input.fluid;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidHelper.FluidExchange;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class PlayerInteractReactorFluidInput {
    public static InteractionResult interact(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, boolean onClient, BlockHitResult ray) {
        FluidExchange exchange = null;
        ReactorFluidInputEntity be = (ReactorFluidInputEntity) level.getBlockEntity(pos);
        if (be == null) {
            return InteractionResult.FAIL;
        }

        Storage<FluidVariant> fluidInput = be.getFluidStorage(ray.getDirection());
        FluidStack prevFluidInInput = TransferUtil.firstCopyOrEmpty(fluidInput);

        if (FluidHelper.tryEmptyItemIntoBE(level, player, hand, stack, be, ray.getDirection())) exchange = FluidExchange.ITEM_TO_TANK;
        if (FluidHelper.tryFillItemFromBE(level, player, hand, stack, be, ray.getDirection())) exchange = FluidExchange.TANK_TO_ITEM;

        if (exchange == null) {
            if (GenericItemEmptying.canItemBeEmptied(level, stack) || GenericItemFilling.canItemBeFilled(level, stack)) return InteractionResult.SUCCESS;
            return InteractionResult.PASS;
        }

        SoundEvent soundEvent = null;
        BlockState fluidState = null;
        FluidStack fluidInInput = TransferUtil.firstOrEmpty(fluidInput);

        if (exchange == FluidExchange.ITEM_TO_TANK) {
            Fluid fluid = fluidInInput.getFluid();
            fluidState = fluid.defaultFluidState().createLegacyBlock();
            soundEvent = FluidHelper.getEmptySound(fluidInInput);
        }

        if (exchange == FluidExchange.TANK_TO_ITEM) {
            Fluid fluid = prevFluidInInput.getFluid();
            fluidState = fluid.defaultFluidState().createLegacyBlock();
            soundEvent = FluidHelper.getFillSound(prevFluidInInput);
        }

        if (soundEvent != null && !onClient) {
            float pitch = Mth
                    .clamp(1 - (1f * fluidInInput.getAmount() / (FluidTankBlockEntity.getCapacityMultiplier() * 16)), 0, 1);
            pitch /= 1.5f;
            pitch += .5f;
            pitch += (level.random.nextFloat() - .5f) / 4f;
            level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, .5f, pitch);
        }

        if (!fluidInInput.isFluidStackIdentical(prevFluidInInput)) {
            if (fluidState != null && onClient) {
                BlockParticleOption blockParticleData =
                        new BlockParticleOption(ParticleTypes.BLOCK, fluidState);
                float flevel = (float) fluidInInput.getAmount() / be.getTankCapacity();

                boolean reversed = FluidVariantAttributes.isLighterThanAir(fluidInInput.getType());
                if (reversed)
                    flevel = 1 - flevel;

                Vec3 vec = ray.getLocation();
                vec = new Vec3(vec.x, be.getBlockPos()
                        .getY() + flevel * (1 - .5f) + .25f, vec.z);
                Vec3 motion = player.position()
                        .subtract(vec)
                        .multiply(1 / 20f);
                vec = vec.add(motion);
                level.addParticle(blockParticleData, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;

    }
}
