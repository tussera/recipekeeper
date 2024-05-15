package com.example.recipekeeper.api;

import com.example.recipekeeper.api.domain.RecipeRequest;
import com.example.recipekeeper.api.domain.SearchRecipeResponse;
import com.example.recipekeeper.persistence.model.RecipeEntity;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @Operation(description = "Create a new Recipe")
    public ResponseEntity<RecipeEntity> createRecipe(@Valid @RequestBody RecipeRequest request) {
        var recipe = recipeService.createRecipe(request);
        log.debug("Recipe created -> " + recipe);
        return ResponseEntity.ok(recipe);
    }

    @PutMapping("/{recipeId}")
    @Operation(description = "Update an existent Recipe")
    public ResponseEntity<RecipeEntity> updateRecipe(
            @PathVariable long recipeId,
            @Valid @RequestBody RecipeRequest request
    ) {
        var recipe = recipeService.updateRecipe(recipeId, request);
        log.debug("Recipe updated -> " + recipe);
        return ResponseEntity.ok(recipe);
    }

    @DeleteMapping("/{recipeId}")
    @Operation(description = "Delete an existent Recipe")
    public ResponseEntity<?> deleteRecipe(@PathVariable long recipeId) {
        recipeService.deleteRecipe(recipeId);
        log.debug("Recipe deleted -> ID=" + recipeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    @Operation(description = "Search recipes using the available filters")
    public ResponseEntity<SearchRecipeResponse> searchRecipes(
            @ParameterObject RecipeSearchFilter filter
    ) {
        var recipes = recipeService.searchRecipes(filter);
        log.debug("Recipes found -> " + recipes);
        return ResponseEntity.ok(recipes);
    }
}
