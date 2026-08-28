package net.nuclearteam.createnuclear.api.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SmithingClothRecipeBuilder {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final RecipeCategory category;
    private final ItemStack result;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final RecipeSerializer<?> type;

    public SmithingClothRecipeBuilder(RecipeSerializer<?> pType, Ingredient pTemplate, Ingredient pBase, Ingredient pAddition, RecipeCategory pCategory, ItemStack pResult) {
        this.category = pCategory;
        this.type = pType;
        this.template = pTemplate;
        this.base = pBase;
        this.addition = pAddition;
        this.result = pResult;
    }

    public static SmithingClothRecipeBuilder smithingCloth(Ingredient pTemplate, Ingredient pBase, Ingredient pAddition, RecipeCategory pCategory, ItemStack pResult) {
        return new SmithingClothRecipeBuilder(RecipeSerializer.SMITHING_TRANSFORM, pTemplate, pBase, pAddition, pCategory, pResult);
    }

    public SmithingClothRecipeBuilder unlocks(String pKey, CriterionTriggerInstance pCriterion) {
        this.advancement.addCriterion(pKey, pCriterion);
        return this;
    }

    public void save(Consumer<FinishedRecipe> pRecipeConsumer, ResourceLocation pLocation) {
        this.ensureValid(pLocation);
        this.advancement.parent(Advancement.Builder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", new RecipeUnlockedTrigger.TriggerInstance(ContextAwarePredicate.ANY, pLocation)).rewards(AdvancementRewards.Builder.recipe(pLocation)).requirements(RequirementsStrategy.OR);
        pRecipeConsumer.accept(new SmithingClothRecipeBuilder.Result(pLocation, this.type, this.template, this.base, this.addition, this.result, this.advancement, pLocation.withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void ensureValid(ResourceLocation pLocation) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pLocation);
        }
    }

    public static record Result(ResourceLocation id, RecipeSerializer<?> type, Ingredient template, Ingredient base, Ingredient addition, ItemStack result, Advancement.Builder advancement, ResourceLocation advancementId) implements FinishedRecipe {
        public void serializeRecipeData(JsonObject jObject) {
            jObject.add("template", this.template.toJson());
            jObject.add("base", this.base.toJson());
            jObject.add("addition", this.addition.toJson());
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result.getItem()).toString());
            if (!this.result.getOrCreateTag().isEmpty() && this.result.getOrCreateTag().contains("Cloth")) {
                CompoundTag nbt = new CompoundTag();
                nbt.putString("Cloth", this.result.getOrCreateTag().getString("Cloth"));
                jsonobject.addProperty("nbt", nbt.toString());
            }
            jObject.add("result", jsonobject);
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return this.type;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.toJson();
        }

        /**
         * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson}
         * is non-null.
         */
        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}

