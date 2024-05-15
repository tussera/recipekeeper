package com.example.recipekeeper.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record RecipeRequest(
        @Valid
        @NotNull
        @Schema(description = "Recipe Title", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,
        @Schema(description = "Number of Servings")
        int numberOfServings,
        @Schema(description = "Is this recipe Vegetarian?")
        boolean isVegetarian,
        @Schema(description = "List of Ingredients")
        List<Ingredient> ingredients,
        @Valid
        @NotNull
        @Schema(description = "Detailed instructions")
        String instructions
) { }
