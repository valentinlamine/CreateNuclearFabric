package net.ynov.createnuclear.fluid;

import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.tags.CNTag;
import net.ynov.createnuclear.effects.CNEffects;

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
                .tag(CNTag.forgeFluidTag("uranium"), CNTag.FluidTag.LAVA.tag)
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
        world.players().forEach(player -> {
            //CreateNuclear.LOGGER.info("In fluid ? " + pla.updateFluidHeightAndDoFluidPushing(CNTag.FluidTag.URANIUM.tag, 0.014));
            if (player.isAlive() && !player.isSpectator()) {
                if (player.tickCount % 20 != 0) return;
                if (player.updateFluidHeightAndDoFluidPushing(CNTag.FluidTag.URANIUM.tag, 0.014)) {
                    player.addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), 100, 0));
                }
            }});
    }
}
