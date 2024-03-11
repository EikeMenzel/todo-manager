package org.databaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.databaseservice.payload.CategoryDTO;
import org.databaseservice.services.ICategoryService;
import org.databaseservice.services.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.registerWithEureka=false",
        "eureka.client.fetchRegistry=false"
})
@AutoConfigureMockMvc
class CategoryControllerTest {
    @MockBean
    private ICategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDTO validCategoryDTO;
    private Long validUserId = 1L;
    private Long validCategoryId = 1L;

    @BeforeEach
    void setUp() {
        validCategoryDTO = new CategoryDTO(validCategoryId, "Books");
    }

    @Test
    void createCategory_Success_ShouldReturnStatusCreated() throws Exception {
        doNothing().when(categoryService).saveCategory(eq(validUserId), any(CategoryDTO.class));

        mockMvc.perform(post("/api/v1/db/users/{userId}/categories", validUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new CategoryDTO(null, "Books"))))
                .andExpect(status().isCreated());
    }

    @Test
    void getCategories_Success_ShouldReturnCategories() throws Exception {
        List<CategoryDTO> categories = List.of(validCategoryDTO);
        when(categoryService.getCategories(validUserId)).thenReturn(categories);

        mockMvc.perform(get("/api/v1/db/users/{userId}/categories", validUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))); // Verify that the JSON array has 1 element
    }

    @Test
    void getCategories_NoContent_ShouldReturn204() throws Exception {
        when(categoryService.getCategories(validUserId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/db/users/{userId}/categories", validUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateCategory_Success_ShouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).updateCategory(validUserId, validCategoryId, validCategoryDTO);

        mockMvc.perform(put("/api/v1/db/users/{userId}/categories/{categoryId}", validUserId, validCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCategoryDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_Success_ShouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).deleteCategory(validUserId, validCategoryId);

        mockMvc.perform(delete("/api/v1/db/users/{userId}/categories/{categoryId}", validUserId, validCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
