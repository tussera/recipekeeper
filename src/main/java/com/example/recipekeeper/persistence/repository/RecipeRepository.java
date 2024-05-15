package com.example.recipekeeper.persistence.repository;

import com.example.recipekeeper.persistence.model.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    Optional<RecipeEntity> findByTitle(String title);

    @Query("""
            SELECT rec
            FROM RecipeEntity rec
            WHERE (:isVegetarian IS NULL OR rec.isVegetarian = :isVegetarian)
              AND (:numberOfServings IS NULL OR rec.numberOfServings = :numberOfServings)
              AND (:keywords IS NULL OR rec.instructions LIKE :keywords)
              AND (:includedIngredientsCount = 0 OR :includedIngredientsCount = (SELECT COUNT(*)
                                                                                 FROM RecipeEntity rec2
                                                                                 INNER JOIN rec2.ingredients ing2
                                                                                 WHERE ing2.name in :includedIngredients
                                                                                   AND rec = rec2))
              AND (:excludedIngredientsCount = 0 OR 1 > (SELECT COUNT(*)
                                                         FROM RecipeEntity rec2
                                                         INNER JOIN rec2.ingredients ing2
                                                         WHERE ing2.name in :excludedIngredients
                                                           AND rec = rec2))
                  
    """
    )
    List<RecipeEntity> searchRecipes(
            Boolean isVegetarian,
            Integer numberOfServings,
            String keywords,
            List<String> includedIngredients,
            Integer includedIngredientsCount,
            List<String> excludedIngredients,
            Integer excludedIngredientsCount
    );
}
