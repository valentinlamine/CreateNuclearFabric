package net.nuclearteam.createnuclear.tags;

import static net.nuclearteam.createnuclear.tags.CNTag.NameSpace.*;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.nuclearteam.createnuclear.CreateNuclear;

public class CNTag {
    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> forgeTag(Registry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("c", path));
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(BuiltInRegistries.BLOCK, path);
    }
    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(BuiltInRegistries.FLUID, path);
    }
    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(BuiltInRegistries.ITEM, path);
    }

    public enum NameSpace {
        MOD(CreateNuclear.MOD_ID, false, true),
        CREATE("create"),
        FORGE("f"),
        FABRIC("c"),
        MINECRAFT("minecraft")
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
        URANIUM,
        LAVA(MINECRAFT)
        ;

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
        FAN_PROCESSING_CATALYSTS_ENRICHED("fan_processing_catalysts/enriched"),
        ENRICHING_FIRE_BASE_BLOCKS("uranium_fire_base_blocks"),
        CAMPFIRE(MINECRAFT),
        ALL_CAMPFIRE(MINECRAFT, "all/campfires"),
        DRAGON_TRANSPARENT(MINECRAFT),
        FIRE(MINECRAFT),
        NEEDS_DIAMOND_TOOL(MINECRAFT),
        NEEDS_IRON_TOOL(MINECRAFT),
        NEEDS_STONE_TOOL(MINECRAFT),
        SHOVEL(MINECRAFT, "mineable/shovel"),
        URANIUM_ORES,
        LEAD_ORES,


        ;
        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;

        BlockTags() {
            this(MOD);
        }

        BlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwayDatagenDefault);
        }

        BlockTags(NameSpace namespace, String path) { this(namespace, path, namespace.optionalDefault, namespace.alwayDatagenDefault); }

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

    public enum ItemTags {
        CLOTH,
        FUEL,
        COOLER,
        URANIUM_ORES,
        LEAD_ORES,
        ANTI_RADIATION_HELMET_DYE,
        ANTI_RADIATION_CHESTPLATE_DYE,
        ANTI_RADIATION_LEGGINGS_DYE,
        ANTI_RADIATION_BOOTS_DYE,
        ANTI_RADIATION_ARMOR,
        ;

        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;

        ItemTags() {
            this(MOD);
        }

        ItemTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwayDatagenDefault);
        }

        ItemTags(NameSpace nameSpace, String path) {
            this(nameSpace, nameSpace.optionalDefault, nameSpace.alwayDatagenDefault);
        }
        
        ItemTags(NameSpace nameSpace, boolean optional, boolean alwaysDatagen) {
            this(nameSpace, null, optional, alwaysDatagen);
        }

        ItemTags(NameSpace nameSpace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(nameSpace.id, path == null ? Lang.asId(name()) : path);
            tag = optionalTag(BuiltInRegistries.ITEM, id);
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Item item) {
            return item.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }

        private static void init() {}
    }

    public enum EntityTypeTags {
        FALL_DAMAGE_IMMUNE(MINECRAFT, "fall_damage_immune"),
        IRRADIATED_IMMUNE("irradiated_immune"),
        ;

        public final TagKey<EntityType<?>> tag;
        public final boolean alwaysDatagen;

        EntityTypeTags() {
            this(MOD);
        }

        EntityTypeTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwayDatagenDefault);
        }

        EntityTypeTags(String path) {
            this(MOD, path, MOD.optionalDefault, MOD.alwayDatagenDefault);
        }

        EntityTypeTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwayDatagenDefault);
        }

        EntityTypeTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        EntityTypeTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.ENTITY_TYPE, id);
            } else {
                tag = TagKey.create(Registries.ENTITY_TYPE, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(EntityType<?> type) {
            return type.is(tag);
        }

        public boolean matches(Entity entity) {
            return matches(entity.getType());
        }

        private static void init() {}

    }


    public static void registerModItems() {
        CreateNuclear.LOGGER.info("Registering mod tags for " + CreateNuclear.MOD_ID);
        FluidTag.init();
        BlockTags.init();
        ItemTags.init();
    }
}
