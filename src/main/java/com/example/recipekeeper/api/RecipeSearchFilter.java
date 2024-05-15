package com.example.recipekeeper.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RecipeSearchFilter {
    @Null
    @Schema(description = "Indicate if the recipe is vegetarian")
    private Boolean isVegetarian;

    @Null
    @Schema(description = "Number of Servings")
    private Integer numberOfServings;

    @Null
    @Schema(description = "Only filter recipes that contains these ingredients")
    private List<String> includedIngredients;

    @Null
    @Schema(description = "Only filter recipes that doesn't contains these ingredients")
    private List<String> excludedIngredients;

    @Null
    @Schema(description = "Used in the instructions text search")
    private String text;
}
