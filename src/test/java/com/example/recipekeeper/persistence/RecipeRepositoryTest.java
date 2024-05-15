package com.example.recipekeeper.persistence;

import com.example.recipekeeper.api.RecipeMapper;
import com.example.recipekeeper.fixtures.TestData;
import com.example.recipekeeper.persistence.model.IngredientEntity;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class RecipeRepositoryTest implements WithAssertions {

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        recipeRepository.deleteAll();
        recipeRepository.save(RecipeMapper.recipeToEntity(TestData.createFishAndChips()));
        recipeRepository.save(RecipeMapper.recipeToEntity(TestData.createSpaghettiCarbonara()));
        recipeRepository.save(RecipeMapper.recipeToEntity(TestData.createVegetarianCreamyBroccoliPasta()));
    }

    @Test
    void shouldFindRecipeByTitle() {
        var recipe = recipeRepository.findByTitle(TestData.createFishAndChips().title()).orElse(null);

        assert recipe != null;
        assertThat(recipe.getTitle()).isEqualTo(recipe.getTitle());
    }

    @Test
    void shouldFindOnlyVegetarianRecipes() {
        var isVegetarian = true;
        var recipeList = recipeRepository.searchRecipes(isVegetarian, null, null, null, 0, null, 0);
        assertThat(recipeList).hasSize(1);

        var recipe = recipeList.getFirst();
        assertThat(recipe.isVegetarian()).isEqualTo(isVegetarian);
    }

    @Test
    void shouldFindRecipesByNumberOfServings() {
        var numberOfServings = 4;
        var recipeList = recipeRepository.searchRecipes(null, numberOfServings, null, null, 0, null, 0);
        assertThat(recipeList).hasSize(1);

        var recipe = recipeList.getFirst();
        assertThat(recipe.getNumberOfServings()).isEqualTo(numberOfServings);
    }

    @Test
    void shouldFindRecipesByTextInInstructions() {
        var text = "magic";
        var recipeList = recipeRepository.searchRecipes(null, null, text, null, 0, null, 0);
        assertThat(recipeList).hasSize(1);

        var recipe = recipeList.getFirst();
        assertThat(recipe.getInstructions()).contains(text);
    }

    @Test
    void shouldFindRecipesByIngredients() {
        var includedIngredients = List.of("eggs");
        var excludedIngredients = List.of("potatoes");
        var recipeList = recipeRepository.searchRecipes(null, null, null, includedIngredients, includedIngredients.size(), excludedIngredients, excludedIngredients.size());
        assertThat(recipeList).hasSize(2);

        assertThat(recipeList).allSatisfy(recipe -> {
            var ingredients = recipe.getIngredients().stream().map(IngredientEntity::getName).toList();
            assertThat(ingredients).containsAll(includedIngredients);
            assertThat(ingredients).doesNotContain(String.valueOf(excludedIngredients));
        });
    }

    @Test
    void shouldFindRecipesUsingAvailableFilters() {
        var isVegetarian = true;
        var numberOfServings = 5;
        var text = "together";
        var includedIngredients = List.of("eggs");
        var excludedIngredients = List.of("potatoes");
        var recipeList = recipeRepository.searchRecipes(isVegetarian, numberOfServings, text, includedIngredients, includedIngredients.size(), excludedIngredients, excludedIngredients.size());
        assertThat(recipeList).hasSize(1);

        var recipe = recipeList.getFirst();
        assertThat(recipe.isVegetarian()).isEqualTo(isVegetarian);
        assertThat(recipe.getNumberOfServings()).isEqualTo(numberOfServings);
        assertThat(recipe.getInstructions()).contains(text);

        var ingredients = recipe.getIngredients().stream().map(IngredientEntity::getName).toList();
        assertThat(ingredients).containsAll(includedIngredients);
        assertThat(ingredients).doesNotContain(String.valueOf(excludedIngredients));
    }

}