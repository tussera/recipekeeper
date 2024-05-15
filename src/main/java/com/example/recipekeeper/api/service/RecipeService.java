package com.example.recipekeeper.api.service;

import com.example.recipekeeper.api.SearchRecipeFilter;
import com.example.recipekeeper.api.domain.RecipeRequest;
import com.example.recipekeeper.api.domain.SearchRecipeResponse;
import com.example.recipekeeper.api.mapper.RecipeMapper;
import com.example.recipekeeper.exception.RecipeNotFoundException;
import com.example.recipekeeper.persistence.model.RecipeEntity;
import com.example.recipekeeper.persistence.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeEntity createRecipe(RecipeRequest request) {
        return recipeRepository.save(RecipeMapper.recipeToEntity(request));
    }

    public RecipeEntity updateRecipe(long recipeId, RecipeRequest request) {
        var recipe = recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
        RecipeMapper.updateRequestToEntity(recipe, request);
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(long recipeId) {
        recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
        recipeRepository.deleteById(recipeId);
    }

    public SearchRecipeResponse searchRecipes(SearchRecipeFilter filter) {
        var recipes = recipeRepository.searchRecipes(
                filter.getIsVegetarian(),
                filter.getNumberOfServings(),
                filter.getKeywords() != null ? "%" + filter.getKeywords() + "%" : null, // added to use properly with like clause
                filter.getIncludedIngredients() != null ? filter.getIncludedIngredients() : List.of(),
                filter.getIncludedIngredients() != null ? filter.getIncludedIngredients().size() : 0,
                filter.getExcludedIngredients() != null ? filter.getExcludedIngredients() : List.of(),
                filter.getExcludedIngredients() != null ? filter.getExcludedIngredients().size() : 0
        );
        return SearchRecipeResponse.builder()
                .recipes(recipes)
                .build();
    }
}
