package com.example.recipekeeper.persistence.repository;

import com.example.recipekeeper.api.mapper.RecipeMapper;
import com.example.recipekeeper.fixtures.TestData;
import com.example.recipekeeper.persistence.model.RecipeEntity;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class RecipeRepositoryTest implements WithAssertions {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveRecipeSuccessfully() {
        var recipe = RecipeMapper.recipeToEntity(TestData.createFishAndChips());
        var insertedRecipe = recipeRepository.save(recipe);
        assertThat(entityManager.find(RecipeEntity.class, insertedRecipe.getId()) ).isEqualTo(recipe);
    }

}