package org.databaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.databaseservice.payload.ToDoDTO;
import org.databaseservice.payload.ToDoPriorityDTO;
import org.databaseservice.payload.ToDoStatusDTO;
import org.databaseservice.services.ICategoryService;
import org.databaseservice.services.IToDoService;
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
class ToDoControllerTest {
    @MockBean
    private IToDoService toDoService;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ToDoController toDoController;

    private final Long validUserId = 1L;
    private final Long validCategoryId = 1L;
    private final Long validToDoId = 1L;
    private ToDoDTO validToDoDTO;

    @BeforeEach
    void setUp() {
        validToDoDTO = new ToDoDTO(validToDoId, "Task 1", ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.LOW, validCategoryId);
    }

    @Test
    void saveToDo_Success_ShouldReturnStatusCreated() throws Exception {
        doNothing().when(toDoService).saveTodo(eq(validUserId), eq(validCategoryId), any(ToDoDTO.class));

        mockMvc.perform(post("/api/v1/db/users/{userId}/categories/{categoryId}/tasks", validUserId, validCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ToDoDTO(null, "Task 1", ToDoStatusDTO.IN_PROGRESS, ToDoPriorityDTO.LOW, validCategoryId))))
                .andExpect(status().isCreated());
    }

    @Test
    void getToDosFromCategory_Success_ShouldReturnToDos() throws Exception {
        List<ToDoDTO> toDoList = List.of(validToDoDTO);
        when(toDoService.getTodosFromCategory(validUserId, validCategoryId)).thenReturn(toDoList);

        mockMvc.perform(get("/api/v1/db/users/{userId}/categories/{categoryId}/tasks", validUserId, validCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getToDosFromCategory_NoContent_ShouldReturn204() throws Exception {
        when(toDoService.getTodosFromCategory(validUserId, validCategoryId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/db/users/{userId}/categories/{categoryId}/tasks", validUserId, validCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateToDo_Success_ShouldReturnNoContent() throws Exception {
        doNothing().when(toDoService).updateTodo(eq(validUserId), eq(validCategoryId), eq(validToDoId), any(ToDoDTO.class));

        mockMvc.perform(put("/api/v1/db/users/{userId}/categories/{categoryId}/tasks/{toDoId}", validUserId, validCategoryId, validToDoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validToDoDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteToDo_Success_ShouldReturnNoContent() throws Exception {
        doNothing().when(toDoService).deleteTodo(eq(validUserId), eq(validCategoryId), eq(validToDoId));

        mockMvc.perform(delete("/api/v1/db/users/{userId}/categories/{categoryId}/tasks/{toDoId}", validUserId, validCategoryId, validToDoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}
