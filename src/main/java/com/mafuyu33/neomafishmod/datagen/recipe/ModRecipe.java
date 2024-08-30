package com.mafuyu33.neomafishmod.datagen.recipe;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModRecipe extends RecipeProvider {

    public ModRecipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RUYIJINGU.get())
                .pattern("  C")
                .pattern(" B ")
                .pattern("A  ")
                .define('A', Items.BLAZE_ROD)
                .define('B', Items.STICK)
                .define('C', Items.BREEZE_ROD)
                .unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD))
                .unlockedBy("has_stick", has(Items.STICK))
                .unlockedBy("has_breeze_rod", has(Items.BREEZE_ROD))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BREAD_SWORD.get())
                .pattern("B")
                .pattern("B")
                .pattern("S")
                .define('B',Items.BREAD)
                .define('S',Items.STICK)
                .unlockedBy("has_ruby",has(Items.BREAD))
                .save(recipeOutput);

        cookRecipes(recipeOutput, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 100);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.STONE_BALL.get())
                .requires(Items.SNOWBALL)
                .requires(Items.COBBLESTONE)
                .unlockedBy("has_snowball",has(Items.SNOWBALL))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.TNT_BALL.get())
                .requires(Items.SNOWBALL)
                .requires(Items.TNT)
                .unlockedBy("has_snowball",has(Items.TNT))
                .save(recipeOutput);

    }

    protected static <T extends AbstractCookingRecipe> void cookRecipes(
            RecipeOutput recipeOutput, String cookingMethod, RecipeSerializer<T> cookingSerializer, AbstractCookingRecipe.Factory<T> recipeFactory, int cookingTime
    ) {
        simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.BREAD_SWORD, ModItems.BREAD_SWORD_HOT, 0.35F);
        simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.BREAD_SWORD_HOT, ModItems.BREAD_SWORD_VERY_HOT, 0.35F);
    }

    protected static <T extends AbstractCookingRecipe> void simpleCookingRecipe(
            RecipeOutput recipeOutput,
            String cookingMethod,
            RecipeSerializer<T> cookingSerializer,
            AbstractCookingRecipe.Factory<T> recipeFactory,
            int cookingTime,
            ItemLike material,
            ItemLike result,
            float experience
    ) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material), RecipeCategory.FOOD, result, experience, cookingTime, cookingSerializer, recipeFactory)
                .unlockedBy(getHasName(material), has(material))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,getItemName(result) + "_from_" + cookingMethod));
    }
}
