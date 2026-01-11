package net.nuclearteam.createnuclear;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.fabricators_of_create.porting_lib.fluids.FluidInteractionRegistry;
import io.github.fabricators_of_create.porting_lib.fluids.FluidInteractionRegistry.InteractionInformation;
import io.github.fabricators_of_create.porting_lib.fluids.FluidType;
import io.github.fabricators_of_create.porting_lib.fluids.PortingLibFluids;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteStoneTypes;
import net.nuclearteam.createnuclear.CNTags.CNFluidTags;

import javax.annotation.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class CNFluids {
    private static final CreateRegistrate REGISTRATE = CreateNuclear.registrate();


    public static final FluidEntry<SimpleFlowableFluid.Flowing> URANIUM = REGISTRATE.fluid("uranium", CreateNuclear.asResource("fluid/uranium_still"), CreateNuclear.asResource("fluid/uranium_flow"))
            .fluidAttributes(() -> new CreateNuclearAttributeHandler("fluid.createnuclear.uranium", 2500, 1600))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(15)
                    .flowSpeed(6)
                    .blastResistance(100f))
            .lang("Liquid Uranium")
            .tag(CNFluidTags.URANIUM.tag)
            .source(SimpleFlowableFluid.Source::new)
            .block()
            .properties(p -> p.mapColor(MapColor.COLOR_GREEN))
            .build()
            .bucket()
            .onRegister(CNFluids::registerFluidDispenseBehavior)
            .build()
            .register();

    public static final FluidType URANIUM_FLUID_TYPE = new FluidType(FluidType.Properties.create());

    public static void register() {
        Registry.register(PortingLibFluids.FLUID_TYPES, CreateNuclear.asResource("uranium"), URANIUM_FLUID_TYPE);
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

    public static void registerFluidInteractions() {
        FluidInteractionRegistry.addInteraction(PortingLibFluids.LAVA_TYPE, new InteractionInformation(
                URANIUM.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return CNPaletteStoneTypes.AUTUNITE.getBaseBlock().get().defaultBlockState();
                    } else {
                        return CNPaletteStoneTypes.AUTUNITE.getBaseBlock().get().defaultBlockState();
                    }
                }
        ));
//
        FluidInteractionRegistry.addInteraction(PortingLibFluids.WATER_TYPE, new InteractionInformation(
                URANIUM.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return CNPaletteStoneTypes.AUTUNITE.getBaseBlock().get().defaultBlockState();
                    } else {
                        return CNPaletteStoneTypes.AUTUNITE.getBaseBlock().get().defaultBlockState();
                    }
                }
        ));

    }

    private static final DispenseItemBehavior DEFAULT = new DefaultDispenseItemBehavior();
    private static final DispenseItemBehavior DISPENSE_FLUID = new DefaultDispenseItemBehavior(){
        @Override
        protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
            DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) pStack.getItem();
            BlockPos pos = pSource.getPos().relative(pSource.getBlockState().getValue(DispenserBlock.FACING));
            Level level = pSource.getLevel();
            if (dispensibleContainerItem.emptyContents(null, level, pos, null)) {
                return new ItemStack(Items.BUCKET);
            }
            return DEFAULT.dispense(pSource, pStack);
        }
    };

    private static void registerFluidDispenseBehavior(BucketItem bucket) {
        DispenserBlock.registerBehavior(bucket, DISPENSE_FLUID);
    }
}
