package net.ynov.createnuclear.Tags;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.ynov.createnuclear.CreateNuclear;

import javax.naming.Name;
import java.util.Locale;

public class CNTag {


    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> commonTag(Registry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("c", path));
    }

    String CREATE = "create";

    static String toTagName(String enumName) {
        return enumName.replace('$', '/').toLowerCase(Locale.ROOT);
    }

    public enum NameSpace {
        MOD(CreateNuclear.MOD_ID, false, true),
        CREATE("create", false, true),
        COMMON("c");

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
        URANIUM(NameSpace.COMMON);

        public final TagKey<Fluid> tag;
        public final boolean alwayDatagen;

        FluidTag() {
            this(NameSpace.MOD);
        }
        FluidTag(boolean alwayDatagen) {
            this(NameSpace.MOD, NameSpace.MOD.optionalDefault, alwayDatagen);
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
        public boolean mathes(Fluid fluid) {
            return fluid.is(tag);
        }

        public boolean matches(FluidState state) {
            return state.is(tag);
        }

        private static void init() {}
    }














    public static void registerModItems() {
        CreateNuclear.LOGGER.info("Registering mod tags for " + CreateNuclear.MOD_ID);
        FluidTag.init();
    }
}
