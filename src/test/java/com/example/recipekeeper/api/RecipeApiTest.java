package com.example.recipekeeper.api;

import com.example.recipekeeper.fixtures.TestData;
import com.example.recipekeeper.persistence.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeApiTest implements WithAssertions {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        recipeRepository.deleteAll();
    }

    @Nested
    class CreateRecipe {
        @Test
        public void shouldCreateRecipeSuccessfully() throws Exception {
            // given the request provided
            var request = TestData.createSpaghettiCarbonara();

            // then create a new recipe and check the results
            mockMvc.perform(
                            post("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(request.title()))
                    .andExpect(jsonPath("$.numberOfServings").value(request.numberOfServings()))
                    .andExpect(jsonPath("$.ingredients", hasSize(request.ingredients().size())))
                    .andExpect(jsonPath("$.instructions").value(request.instructions()));
        }

        @Test
        public void shouldThrowInternalServerErrorWhenUnexpectedErrorOccurs() throws Exception {
            mockMvc.perform(
                            post("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print()).andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class UpdateRecipe {
        @Test
        public void shouldUpdateAnExistentRecipeSuccessfully() throws Exception {
            // given the request provided
            var createRequest = TestData.createFishAndChips();

            // then create a new recipe
            mockMvc.perform(
                            post("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(createRequest))
                    )
                    .andDo(print()).andExpect(status().isOk());

            // and fetch the new recipe so it can be updated
            var recipeFromDB = recipeRepository.findByTitle(createRequest.title()).orElseThrow();

            var updateRequest = TestData.createVegetarianCreamyBroccoliPasta();

            mockMvc.perform(
                            put("/api/recipe/{recipeId}", recipeFromDB.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(updateRequest))
                    )
                    .andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(recipeFromDB.getId()))
                    .andExpect(jsonPath("$.title").value(updateRequest.title()))
                    .andExpect(jsonPath("$.numberOfServings").value(updateRequest.numberOfServings()))
                    .andExpect(jsonPath("$.ingredients", hasSize(updateRequest.ingredients().size())))
                    .andExpect(jsonPath("$.instructions").value(updateRequest.instructions()));
        }

        @Test
        public void shouldReturnNotFoundWhenUpdatingNotExistentRecipe() throws Exception {
            // given the not existent recipe ID
            var recipeId = 1;
            var updateRequest = TestData.createVegetarianCreamyBroccoliPasta();

            // when trying to update the recipe then NOT FOUND is returned
            mockMvc.perform(
                            put("/api/recipe/{recipeId}", recipeId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(updateRequest))
                    )
                    .andDo(print()).andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteRecipe {
        @Test
        public void shouldDeleteAnExistentRecipeSuccessfully() throws Exception {
            // given the request provided
            var createRequest = TestData.createVegetarianCreamyBroccoliPasta();

            // then create a new recipe
            mockMvc.perform(
                            post("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(createRequest))
                    )
                    .andDo(print()).andExpect(status().isOk());

            // and fetch the new recipe so it can be deleted
            var recipeFromDB = recipeRepository.findByTitle(createRequest.title()).orElseThrow();

            mockMvc.perform(
                            delete("/api/recipe/{recipeId}", recipeFromDB.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print()).andExpect(status().isOk());

            assertThat(recipeRepository.findById(recipeFromDB.getId())).isEmpty();
        }

        @Test
        public void shouldReturnNotFoundWhenDeletingNotExistentRecipe() throws Exception {
            // given the not existent recipe ID
            var recipeId = 1;

            // when trying to delete the recipe then NOT FOUND is returned
            mockMvc.perform(
                            delete("/api/recipe/{recipeId}", recipeId)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print()).andExpect(status().isNotFound());
        }
    }

    @Nested
    class SearchRecipe {
        @Test
        public void shouldReturnAllRecipesWhenNotUsingFilters() throws Exception {
            // given some test data
            insertTestData();

            // all recipes are returned when searching recipes without filtering
            mockMvc.perform(
                    get("/api/recipe")
                            .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.recipes", hasSize(3)))
                    .andExpect(jsonPath("$.recipes[0].title", is("Fish & Chips")))
                    .andExpect(jsonPath("$.recipes[1].title", is("Spaghetti Carbonara")))
                    .andExpect(jsonPath("$.recipes[2].title", is("Creamy Broccoli Pasta")));
        }

        @Test
        public void shouldReturnOnlyVegetarianRecipes() throws Exception {
            // given some test data
            insertTestData();

            // only vegetarian recipes are returned when using vegetarian filter
            mockMvc.perform(
                            get("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("isVegetarian", "true")
                    ).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.recipes", hasSize(1)))
                    .andExpect(jsonPath("$.recipes[0].title", is("Creamy Broccoli Pasta")));
        }

        @Test
        public void shouldReturnOnlyRecipesBasedOnNumberOfServings() throws Exception {
            // given some test data
            insertTestData();

            // only vegetarian recipes are returned when using vegetarian filter
            mockMvc.perform(
                            get("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("numberOfServings", "2")
                    ).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.recipes", hasSize(1)))
                    .andExpect(jsonPath("$.recipes[0].title", is("Fish & Chips")));
        }

        @Test
        public void shouldReturnOnlyRecipesBasedOnInstructionsText() throws Exception {
            // given some test data
            insertTestData();

            // only recipes with specific text in the instructions are returned
            mockMvc.perform(
                            get("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("text", "Put all together")
                    ).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.recipes", hasSize(1)))
                    .andExpect(jsonPath("$.recipes[0].title", is("Creamy Broccoli Pasta")));
        }

        @Test
        public void shouldReturnOnlyRecipesBasedOnIncludedAndExcludedIngredients() throws Exception {
            // given some test data
            insertTestData();

            // only recipes based on ingredient filters are returned
            mockMvc.perform(
                            get("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("includedIngredients", "eggs", "garlic")
                                    .param("excludedIngredients", "cheese", "broccoli")
                    ).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.recipes", hasSize(1)))
                    .andExpect(jsonPath("$.recipes[0].title", is("Spaghetti Carbonara")));
        }

        @Test
        public void shouldReturnOnlyRecipesBasedOnMultipleFilters() throws Exception {
            // given some test data
            insertTestData();

            // only recipes based on all filters are returned
            mockMvc.perform(
                            get("/api/recipe")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("isVegetarian", "false")
                                    .param("numberOfServings", "2")
                                    .param("text", "magic")
                                    .param("includedIngredients", "potatoes")
                                    .param("excludedIngredients", "meat", "chicken")
                    ).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.recipes", hasSize(1)))
                    .andExpect(jsonPath("$.recipes[0].title", is("Fish & Chips")));
        }

    }

    private void insertTestData() {
        recipeRepository.save(RecipeMapper.recipeToEntity(TestData.createFishAndChips()));
        recipeRepository.save(RecipeMapper.recipeToEntity(TestData.createSpaghettiCarbonara()));
        recipeRepository.save(RecipeMapper.recipeToEntity(TestData.createVegetarianCreamyBroccoliPasta()));
    }
}