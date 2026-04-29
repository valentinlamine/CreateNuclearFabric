package net.nuclearteam.createnuclear;

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

import static net.nuclearteam.createnuclear.CNTags.NameSpace.MOD;

@SuppressWarnings({"unused", "deprecation"})
public class CNTags {
    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> forgeTag(Registry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation(NameSpace.FABRIC.id, path));
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
        public final boolean alwaysDatagenDefault;

        NameSpace(String id) {
            this(id, true, false);
        }
        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }

    public enum CNFluidTags {
        URANIUM,
        THORIUM,
        NITROGEN,
        ;

        public final TagKey<Fluid> tag;
        public final boolean alwaysDatagen;

        CNFluidTags() {
            this(MOD);
        }
        CNFluidTags(NameSpace nameSpace) {
            this(nameSpace, nameSpace.optionalDefault, nameSpace.alwaysDatagenDefault);
        }
        CNFluidTags(NameSpace nameSpace, String path) {
            this(nameSpace, path, nameSpace.optionalDefault, nameSpace.alwaysDatagenDefault);
        }

        CNFluidTags(NameSpace nameSpace, boolean optional, boolean alwaysDatagen) {
            this(nameSpace, null, optional, alwaysDatagen);
        }

        CNFluidTags(NameSpace nameSpace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(nameSpace.id, path == null ? Lang.asId(name()) : path);
            tag = optionalTag(BuiltInRegistries.FLUID, id);
            this.alwaysDatagen = alwaysDatagen;
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

    public enum CNBlockTags {
        FAN_PROCESSING_CATALYSTS_ENRICHED("fan_processing_catalysts/enriched"),
        ENRICHING_FIRE_BASE_BLOCKS("uranium_fire_base_blocks"),
        URANIUM_ORES,
        LEAD_ORES,
        THORIUM_ORES,
        REACTOR_CASING,
        NITRATE_ORES

        ;
        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;

        CNBlockTags() {
            this(MOD);
        }

        CNBlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNBlockTags(NameSpace namespace, String path) { this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault); }

        CNBlockTags(String path) {
            this(MOD, path, MOD.optionalDefault, MOD.alwaysDatagenDefault);
        }

        CNBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        CNBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
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

    public enum CNItemTags {
        CLOTH,
        FUEL,
        COOLER,
        URANIUM_ORES,
        LEAD_ORES,
        THORIUM_ORES,
        NITRATE_ORES,
        ANTI_RADIATION_HELMET_DYE,
        ANTI_RADIATION_CHESTPLATE_DYE,
        ANTI_RADIATION_LEGGINGS_DYE,
        ANTI_RADIATION_BOOTS_DYE,
        ANTI_RADIATION_ARMOR,
        ALL_ANTI_RADIATION_ARMORS,
        ANTI_RADIATION_HELMET_FULL_DYE,
        ANTI_RADIATION_CHESTPLATE_FULL_DYE,
        ANTI_RADIATION_LEGGINGS_FULL_DYE,
        ;

        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;

        CNItemTags() {
            this(MOD);
        }

        CNItemTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNItemTags(NameSpace nameSpace, String path) {
            this(nameSpace, nameSpace.optionalDefault, nameSpace.alwaysDatagenDefault);
        }
        
        CNItemTags(NameSpace nameSpace, boolean optional, boolean alwaysDatagen) {
            this(nameSpace, null, optional, alwaysDatagen);
        }

        CNItemTags(NameSpace nameSpace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(nameSpace.id, path == null ? Lang.asId(name()) : path);
            tag = optionalTag(BuiltInRegistries.ITEM, id);
            this.alwaysDatagen = alwaysDatagen;
        }


        public boolean matches(Item item) {
            return item.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }

        private static void init() {}
    }

    public enum CNEntityTypeTags {
        IRRADIATED_IMMUNE("irradiated_immune"),
        ;

        public final TagKey<EntityType<?>> tag;
        public final boolean alwaysDatagen;

        CNEntityTypeTags() {
            this(MOD);
        }

        CNEntityTypeTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNEntityTypeTags(String path) {
            this(MOD, path, MOD.optionalDefault, MOD.alwaysDatagenDefault);
        }

        CNEntityTypeTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CNEntityTypeTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        CNEntityTypeTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
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


    public static void register() {
        CreateNuclear.LOGGER.info("Registering mod tags for " + CreateNuclear.MOD_ID);
        CNFluidTags.init();
        CNBlockTags.init();
        CNItemTags.init();
        CNEntityTypeTags.init();
    }
}
