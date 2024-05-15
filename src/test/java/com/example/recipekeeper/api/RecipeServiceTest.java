package com.example.recipekeeper.api;

import com.example.recipekeeper.exception.RecipeNotFoundException;
import com.example.recipekeeper.fixtures.TestData;
import com.example.recipekeeper.persistence.RecipeRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest implements WithAssertions {

    private final RecipeRepository recipeRepository = Mockito.mock(RecipeRepository.class);

    private final RecipeService recipeService = new RecipeService(recipeRepository);

    @Test
    void shouldCreateRecipeSuccessfully() {
        // given
        var recipeRequest = TestData.createFishAndChips();
        var recipeEntity = RecipeMapper.recipeToEntity(recipeRequest);
        when(recipeRepository.save(any())).thenReturn(recipeEntity);

        // when
        var result = recipeService.createRecipe(recipeRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveAssertion().isEqualTo(recipeEntity);
    }

    @Test
    void shouldUpdateRecipeSuccessfully() {
        // given
        var recipeId = 1L;
        var recipeRequest = TestData.createSpaghettiCarbonara();
        var recipeEntity = RecipeMapper.recipeToEntity(recipeRequest);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.save(any())).thenReturn(recipeEntity);

        // when
        var result = recipeService.updateRecipe(recipeId, recipeRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveAssertion().isEqualTo(recipeEntity);
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistentRecipe() {
        // given
        var recipeId = 1L;
        var recipeRequest = TestData.createSpaghettiCarbonara();

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(RecipeNotFoundException.class)
                .isThrownBy(() -> recipeService.updateRecipe(recipeId, recipeRequest));
    }

    @Test
    void shouldDeleteRecipeSuccessfully() {
        // given
        var recipeId = 1L;
        var recipeRequest = TestData.createSpaghettiCarbonara();
        var recipeEntity = RecipeMapper.recipeToEntity(recipeRequest);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.save(any())).thenReturn(recipeEntity);

        // when
        recipeService.deleteRecipe(recipeId);

        verify(recipeRepository, times(1)).deleteById(any());
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistentRecipe() {
        // given
        var recipeId = 1L;

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(RecipeNotFoundException.class)
                .isThrownBy(() -> recipeService.deleteRecipe(recipeId));
    }

    @Test
    void shouldFindRecipesUsingFilters() {
        // given
        var isVegetarian = true;
        var numberOfServings = 5;
        var text = "together";
        var includedIngredients = List.of("eggs");
        var excludedIngredients = List.of("potatoes");
        var filter = new RecipeSearchFilter(isVegetarian, numberOfServings, includedIngredients, excludedIngredients, text);
        var recipeEntity = RecipeMapper.recipeToEntity(TestData.createFishAndChips());

        when(recipeRepository.searchRecipes(
                isVegetarian, numberOfServings, "%"+text+"%", includedIngredients, 1, excludedIngredients, 1
        )).thenReturn(List.of(recipeEntity));

        // when
        var result = recipeService.searchRecipes(filter);

        // then
        assertThat(result).isNotNull();
        assertThat(result.recipes()).hasSize(1);
        assertThat(result.recipes().getFirst())
                .usingRecursiveAssertion()
                .isEqualTo(recipeEntity);
    }


}