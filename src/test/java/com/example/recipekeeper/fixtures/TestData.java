package com.example.recipekeeper.fixtures;

import com.example.recipekeeper.api.domain.Ingredient;
import com.example.recipekeeper.api.domain.RecipeRequest;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TestData {

    public static RecipeRequest createSpaghettiCarbonara() {
        return RecipeRequest.builder()
                .title("Spaghetti Carbonara")
                .numberOfServings(4)
                .isVegetarian(false)
                .instructions("Put a large saucepan of water on to boil..")
                .ingredients(
                        List.of(
                                Ingredient.builder().name("spaghetti").build(),
                                Ingredient.builder().name("parmesan").build(),
                                Ingredient.builder().name("eggs").build(),
                                Ingredient.builder().name("garlic").build(),
                                Ingredient.builder().name("bacon").build(),
                                Ingredient.builder().name("salt").build()
                        )
                )
                .build();
    }

    public static RecipeRequest createFishAndChips() {
        return RecipeRequest.builder()
                .title("Fish & Chips")
                .numberOfServings(2)
                .isVegetarian(false)
                .instructions("Gather the ingredients and to the magic..")
                .ingredients(
                        List.of(
                                Ingredient.builder().name("fish").build(),
                                Ingredient.builder().name("potatoes").build(),
                                Ingredient.builder().name("pepper").build(),
                                Ingredient.builder().name("salt").build()
                        )
                )
                .build();
    }

    public static RecipeRequest createVegetarianCreamyBroccoliPasta() {
        return RecipeRequest.builder()
                .title("Creamy Broccoli Pasta")
                .numberOfServings(5)
                .isVegetarian(true)
                .instructions("Put all together and tada..")
                .ingredients(
                        List.of(
                                Ingredient.builder().name("broccoli").build(),
                                Ingredient.builder().name("lemon").build(),
                                Ingredient.builder().name("eggs").build(),
                                Ingredient.builder().name("rigatoni").build(),
                                Ingredient.builder().name("garlic").build(),
                                Ingredient.builder().name("salt").build()
                        )
                )
                .build();
    }
}
