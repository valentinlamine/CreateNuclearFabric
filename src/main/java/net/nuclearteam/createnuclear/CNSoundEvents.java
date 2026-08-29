package net.nuclearteam.createnuclear;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllSoundEvents.ConfiguredSoundEvent;
import com.simibubi.create.AllSoundEvents.SoundEntry;
import com.simibubi.create.Create;
import com.tterrag.registrate.fabric.RegistryObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CNSoundEvents {

    public static final Map<ResourceLocation, SoundEntry> ALL = new HashMap<>();

    public static final SoundEntry

            REACTOR_ACTIVATION = create("reacteur/activation")
            .subtitle("Reactor Activation")
            .category(SoundSource.BLOCKS)
            .build(),

    REACTOR_RUNNING = create("reacteur/running")
            .subtitle("Reactor Running")
            .category(SoundSource.BLOCKS)
            .build(),

    REACTOR_SHUT_OFF = create("reacteur/shut_off")
            .subtitle("Reactor Shut Off")
            .category(SoundSource.BLOCKS)
            .build(),

    REACTOR_ALARM_ONESHOT = create("alarm/reactor", "alarm")
            .subtitle("Reactor Alarm")
            .category(SoundSource.BLOCKS)
            .build(),

    REACTOR_ALARM_LOOP = create("alarm/alarm")
            .subtitle("Reactor Alarm Loop")
            .category(SoundSource.BLOCKS)
            .build(),

    NUCLEAR_EXPLOSION = create("explosion/nuclear_explosion")
            .subtitle("Nuclear Explosion")
            .category(SoundSource.AMBIENT)
            .build(),

    NUCLEAR_EXPLOSION_LARGE = create("explosion/large_nuclear_explosion")
            .subtitle("Large Nuclear Explosion")
            .category(SoundSource.AMBIENT)
            .build(),

    NUCLEAR_EXPLOSION_RINGING = create("explosion/ringing")
            .subtitle("Nuclear Explosion Ringing")
            .category(SoundSource.AMBIENT)
            .build(),

    NUCLEAR_EXPLOSION_RUMBLE = create("explosion/nuclear_explosion_rumble")
            .subtitle("Nuclear Explosion Rumble")
            .category(SoundSource.AMBIENT)
            .build(),

    MOTOR_ASSEMBLE = create("reacteur/assemble_deassemble/motor_assemble")
            .subtitle("Motor Assemble")
            .category(SoundSource.BLOCKS)
            .build(),

    MOTOR_DISASSEMBLE = create("reacteur/assemble_deassemble/motor_disassemble")
            .subtitle("Motor Disassemble")
            .category(SoundSource.BLOCKS)
            .build(),

    NUCLEAR_EXPLOSION_RUMBLE_2 = create("explosion/rumble")
            .subtitle("Nuclear Explosion Rumble")
            .category(SoundSource.AMBIENT)
            .build(),

    NUCLEAR_EXPLOSION_MAIN = create("explosion/main")
            .subtitle("Nuclear Explosion Main")
            .category(SoundSource.AMBIENT)
            .build(),

    NUCLEAR_EXPLOSION_SHOCKWAVE = create("explosion/shockwave")
            .subtitle("Nuclear Explosion Shockwave")
            .category(SoundSource.AMBIENT)
            .build(),

    GEIGER_HIGH = create("geiger/high")
            .subtitle("Geiger High")
            .category(SoundSource.AMBIENT)
            .build(),

    GEIGER_LOW = create("geiger/low")
            .subtitle("Geiger Low")
            .category(SoundSource.AMBIENT)
            .build(),

    GEIGER_MEDIUM = create("geiger/medium")
            .subtitle("Geiger Medium")
            .category(SoundSource.AMBIENT)
            .build(),

    BIOME_WASTELAND = create("biomes/wasteland")
            .subtitle("Wasteland")
            .category(SoundSource.AMBIENT)
            .build()
            ;

    private static SoundEntryBuilder create(String... pathParts) {
        String name = String.join("_", pathParts);
        return create(CreateNuclear.asResource(name));
    }

    private static SoundEntryBuilder create(ResourceLocation id) {
        return new SoundEntryBuilder(id);
    }

    public static void prepare() {
        for (SoundEntry entry : ALL.values()) entry.prepare();
    }

    public static void register() {
        for (SoundEntry entry : ALL.values()) entry.register();
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (SoundEntry entry : ALL.values())
            if (entry.hasSubtitle())
                consumer.accept(entry.getSubtitleKey(), entry.getSubtitle());
    }

    public static SoundEntryProvider provider(FabricDataOutput output) {
        return new SoundEntryProvider(output);
    }

    public static void playItemPickup(Player player) {
        player.level()
                .playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f,
                        1f + player.level().random.nextFloat());
    }

    public static class SoundEntryProvider implements DataProvider {

        private PackOutput output;

        public SoundEntryProvider(PackOutput output) {
            this.output = output;
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cache) {
            return generate(output.getOutputFolder(), cache);
        }

        @Override
        public String getName() {
            return "CreateNuclear's Custom Sounds";
        }

        public CompletableFuture<?> generate(Path path, CachedOutput cache) {
            path = path.resolve("assets/createnuclear");
            JsonObject json = new JsonObject();
            ALL.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        entry.getValue()
                                .write(json);
                    });
            return DataProvider.saveStable(cache, json, path.resolve("sounds.json"));
        }

    }

    public static class SoundEntryBuilder {

        protected ResourceLocation id;
        protected String subtitle = "unregistered";
        protected SoundSource category = SoundSource.BLOCKS;
        protected List<ConfiguredSoundEvent> wrappedEvents;
        protected List<ResourceLocation> variants;
        protected int attenuationDistance;

        public SoundEntryBuilder(ResourceLocation id) {
            wrappedEvents = new ArrayList<>();
            variants = new ArrayList<>();
            this.id = id;
        }

        public SoundEntryBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public SoundEntryBuilder attenuationDistance(int distance) {
            this.attenuationDistance = distance;
            return this;
        }

        public SoundEntryBuilder noSubtitle() {
            this.subtitle = null;
            return this;
        }

        public SoundEntryBuilder category(SoundSource category) {
            this.category = category;
            return this;
        }

        public SoundEntryBuilder addVariant(String name) {
            return addVariant(Create.asResource(name));
        }

        public SoundEntryBuilder addVariant(ResourceLocation id) {
            variants.add(id);
            return this;
        }

        public SoundEntryBuilder playExisting(Supplier<SoundEvent> event, float volume, float pitch) {
            wrappedEvents.add(new ConfiguredSoundEvent(event, volume, pitch));
            return this;
        }

        public SoundEntryBuilder playExisting(SoundEvent event, float volume, float pitch) {
            return playExisting(() -> event, volume, pitch);
        }

        public SoundEntryBuilder playExisting(SoundEvent event) {
            return playExisting(event, 1, 1);
        }

        public SoundEntryBuilder playExisting(Holder<SoundEvent> event) {
            return playExisting(event::value, 1, 1);
        }

        public SoundEntry build() {
            SoundEntry entry =
                    wrappedEvents.isEmpty() ? new CustomSoundEntry(id, variants, subtitle, category, attenuationDistance)
                            : new WrappedSoundEntry(id, subtitle, wrappedEvents, category, attenuationDistance);
            ALL.put(entry.getId(), entry);
            return entry;
        }

    }

    private static class CustomSoundEntry extends SoundEntry {

        protected List<ResourceLocation> variants;
        protected RegistryObject<SoundEvent> event;

        public CustomSoundEntry(ResourceLocation id, List<ResourceLocation> variants, String subtitle,
                                SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.variants = variants;
        }

        @Override
        public void prepare() {
        }

        @Override
        public void register() {
            SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(id);
            Registry.register(BuiltInRegistries.SOUND_EVENT, id, soundEvent);
            event = RegistryObject.of(id, BuiltInRegistries.SOUND_EVENT);
        }

        @Override
        public SoundEvent getMainEvent() {
            return event.get();
        }

        @Override
        public void write(JsonObject json) {
            JsonObject entry = new JsonObject();
            JsonArray list = new JsonArray();

            JsonObject s = new JsonObject();
            s.addProperty("name", id.toString());
            s.addProperty("type", "file");
            if (attenuationDistance != 0)
                s.addProperty("attenuation_distance", attenuationDistance);
            list.add(s);

            for (ResourceLocation variant : variants) {
                s = new JsonObject();
                s.addProperty("name", variant.toString());
                s.addProperty("type", "file");
                if (attenuationDistance != 0)
                    s.addProperty("attenuation_distance", attenuationDistance);
                list.add(s);
            }

            entry.add("sounds", list);
            if (hasSubtitle())
                entry.addProperty("subtitle", getSubtitleKey());
            json.add(id.getPath(), entry);
        }

        @Override
        public void play(Level world, Player entity, double x, double y, double z, float volume, float pitch) {
            world.playSound(entity, x, y, z, event.get(), category, volume, pitch);
        }

        @Override
        public void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade) {
            world.playLocalSound(x, y, z, event.get(), category, volume, pitch, fade);
        }

    }

    private static class WrappedSoundEntry extends SoundEntry {

        private List<ConfiguredSoundEvent> wrappedEvents;
        private List<WrappedSoundEntry.CompiledSoundEvent> compiledEvents;

        public WrappedSoundEntry(ResourceLocation id, String subtitle,
                                 List<ConfiguredSoundEvent> wrappedEvents, SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.wrappedEvents = wrappedEvents;
            compiledEvents = new ArrayList<>();
        }

        @Override
        public void prepare() {
        }

        @Override
        public void register() {
            for (int i = 0; i < wrappedEvents.size(); i++) {
                ConfiguredSoundEvent wrapped = wrappedEvents.get(i);
                ResourceLocation location = getIdOf(i);
                SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(location);
                Registry.register(BuiltInRegistries.SOUND_EVENT, location, soundEvent);
                RegistryObject<SoundEvent> event = RegistryObject.of(location, BuiltInRegistries.SOUND_EVENT);
                compiledEvents.add(new WrappedSoundEntry.CompiledSoundEvent(event, wrapped.volume(), wrapped.pitch()));
            }
        }

        @Override
        public SoundEvent getMainEvent() {
            return compiledEvents.get(0)
                    .event().get();
        }

        protected ResourceLocation getIdOf(int i) {
            return new ResourceLocation(id.getNamespace(), i == 0 ? id.getPath() : id.getPath() + "_compounded_" + i);
        }

        @Override
        public void write(JsonObject json) {
            for (int i = 0; i < wrappedEvents.size(); i++) {
                ConfiguredSoundEvent event = wrappedEvents.get(i);
                JsonObject entry = new JsonObject();
                JsonArray list = new JsonArray();
                JsonObject s = new JsonObject();
                s.addProperty("name", event.event()
                        .get()
                        .getLocation()
                        .toString());
                s.addProperty("type", "event");
                if (attenuationDistance != 0)
                    s.addProperty("attenuation_distance", attenuationDistance);
                list.add(s);
                entry.add("sounds", list);
                if (i == 0 && hasSubtitle())
                    entry.addProperty("subtitle", getSubtitleKey());
                json.add(getIdOf(i).getPath(), entry);
            }
        }

        @Override
        public void play(Level world, Player entity, double x, double y, double z, float volume, float pitch) {
            for (WrappedSoundEntry.CompiledSoundEvent event : compiledEvents) {
                world.playSound(entity, x, y, z, event.event().get(), category, event.volume() * volume,
                        event.pitch() * pitch);
            }
        }

        @Override
        public void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade) {
            for (WrappedSoundEntry.CompiledSoundEvent event : compiledEvents) {
                world.playLocalSound(x, y, z, event.event().get(), category, event.volume() * volume,
                        event.pitch() * pitch, fade);
            }
        }

        private record CompiledSoundEvent(RegistryObject<SoundEvent> event, float volume, float pitch) {
        }

    }

}
