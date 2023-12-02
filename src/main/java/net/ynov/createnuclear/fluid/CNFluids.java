package net.ynov.createnuclear.fluid;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.Tags.CNTag;
import net.ynov.createnuclear.Tags.CNTags2;

import javax.annotation.Nullable;


public class CNFluids {

    public static FluidEntry<SimpleFlowableFluid.Flowing> URANIUM;

    public static void register() {
        var uranium = CreateNuclear.REGISTRATE.fluid("uranium", new ResourceLocation("createnuclear","block/fluid/uranium_still"), new ResourceLocation("createnuclear","block/fluid/uranium_flow"))
                .fluidAttributes(() -> new CreateNuclearAttributeHandler("fluid.createnuclear.uranium", 2500, 1600))
                .fluidProperties(p -> p.levelDecreasePerBlock(2)
                        .tickRate(15)
                        .flowSpeed(6)
                        .blastResistance(100f))
                .tag(CNTag.FluidTag.URANIUM.tag)
                .source(SimpleFlowableFluid.Source::new);

        URANIUM = uranium.register();

    }

    private record CreateNuclearAttributeHandler(Component name, int viscosity, boolean lighterThanAir) implements FluidVariantAttributeHandler {
        private CreateNuclearAttributeHandler(String key, int viscosity, int density) {
            this(Component.translatable(key), viscosity, density <= 0);
        }

        @Override
        public Component getName(FluidVariant fluidVariant) {
            return name;
        }

        @Override
        public int getViscosity(FluidVariant variant, @Nullable Level world) {
            return viscosity;
        }

        @Override
        public boolean isLighterThanAir(FluidVariant variant) {
            return lighterThanAir;
        }
    }

    public static void handleFluidEffect(ServerLevel world) {
        for (var entity: world.getAllEntities()) {
            if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && !livingEntity.isSpectator()) {
                if (entity.tickCount % 20 != 0) return;
                CreateNuclear.LOGGER.info("In fluid ? " + livingEntity.updateFluidHeightAndDoFluidPushing(CNTag.FluidTag.URANIUM.tag, 0.014));


                if (livingEntity.updateFluidHeightAndDoFluidPushing(CNTag.FluidTag.URANIUM.tag, 0.014)) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 3, true, true, true));
                }
                if (livingEntity.isSwimming() && livingEntity.isEyeInFluid(FluidTags.WATER)) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 0, 3, false, false, false));
                }
            }
        }
    }
}
