package net.nuclearteam.createnuclear;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

import java.rmi.registry.Registry;

public class CNSounds {

    public static final LazyRegistrar<SoundEvent> SOUND_EVENTS = LazyRegistrar.create(BuiltInRegistries.SOUND_EVENT, CreateNuclear.MOD_ID);

    public static final RegistryObject<SoundEvent> REACTOR_CASING_BREAK = registerSoundEvent("reactor_casing_break");
    public static final RegistryObject<SoundEvent> REACTOR_CASING_STEP = registerSoundEvent("reactor_casing_step");
    public static final RegistryObject<SoundEvent> REACTOR_CASING_PLACE = registerSoundEvent("reactor_casing_place");
    public static final RegistryObject<SoundEvent> REACTOR_CASING_HIT = registerSoundEvent("reactor_casing_hit");
    public static final RegistryObject<SoundEvent> REACTOR_CASING_FALL = registerSoundEvent("reactor_casing_fall");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CreateNuclear.MOD_ID, name)));
    }
    public static void register() {
        SOUND_EVENTS.register();
    }
    public static SoundType getSoundType(String id) {
        return new SoundType(1.0F, 1.0F,
                SoundEvent.createVariableRangeEvent(new ResourceLocation(CreateNuclear.MOD_ID, id + "_break")),
                SoundEvent.createVariableRangeEvent(new ResourceLocation(CreateNuclear.MOD_ID, id + "_step")),
                SoundEvent.createVariableRangeEvent(new ResourceLocation(CreateNuclear.MOD_ID, id + "_place")),
                SoundEvent.createVariableRangeEvent(new ResourceLocation(CreateNuclear.MOD_ID, id + "_hit")),
                SoundEvent.createVariableRangeEvent(new ResourceLocation(CreateNuclear.MOD_ID, id + "_fall")));
    }


}