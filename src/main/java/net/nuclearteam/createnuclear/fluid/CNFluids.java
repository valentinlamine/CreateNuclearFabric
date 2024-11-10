package net.nuclearteam.createnuclear.fluid;

import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.tags.CNTag;

import javax.annotation.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class CNFluids {

    public static final FluidEntry<SimpleFlowableFluid.Flowing> URANIUM = CreateNuclear.REGISTRATE.fluid("uranium", CreateNuclear.asResource("block/fluid/uranium_still"), CreateNuclear.asResource("block/fluid/uranium_flow"))
            .fluidAttributes(() -> new CreateNuclearAttributeHandler("fluid.createnuclear.uranium", 2500, 1600))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(15)
                    .flowSpeed(6)
                    .blastResistance(100f))
            .lang("Liquid Uranium")
            .tag(CNTag.forgeFluidTag("uranium"), FluidTags.LAVA)
            .register();

    public static void register() {
    }

    private record CreateNuclearAttributeHandler(Component name, int viscosity, boolean lighterThanAir) implements FluidVariantAttributeHandler {
        private CreateNuclearAttributeHandler(String key, int viscosity, int density) {
            this(Component.translatable(key), viscosity, density <= 0);
        }

        @Override
        public Component getName(FluidVariant fluidVariant) {
            return name.copy();
        }

        @Override
        public int getViscosity(FluidVariant variant, @Nullable Level world) {
            return viscosity;
        }

        @Override
        public boolean isLighterThanAir(FluidVariant variant) {
            return lighterThanAir;
        }

        @Override
        public int getTemperature(FluidVariant variant) {
            return 0;
        }
    }

    public static void handleFluidEffect(ServerLevel world) {
        world.players().forEach(player -> {
            if (player.isAlive() && !player.isSpectator()) {
                if (player.tickCount % 20 != 0) return;
                if (player.updateFluidHeightAndDoFluidPushing(CNTag.FluidTag.URANIUM.tag, 0.014) || player.updateFluidHeightAndDoFluidPushing(CNTag.forgeFluidTag("uranium"), 0.014)) {
                    player.addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), 100, 0));
                }
            }
        });
    }

}
