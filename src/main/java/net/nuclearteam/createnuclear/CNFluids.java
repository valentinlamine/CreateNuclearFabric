package net.nuclearteam.createnuclear;

import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteStoneTypes;
import net.nuclearteam.createnuclear.content.fluids.FluidInteractionManager;
import net.nuclearteam.createnuclear.content.fluids.FluidInteractionManager.FluidInteractionRule;
import net.nuclearteam.createnuclear.CNTags.CNFluidTags;

import javax.annotation.Nullable;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class CNFluids {

    public static final FluidEntry<SimpleFlowableFluid.Flowing> URANIUM = CreateNuclear.REGISTRATE.fluid("uranium", CreateNuclear.asResource("fluid/uranium_still"), CreateNuclear.asResource("fluid/uranium_flow"))
            .fluidAttributes(() -> new CreateNuclearAttributeHandler("fluid.createnuclear.uranium", 2500, 1600))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(15)
                    .flowSpeed(6)
                    .blastResistance(100f))
            .lang("Liquid Uranium")
            .tag(CNFluidTags.URANIUM.tag)
            .register();

    public static final FluidEntry<SimpleFlowableFluid.Flowing> THORIUM = CreateNuclear.REGISTRATE.fluid("thorium", CreateNuclear.asResource("fluid/thorium_still"), CreateNuclear.asResource("fluid/thorium_flow"))
            .fluidAttributes(() -> new CreateNuclearAttributeHandler("fluid.createnuclear.thorium", 2500, 1600))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(15)
                    .flowSpeed(6)
                    .blastResistance(100f))
            .lang("Liquid Thorium")
            .tag(CNFluidTags.THORIUM.tag)
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
                if (player.updateFluidHeightAndDoFluidPushing(CNFluidTags.URANIUM.tag, 0.014) || player.updateFluidHeightAndDoFluidPushing(CNFluidTags.URANIUM.tag, 0.014)) {
                    player.addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), 100, 0));
                }
            }
        });
    }

    public static void registerFluidInteractions() {
        Function<Boolean, BlockState> AUTUNITE = isSource -> CNPaletteStoneTypes.AUTUNITE.getBaseBlock().get().defaultBlockState();
        // Uranium + water interaction
        FluidInteractionManager.addRule(
            CNFluids.URANIUM.get(),
            new FluidInteractionRule(
                100,
                fs -> fs.is(FluidTags.WATER),
                true, AUTUNITE,
                ctx -> {},
                false
            )
        );

        // Uranium + lava interaction
        FluidInteractionManager.addRule(
            CNFluids.URANIUM.get(),
            new FluidInteractionRule(
                100,
                fs -> fs.is(FluidTags.LAVA),
                true, AUTUNITE,
                ctx -> {},
                false
            )
        );
        // Thorium + water interaction
        FluidInteractionManager.addRule(
            CNFluids.THORIUM.get(),
            new FluidInteractionRule(
                100,
                fs -> fs.is(FluidTags.WATER),
                true, AUTUNITE,
                ctx -> {},
                false
            )
        );

        // Thorium + lava interaction
        FluidInteractionManager.addRule(
            CNFluids.THORIUM.get(),
            new FluidInteractionRule(
                100,
                fs -> fs.is(FluidTags.LAVA),
                true, AUTUNITE,
                ctx -> {},
                false
            )
        );
    }
}
