package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

import java.util.List;

@MethodsReturnNonnullByDefault
public class CRadiation extends ConfigBase {
    public final ConfigBool enabledItemRadiation = b(true, "enabled_item_radiation", Comments.enabled);
    public final ConfigInt radiationLevel1 = i(10, 0, 50, "radiation_level_1", Comments.radiationLevel1);
    public final ConfigInt radiationLevel2 = i(25, 0, 50, "radiation_level_2", Comments.radiationLevel2);
    public final ConfigInt radiationLevel3 = i(50, 0, 50, "radiation_level_3", Comments.radiationLevel3);
    public final ConfigInt amplifierLevel0 = i(0, 0, 10, "amplifier_level_0", Comments.amplifierLevel0);
    public final ConfigInt amplifierLevel1 = i(1, 0, 10, "amplifier_level_1", Comments.amplifierLevel1);
    public final ConfigInt amplifierLevel2 = i(2, 0, 10, "amplifier_level_2", Comments.amplifierLevel2);
    public final ConfiguredLists configuredLists = nested(0, ConfiguredLists::new, Comments.list);

    @Override
    public String getName() {
        return "Radiation";
    }

    private static class Comments {
        static String enabled = "When enabled, certain items may emit radiation affecting players and nearby entities. Disable to turn off radiation effects without removing items.";
        static String radiationLevel1 = "Minimum radiation value required to apply Radiation I. Values below this do not apply effects.";
        static String radiationLevel2 = "Minimum radiation value required to apply Radiation II. Should be greater than radiation_level_1.";
        static String radiationLevel3 = "Minimum radiation value required to apply Radiation III. Should be greater than radiation_level_2.";
        static String amplifierLevel0 = "Effect amplifier for Radiation I (0 = level I).";
        static String amplifierLevel1 = "Effect amplifier for Radiation II (1 = level II).";
        static String amplifierLevel2 = "Effect amplifier for Radiation III (2 = level III).";
        static String list = "Lists related to radiation configuration, for example the entity blacklist.";
        static String blackListEntity =
                "List of entity IDs excluded from radiation effects. Use registry names like \"minecraft:armor_stand\".\n\n"
                        + "How to edit:\n"
                        + "- Add entries as namespace:entity_name strings.\n"
                        + "- Example: \"minecraft:armor_stand\"\n"
                        + "- Remove an entry to stop excluding it.\n";
    }

    public static class ConfiguredLists extends ConfigBase {
        private static ConfigValue<List<? extends String>> ENTITY_BLACKLIST = null;

        @Override
        public void registerAll(ForgeConfigSpec.Builder builder) {

            ENTITY_BLACKLIST = builder
                    .comment(Comments.blackListEntity)
                    .defineListAllowEmpty(
                            List.of("entity_blacklist"),
                            () -> List.of(
                                    "minecraft:armor_stand",
                                    "minecraft:item_frame",
                                    "minecraft:glow_item_frame"
                            ),
                            obj -> obj instanceof String
                    );
        }

        @Override
        public String getName() {
            return "lists";
        }

        public List<? extends String> getEntityBlackList() {
            return ENTITY_BLACKLIST.get();
        }
    }
}