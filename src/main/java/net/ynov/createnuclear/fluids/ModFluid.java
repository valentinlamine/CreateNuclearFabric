package net.ynov.createnuclear.fluids;

import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.ynov.createnuclear.CreateNuclear;

public class ModFluid {

    public static FluidEntry<SimpleFlowableFluid.Flowing> URANIUM;

    public static void register() {
        var uraniumFluid = CreateNuclear.REGISTRATE.fluid("uranium", new ResourceLocation("createnuclear", "fluid/liquid_uranium_still"), new ResourceLocation("createnuclear", "fluid/liquid_uranium_flow"))
                .fluidProperties(p -> p.levelDecreasePerBlock(2)
                        .tickRate(15)
                        .flowSpeed(6)
                        .blastResistance(100f))
                .source(SimpleFlowableFluid.Source::new);
        var uraniumBucket = uraniumFluid.bucket()
                .properties(p -> p.stacksTo(1))
                .register();
        URANIUM = uraniumFluid.register();
    }

}
