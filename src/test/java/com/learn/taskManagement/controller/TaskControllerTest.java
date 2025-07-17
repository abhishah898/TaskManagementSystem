package com.learn.taskManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.taskManagement.dto.TaskRequestDto;
import com.learn.taskManagement.dto.TaskResponseDto;
import com.learn.taskManagement.enums.Priority;
import com.learn.taskManagement.enums.TaskStatus;
import com.learn.taskManagement.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    public TaskResponseDto createSampleTaskResponse() {
        return TaskResponseDto.builder()
                .id(1L)
                .title("Sample Task")
                .description("")
                .priority(Priority.HIGH)
                .taskStatus(TaskStatus.COMPLETED)
                .dueDate(LocalDate.now().plusDays(2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public TaskRequestDto createSampleTaskRequest() {
        return TaskRequestDto.builder()
                .title("Sample Task")
                .description("Desc")
                .priority(String.valueOf(Priority.HIGH))
                .taskStatus(String.valueOf(TaskStatus.PENDING))
                .dueDate(LocalDate.now().plusDays(2))
                .build();
    }

    @Test
    void testCreateTask() throws Exception {
        TaskRequestDto request = createSampleTaskRequest();
        TaskResponseDto response = createSampleTaskResponse();

        when(taskService.createTask(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/tasks/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetAllTasks() throws Exception {
        TaskResponseDto response = createSampleTaskResponse();

        when(taskService.getAllTasks()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetTaskById() throws Exception {
        TaskResponseDto response = createSampleTaskResponse();
        when(taskService.getTaskById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskRequestDto request = createSampleTaskRequest();
        TaskResponseDto response = createSampleTaskResponse();

        when(taskService.updateTask(Mockito.eq(1L), Mockito.any())).thenReturn(response);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetTasksByPriority() throws Exception {
        TaskResponseDto response = createSampleTaskResponse();
        when(taskService.getTasksByPriority(Priority.HIGH)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/tasks/priority/HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }

    @Test
    void testGetTasksByStatus() throws Exception {
        TaskResponseDto response = createSampleTaskResponse();
        when(taskService.getTasksByStatus(TaskStatus.COMPLETED)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/tasks/status/DONE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("DONE"));
    }

}
