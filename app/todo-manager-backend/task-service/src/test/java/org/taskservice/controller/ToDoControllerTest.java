package org.taskservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.taskservice.clients.IDatabaseServiceClient;
import org.taskservice.payload.CategoryDTO;
import org.taskservice.payload.ToDoDTO;
import org.taskservice.payload.ToDoPriorityDTO;
import org.taskservice.payload.ToDoStatusDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
class ToDoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDatabaseServiceClient databaseServiceClient;

    @Test
    void testSaveToDoSuccessfully() throws Exception {
        Long userId = 1L;
        Long categoryId = 2L;
        ToDoDTO toDoDTO = new ToDoDTO(null, "Finish the monthly report.", ToDoStatusDTO.NOT_STARTED, ToDoPriorityDTO.MEDIUM, categoryId);

        ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.CREATED).build();
        Mockito.when(databaseServiceClient.createToDo(eq(userId), eq(categoryId), any(ToDoDTO.class)))
                .thenReturn(responseEntity);

        mockMvc.perform(post("/api/v1/categories/{categoryId}/tasks", categoryId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(toDoDTO))))
                .andExpect(status().isCreated());

        ArgumentCaptor<ToDoDTO> toDoCaptor = ArgumentCaptor.forClass(ToDoDTO.class);
        Mockito.verify(databaseServiceClient).createToDo(eq(userId), eq(categoryId), toDoCaptor.capture());

        ToDoDTO capturedToDoDTO = toDoCaptor.getValue();
        assertNull(capturedToDoDTO.getId());
        assertEquals("Finish the monthly report.", capturedToDoDTO.getText());
        assertEquals(ToDoStatusDTO.NOT_STARTED, capturedToDoDTO.getStatus());
        assertEquals(ToDoPriorityDTO.MEDIUM, capturedToDoDTO.getPriority());
        assertEquals(categoryId, capturedToDoDTO.getCategoryId());
    }

    @Test
    void testSaveToDoWithInvalidUserId() throws Exception {
        Long invalidUserId = -1L;
        Long categoryId = 2L;
        ToDoDTO toDoDTO = new ToDoDTO(null, "Finish the monthly report.", ToDoStatusDTO.NOT_STARTED, ToDoPriorityDTO.MEDIUM, categoryId);

        mockMvc.perform(post("/api/v1/categories/{categoryId}/tasks", invalidUserId, categoryId)
                        .header("userId", invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(toDoDTO))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveToDoWithInvalidCategoryId() throws Exception {
        Long userId = 1L;
        Long invalidCategoryId = -1L;
        ToDoDTO toDoDTO = new ToDoDTO(null, "Finish the monthly report.", ToDoStatusDTO.NOT_STARTED, ToDoPriorityDTO.MEDIUM, invalidCategoryId);

        mockMvc.perform(post("/api/v1/categories/{categoryId}/tasks", userId, invalidCategoryId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(toDoDTO))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveToDoWithInvalidToDoDTO() throws Exception {
        Long userId = 1L;
        Long categoryId = 2L;
        ToDoDTO invalidToDoDTO = new ToDoDTO(null, "", null, null, categoryId);

        mockMvc.perform(post("/api/v1/categories/{categoryId}/tasks", userId, categoryId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(invalidToDoDTO))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveToDoWithNullToDoDTO() throws Exception {
        Long userId = 1L;
        Long categoryId = 2L;

        mockMvc.perform(post("/api/v1/categories/{categoryId}/tasks", userId, categoryId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveToDoWithNullText() throws Exception {
        Long userId = 1L;
        Long categoryId = 2L;
        ToDoDTO toDoDTO = new ToDoDTO(null, null, ToDoStatusDTO.NOT_STARTED, ToDoPriorityDTO.MEDIUM, categoryId);

        mockMvc.perform(post("/api/v1/categories/{categoryId}/tasks", userId, categoryId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(toDoDTO))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveToDoWithInvalidStatus() throws Exception {
        Long userId = 1L;
        Long categoryId = 2L;
        ToDoDTO toDoDTO = new ToDoDTO(null, "Finish the monthly report.", null, ToDoPriorityDTO.MEDIUM, categoryId);

        mockMvc.perform(post("/api/v1/categories/{categoryId}/tasks", userId, categoryId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(toDoDTO))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveToDoWithInvalidPriority() throws Exception {
        Long userId = 1L;
        Long categoryId = 2L;
        ToDoDTO toDoDTO = new ToDoDTO(null, "Finish the monthly report.", ToDoStatusDTO.NOT_STARTED, null, categoryId);

        mockMvc.perform(post("/api/v1/categories/{categoryId}/tasks", userId, categoryId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(toDoDTO))))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testGetTodosSuccessfully() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;

        ResponseEntity<List<ToDoDTO>> responseEntity = ResponseEntity.ok(Collections.emptyList());
        Mockito.when(databaseServiceClient.getToDoDTOList(userId, categoryId))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/categories/{categoryId}/tasks", categoryId)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetTodosMissingUserId() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(get("/api/v1/categories/{categoryId}/tasks", categoryId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTodosInvalidUserId() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(get("/api/v1/categories/{categoryId}/tasks", categoryId)
                        .header("userId", -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTodosInvalidCategoryId() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/api/v1/categories/{categoryId}/tasks", -1)
                        .header("userId", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTodosInvalidCategoryIdZero() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/api/v1/categories/{categoryId}/tasks", 0)
                        .header("userId", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTodosInvalidCategoryIdNull() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/api/v1/categories/{categoryId}/tasks", "null")
                        .header("userId", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTodoSuccessfully() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Long toDoId = 1L;
        ToDoDTO toDoDTO = new ToDoDTO(toDoId, "Finish the monthly report.", ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.HIGH, categoryId);

        ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        Mockito.when(databaseServiceClient.updateToDo(eq(userId), eq(categoryId), eq(toDoId), any(ToDoDTO.class)))
                .thenReturn(responseEntity);

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(toDoDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateTodoMissingUserId() throws Exception {
        Long categoryId = 1L;
        Long toDoId = 1L;
        ToDoDTO toDoDTO = new ToDoDTO(toDoId, "Finish the monthly report.", ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.HIGH, categoryId);

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(toDoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTodoMissingRequestBody() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Long toDoId = 1L;

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTodoInvalidCategoryId() throws Exception {
        Long userId = 1L;
        Long categoryId = -1L; // Invalid category ID
        Long toDoId = 1L;
        ToDoDTO toDoDTO = new ToDoDTO(toDoId, "Finish the monthly report.", ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.HIGH, categoryId);

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(toDoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTodoInvalidToDoId() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Long toDoId = -1L; // Invalid todo ID
        ToDoDTO toDoDTO = new ToDoDTO(toDoId, "Finish the monthly report.", ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.HIGH, categoryId);

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(toDoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTodoNullToDoDTO() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Long toDoId = 1L;

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Empty request body
                .andExpect(status().isBadRequest());
    }

    private static Stream<String> provideInvalidTexts() {
        return Stream.of("", " ", null);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTexts")
    void testUpdateTodoInvalidTexts(String text) throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Long toDoId = 1L;
        ToDoDTO toDoDTO = new ToDoDTO(toDoId, text, ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.HIGH, categoryId);

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", userId, categoryId, toDoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(toDoDTO)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testUpdateTodoInvalidUserId() throws Exception {
        Long userId = -1L;
        Long categoryId = 1L;
        Long toDoId = 1L;
        ToDoDTO toDoDTO = new ToDoDTO(toDoId, "Update task", ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.HIGH, categoryId);

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(toDoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTodoNullUserId() throws Exception {
        Long categoryId = 1L;
        Long toDoId = 1L;
        ToDoDTO toDoDTO = new ToDoDTO(toDoId, "Update task", ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.HIGH, categoryId);

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(toDoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTodoEmptyRequestBody() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Long toDoId = 1L;

        mockMvc.perform(put("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteToDoSuccessfully() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Long toDoId = 1L;

        mockMvc.perform(delete("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteToDoInvalidUserId() throws Exception {
        Long userId = -1L;
        Long categoryId = 1L;
        Long toDoId = 1L;

        mockMvc.perform(delete("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteToDoNullUserId() throws Exception {
        Long categoryId = 1L;
        Long toDoId = 1L;

        mockMvc.perform(delete("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteToDoInvalidCategoryId() throws Exception {
        Long userId = 1L;
        Long categoryId = -1L;
        Long toDoId = 1L;

        mockMvc.perform(delete("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteToDoInvalidToDoId() throws Exception {
        Long userId = 1L;
        Long categoryId = 1L;
        Long toDoId = -1L;

        mockMvc.perform(delete("/api/v1/categories/{categoryId}/tasks/{toDoId}", categoryId, toDoId)
                        .header("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
