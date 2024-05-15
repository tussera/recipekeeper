package com.example.recipekeeper.api.domain;

import com.example.recipekeeper.persistence.model.RecipeEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record SearchRecipeResponse(List<RecipeEntity> recipes) { }
