package com.example.recipekeeper.api;

import com.example.recipekeeper.api.domain.Ingredient;
import com.example.recipekeeper.api.domain.RecipeRequest;
import com.example.recipekeeper.persistence.model.IngredientEntity;
import com.example.recipekeeper.persistence.model.RecipeEntity;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RecipeMapper {

    public static RecipeEntity recipeToEntity(RecipeRequest request) {
        var recipeEntity = RecipeEntity.builder()
                .title(request.title())
                .numberOfServings(request.numberOfServings())
                .isVegetarian(request.isVegetarian())
                .ingredients(ingredientToEntity(request.ingredients()))
                .instructions(request.instructions())
                .build();
        recipeEntity.getIngredients().forEach(ingredient -> ingredient.setRecipe(recipeEntity));
        return recipeEntity;
    }

    public static void updateRequestToEntity(RecipeEntity entity, RecipeRequest request) {
        entity.setTitle(request.title());
        entity.setNumberOfServings(request.numberOfServings());
        entity.setVegetarian(request.isVegetarian());
        entity.setInstructions(request.instructions());

        entity.getIngredients().clear();
        entity.getIngredients().addAll(ingredientToEntity(request.ingredients()));
        entity.getIngredients().forEach(ingredient -> ingredient.setRecipe(entity));
    }

    public static List<IngredientEntity> ingredientToEntity(List<Ingredient> ingredients) {
        return new ArrayList<>(
                ingredients.stream()
                        .map(ingredient -> IngredientEntity.builder()
                            .name(ingredient.name())
                            .build()
                        )
                        .toList()
        );
    }
}
