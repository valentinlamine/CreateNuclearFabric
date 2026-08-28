package net.nuclearteam.createnuclear;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.fabricators_of_create.porting_lib.fluids.FluidInteractionRegistry;
import io.github.fabricators_of_create.porting_lib.fluids.FluidInteractionRegistry.InteractionInformation;
import io.github.fabricators_of_create.porting_lib.fluids.FluidType;
import io.github.fabricators_of_create.porting_lib.fluids.PortingLibFluids;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteStoneTypes;
import net.nuclearteam.createnuclear.content.radiation.RadiationBucketItem;
import net.nuclearteam.createnuclear.content.radiation.capability.RadiationCapability;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.CNTags.CNFluidTags;

import javax.annotation.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class CNFluids {
    private static final double URANIUM_FLUID_DOSE = 5.0D;
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
            .properties(p -> p.mapColor(MapColor.GREEN))
            .build()
            .bucket((source, settings) -> new RadiationBucketItem(() -> source, settings, 20))
            .onRegister(CNFluids::registerFluidDispenseBehavior)
            .tag(CNTags.forgeItemTag("buckets/uranium"))
            .lang("Uranium Bucket")
            .build()
            .register();

    public static final FluidEntry<SimpleFlowableFluid.Flowing> THORIUM = REGISTRATE.fluid("thorium", CreateNuclear.asResource("fluid/thorium_still"), CreateNuclear.asResource("fluid/thorium_flow"))
            .fluidAttributes(() -> new CreateNuclearAttributeHandler("fluid.createnuclear.thorium", 200, 100))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(15)
                    .flowSpeed(6)
                    .blastResistance(100f))
            .lang("Liquid Thorium")
            .tag(CNFluidTags.THORIUM.tag)
            .source(SimpleFlowableFluid.Source::new)
            .block()
            .build()
            .bucket()
            .onRegister(CNFluids::registerFluidDispenseBehavior)
            .tag(CNTags.forgeItemTag("buckets/thorium"))
            .lang("Thorium Bucket")
            .build()
            .register();

    public static final FluidEntry<SimpleFlowableFluid.Flowing> LIQUID_NITROGEN = REGISTRATE.fluid("nitrogen", CreateNuclear.asResource("fluid/nitrogen_still"), CreateNuclear.asResource("fluid/nitrogen_flow"))
            .fluidAttributes(() -> new CreateNuclearAttributeHandler("fluid.createnuclear.nitrogen", 1000, 1000))
            .fluidProperties(p -> p.levelDecreasePerBlock(5)
                    .tickRate(10)
                    .flowSpeed(6)
                    .blastResistance(100f))
            .lang("Liquid Nitrogen")
            .tag(CNFluidTags.NITROGEN.tag)
            .source(SimpleFlowableFluid.Source::new)
            .block()
            .build()
            .bucket()
            .onRegister(CNFluids::registerFluidDispenseBehavior)
            .tag(CNTags.forgeItemTag("buckets/nitrogen"))
            .lang("Nitrogen Bucket")
            .build()
            .register();

    public static final FluidType URANIUM_FLUID_TYPE = new FluidType(FluidType.Properties.create());
    public static final FluidType THORIUM_FLUID_TYPE = new FluidType(FluidType.Properties.create());
    public static final FluidType NITROGEN_FLUID_TYPE = new FluidType(FluidType.Properties.create());

    public static void register() {
        Registry.register(PortingLibFluids.FLUID_TYPES, CreateNuclear.asResource("uranium"), URANIUM_FLUID_TYPE);
        Registry.register(PortingLibFluids.FLUID_TYPES, CreateNuclear.asResource("thorium"), THORIUM_FLUID_TYPE);
        Registry.register(PortingLibFluids.FLUID_TYPES, CreateNuclear.asResource("nitrogen"), NITROGEN_FLUID_TYPE);
    }

    public static void handleFluidEffect(LivingEntity entity) {
        if (!entity.isAlive() || entity.isSpectator()) return;

        if (entity.updateMovementInFluid(CNFluidTags.URANIUM.tag, 0.014)) {
            if (entity.age % 20 == 0) {
                RadiationCapability.applyContagion(entity, URANIUM_FLUID_DOSE, 100);
            }
        } else if (entity.updateMovementInFluid(CNFluidTags.NITROGEN.tag, 0.014)) {
            if (entity.isOnFire()) entity.extinguish();
            if (entity instanceof Player player && player.isSwimming()) {
                CNAdvancement.CRYOGENIC_BAPTISM.awardTo(player);
            }

            int currentTicks = entity.getFrozenTicks();
            int maxTicks = entity.getMinFreezeDamageTicks();
            int freezeSpeed = 3;
            if (entity.level().isClientSide) {
                entity.setFrozenTicks(Math.min(maxTicks + 1, currentTicks + freezeSpeed + 1));
            } else {
                entity.setFrozenTicks(Math.min(maxTicks + 2, currentTicks + freezeSpeed + 2));
                if (entity.getFrozenTicks() >= maxTicks && entity.age % 10 == 0) {
                    entity.damage(entity.getDamageSources().freeze(), 2.0F);
                }
                entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 1, true, false, false));
            }
        } else if (entity.updateMovementInFluid(CNFluidTags.THORIUM.tag, 0.014)) {
            entity.setOnFireFromLava();
        }
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
                URANIUM_FLUID_TYPE,
                fluidState -> {
                    if (fluidState.isStill()) {
                        return CNPaletteStoneTypes.AUTUNITE.getBaseBlock().get().defaultBlockState();
                    } else {
                        return CNPaletteStoneTypes.AUTUNITE.getBaseBlock().get().defaultBlockState();
                    }
                }
        ));
//
        FluidInteractionRegistry.addInteraction(PortingLibFluids.WATER_TYPE, new InteractionInformation(
                URANIUM_FLUID_TYPE,
                fluidState -> {
                    if (fluidState.isStill()) {
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
        protected ItemStack dispenseSilently(BlockSource pSource, ItemStack pStack) {
            SolidBucketItem dispensibleContainerItem = (SolidBucketItem) pStack.getItem();
            BlockPos pos = pSource.getPos().offset(pSource.getBlockState().getValue(DispenserBlock.FACING));
            Level level = pSource.getLevel();
            if (dispensibleContainerItem.placeFluid(null, level, pos, null)) {
                return new ItemStack(Items.BUCKET);
            }
            return DEFAULT.dispense(pSource, pStack);
        }
    };

    private static void registerFluidDispenseBehavior(BucketItem bucket) {
        DispenserBlock.registerBehavior(bucket, DISPENSE_FLUID);
    }
}
