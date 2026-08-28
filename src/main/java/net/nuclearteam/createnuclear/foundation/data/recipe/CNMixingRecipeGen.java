package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Items;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.CNFluids;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CreateNuclear;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
public class CNMixingRecipeGen extends MixingRecipeGen {

    GeneratedRecipe
        STEEL = create("steel", b -> b
            .require(CNTags.forgeItemTag("dusts/coal"))
            .require(Tags.Items.INGOTS_IRON)
            .output(CNItems.STEEL_INGOT)
        ),

        URANIUM_FLUID = create("uranium_fluid", b -> b
            .require(CNTags.forgeItemTag("dusts/uranium"))
            .output(CNFluids.URANIUM.get(), 2025)
        ),

        THORIUM_FLUID = create("thorium_fluid", b -> b
            .require(CNTags.forgeItemTag("dusts/thorium"))
            .output(CNFluids.THORIUM.get(), 2025)
            .requiresHeat(HeatCondition.HEATED)
        ),

        NITROGEN = create("liquid_nitrogen", b -> b
            .require(CNItems.COOLED_NITROGEN_CONCENTRATE)
            .duration(20)
            .require(Items.ICE)
            .output(CNFluids.LIQUID_NITROGEN.get(), 8100));

    public CNMixingRecipeGen(FabricDataOutput output) {
        super(output, CreateNuclear.MOD_ID);
    }

    @Override
    public String getName() {
        return "CreateNuclear's Processing Recipes: " + getRecipeType().getId().getPath();
    }
}
