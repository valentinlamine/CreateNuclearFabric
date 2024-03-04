package net.ynov.createnuclear.tags;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.ynov.createnuclear.CreateNuclear;

import static net.ynov.createnuclear.tags.CNTag.NameSpace.MOD;

public class CNTag {
    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> forgeTag(Registry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("createnuclear", path));
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(BuiltInRegistries.BLOCK, path);
    }
    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(BuiltInRegistries.FLUID, path);
    }

    public enum NameSpace {
        MOD(CreateNuclear.MOD_ID, false, true),
        CREATE("c"),
        FORGE(CreateNuclear.MOD_ID)

        ;

        public final String id;
        public final boolean optionalDefault;
        public final boolean alwayDatagenDefault;

        NameSpace(String id) {
            this(id, true, false);
        }
        NameSpace(String id, boolean optionalDefault, boolean alwayDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwayDatagenDefault = alwayDatagenDefault;
        }
    }

    public enum FluidTag {
        //URANIUM(NameSpace.MOD),
        URANIUM(NameSpace.CREATE);

        public final TagKey<Fluid> tag;
        public final boolean alwayDatagen;

        FluidTag() {
            this(MOD);
        }
        FluidTag(NameSpace nameSpace) {
            this(nameSpace, nameSpace.optionalDefault, nameSpace.alwayDatagenDefault);
        }
        FluidTag(NameSpace nameSpace, String path) {
            this(nameSpace, path, nameSpace.optionalDefault, nameSpace.alwayDatagenDefault);
        }

        FluidTag(NameSpace nameSpace, boolean optional, boolean alwayDatagen) {
            this(nameSpace, null, optional, alwayDatagen);
        }

        FluidTag(NameSpace nameSpace, String path, boolean optional, boolean alwayDatagen) {
            ResourceLocation id = new ResourceLocation(nameSpace.id, path == null ? Lang.asId(name()) : path);
            tag = optionalTag(BuiltInRegistries.FLUID, id);
            this.alwayDatagen = alwayDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Fluid fluid) {
            return fluid.is(tag);
        }

        public boolean matches(FluidState state) {
            return state.is(tag);
        }

        private static void init() {}
    }

    public enum BlockTags {
        FAN_PROCESSING_CATALYSTS_ENRICHED(MOD, "fan_processing_catalysts/enriched"),
        ENRICHING_FIRE_BASE_BLOCKS(MOD, "uranium_fire_base_blocks"),
        ;
        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;

        BlockTags() {
            this(MOD);
        }

        BlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwayDatagenDefault);
        }

        BlockTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwayDatagenDefault);
        }

        BlockTags(String path) {
            this(MOD, path, MOD.optionalDefault, MOD.alwayDatagenDefault);
        }

        BlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        BlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            tag = optionalTag(BuiltInRegistries.BLOCK, id);
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Block block) {
            return block.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
        }

        public boolean matches(BlockState state) {
            return state.is(tag);
        }

        private static void init() {}
    }



    public static void registerModItems() {
        CreateNuclear.LOGGER.info("Registering mod tags for " + CreateNuclear.MOD_ID);
        FluidTag.init();
        BlockTags.init();
    }
}
