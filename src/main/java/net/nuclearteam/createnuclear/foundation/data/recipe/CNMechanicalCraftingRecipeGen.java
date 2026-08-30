package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.*;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
public class CNMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {

    GeneratedRecipe
        GRAPHITE_ROD = create(CNItems.GRAPHITE_ROD::get)
            .recipe(b -> b
                .key('S', Ingredient.of(CNTags.forgeItemTag("ingots/steel")))
                .key('G', Ingredient.of(CNItems.GRAPHENE))
                .patternLine("SGS")
                .patternLine("SGS")
                .patternLine("SGS")
                .patternLine("SGS")
            ),

        URANIUM_ROD = create(CNItems.URANIUM_ROD::get)
            .recipe(b -> b
                .key('U', Ingredient.of(CNItems.ENRICHED_YELLOWCAKE))
                    .patternLine("    U")
                    .patternLine("   U ")
                    .patternLine("  U  ")
                    .patternLine(" U   ")
                    .patternLine("U    ")
            ),

        THORIUM_ROD = create(CNItems.THORIUM_ROD::get)
            .recipe(b -> b
                .key('U', Ingredient.of(CNItems.THORIUM_INGOT))
                .patternLine("U    ")
                .patternLine(" U   ")
                .patternLine("  U  ")
                .patternLine("   U ")
                .patternLine("    U")
            ),

        REACTOR_FRAME = create(CNBlocks.REACTOR_FRAME::get)
            .recipe(b -> b
                .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
                .key('G', Ingredient.of(CNBlocks.REINFORCED_GLASS))
                .key('B', Ingredient.of(Items.BUCKET))
                .key('S', Ingredient.of(CNTags.forgeItemTag("ingots/steel")))
                .patternLine("CCCCC")
                .patternLine("CSGSC")
                .patternLine("CGBGC")
                .patternLine("CSGSC")
                .patternLine("CCCCC")
            ),

        REACTOR_CONTROLLER = create(CNBlocks.REACTOR_CONTROLLER::get)
            .recipe(b -> b
                .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
                .key('V', Ingredient.of(AllBlocks.ITEM_VAULT))
                .key('O', Ingredient.of(AllBlocks.SMART_OBSERVER))
                .key('T', Ingredient.of(AllItems.ELECTRON_TUBE))
                .key('N', Ingredient.of(Items.NETHERITE_INGOT))
                .key('X', Ingredient.of(Items.NETHER_STAR))
                .patternLine("CCCCC")
                .patternLine("CNONC")
                .patternLine("CTXTC")
                .patternLine("CNVNC")
                .patternLine("CCCCC")
            ),

        REACTOR_COOLER = create(CNBlocks.REACTOR_COOLER::get)
            .recipe(b -> b
                .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
                .key('I', Ingredient.of(Blocks.BLUE_ICE))
                .key('G', Ingredient.of(CNBlocks.REINFORCED_GLASS))
                .key('S', Ingredient.of(CNTags.forgeItemTag("ingots/steel")))
                .patternLine("CCCCC")
                .patternLine("CSGSC")
                .patternLine("CIGIC")
                .patternLine("CSGSC")
                .patternLine("CCCCC")
            ),

        REACTOR_CORE = create(CNBlocks.REACTOR_CORE::get)
            .recipe(b -> b
                .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
                .key('P', Ingredient.of(AllItems.PRECISION_MECHANISM))
                .key('B', Ingredient.of(CNFluids.URANIUM.get().getBucket()))
                .key('S', Ingredient.of(CNTags.forgeItemTag("ingots/steel")))
                .patternLine("CCCCC")
                .patternLine("CPSPC")
                .patternLine("CSBSC")
                .patternLine("CPSPC")
                .patternLine("CCCCC")
            ),

        REACTOR_ALARM = create(CNBlocks.REACTOR_ALARM::get)
            .recipe(b -> b
                .key('C', Ingredient.of(CNBlocks.REACTOR_CASING))
                .key('N', Ingredient.of(Blocks.NOTE_BLOCK))
                .key('R', Ingredient.of(Blocks.REPEATER))
                .key('L', Ingredient.of(Items.CLOCK))
                .patternLine("CCCCC")
                .patternLine("CNRNC")
                .patternLine("CRLRC")
                .patternLine("CNRNC")
                .patternLine("CCCCC")
            );



    public CNMechanicalCraftingRecipeGen(FabricDataOutput output) {
        super(output, CreateNuclear.MOD_ID);
    }

    @Override
    public String getName() {
        return "CreateNuclair's Mechanical Crafting Recipes";
    }
}
