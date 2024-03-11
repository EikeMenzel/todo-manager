package org.taskservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.taskservice.clients.IDatabaseServiceClient;
import org.taskservice.payload.CategoryDTO;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.registerWithEureka=false",
        "eureka.client.fetchRegistry=false"
})
@AutoConfigureMockMvc
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDatabaseServiceClient databaseServiceClient;
    @Test
    void testSaveCategorySuccessfully() throws Exception {
        Long userId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(null, "Books");

        Mockito.when(databaseServiceClient.createCategory(eq(userId), any(CategoryDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        mockMvc.perform(post("/api/v1/categories")
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated());

        ArgumentCaptor<CategoryDTO> categoryCaptor = ArgumentCaptor.forClass(CategoryDTO.class);
        Mockito.verify(databaseServiceClient).createCategory(eq(userId), categoryCaptor.capture());

        CategoryDTO capturedCategoryDTO = categoryCaptor.getValue();
        assertNull(capturedCategoryDTO.getId());
        assertEquals("Books", capturedCategoryDTO.getName());
    }

    @Test
    void testSaveCategoryWithInvalidUserId() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO(null, "Books");

        mockMvc.perform(post("/api/v1/categories")
                        .header("userId", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveCategoryWithInvalidCategoryDetails() throws Exception {
        Long userId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(null, ""); // Empty name

        mockMvc.perform(post("/api/v1/categories")
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCategoriesSuccessfully() throws Exception {
        Long userId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Books");
        ResponseEntity<List<CategoryDTO>> responseEntity = ResponseEntity.ok(Collections.singletonList(categoryDTO));

        Mockito.when(databaseServiceClient.getCategoryDTOList(userId))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/categories")
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(categoryDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(categoryDTO.getName()));

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(databaseServiceClient).getCategoryDTOList(userIdCaptor.capture());
        assertEquals(userId, userIdCaptor.getValue());
    }

    @Test
    void testUpdateCategorySuccessfully() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(null, "Updated Books");
        ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        Mockito.when(databaseServiceClient.updateCategory(eq(userId), eq(categoryId), any(CategoryDTO.class)))
                .thenReturn(responseEntity);

        mockMvc.perform(put("/api/v1/categories/{categoryId}", categoryId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isNoContent());

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryDTO> categoryCaptor = ArgumentCaptor.forClass(CategoryDTO.class);
        Mockito.verify(databaseServiceClient).updateCategory(userIdCaptor.capture(), categoryIdCaptor.capture(), categoryCaptor.capture());

        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(categoryId, categoryIdCaptor.getValue());
        assertEquals(categoryDTO, categoryCaptor.getValue());
    }

    @Test
    void testDeleteCategorySuccessfully() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        Mockito.when(databaseServiceClient.deleteCategory(eq(userId), eq(categoryId)))
                .thenReturn(responseEntity);

        mockMvc.perform(delete("/api/v1/categories/{categoryId}", categoryId)
                        .header("userId", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCategoryInvalidUserId() throws Exception {
        Long userId = -1L; // Negative user ID which is invalid
        Long categoryId = 1L;

        mockMvc.perform(delete("/api/v1/categories/{categoryId}", categoryId)
                        .header("userId", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteCategoryInvalidCategoryId() throws Exception {
        Long userId = 1L;
        Long categoryId = -1L; // Negative category ID which is invalid

        mockMvc.perform(delete("/api/v1/categories/{categoryId}", categoryId)
                        .header("userId", userId))
                .andExpect(status().isBadRequest());
    }
}
