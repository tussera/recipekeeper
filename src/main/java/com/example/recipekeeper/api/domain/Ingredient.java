package com.example.recipekeeper.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record Ingredient(
        @Valid
        @NotNull
        @Schema(description = "Ingredient Name", requiredMode = Schema.RequiredMode.REQUIRED)
        String name
) { }
