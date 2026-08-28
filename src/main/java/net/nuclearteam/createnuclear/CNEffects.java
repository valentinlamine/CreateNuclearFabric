package net.nuclearteam.createnuclear;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;
import net.nuclearteam.createnuclear.CreateNuclear;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.nuclearteam.createnuclear.content.effects.IodineEffect;
import net.nuclearteam.createnuclear.content.radiation.RadiationEffect;


public class CNEffects {
    public static final LazyRegistrar<MobEffect> EFFECTS = LazyRegistrar.create(Registries.MOB_EFFECT, CreateNuclear.MOD_ID);
    public static final RegistryObject<MobEffect> RADIATION = EFFECTS.register("radiation", RadiationEffect::new);
    public static final RegistryObject<MobEffect> IODINE = EFFECTS.register("iodine", IodineEffect::new);

    public static void register() {
        EFFECTS.register();
    }

}
