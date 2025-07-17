package com.learn.taskManagement.service;

import com.learn.taskManagement.dto.TaskRequestDto;
import com.learn.taskManagement.dto.TaskResponseDto;
import com.learn.taskManagement.entity.Task;
import com.learn.taskManagement.enums.Priority;
import com.learn.taskManagement.enums.TaskStatus;
import com.learn.taskManagement.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskServiceImpl taskService;

    private TaskRequestDto getMockTaskRequest() {
        return TaskRequestDto.builder()
                .title("Sample Task")
                .description("Desc")
                .priority(String.valueOf(Priority.HIGH))
                .taskStatus(String.valueOf(TaskStatus.PENDING))
                .dueDate(LocalDate.now().plusDays(2))
                .build();
    }

    private Task getMockTask() {
        return Task.builder()
                .id(1L)
                .title("Sample Task")
                .description("Desc")
                .priority(Priority.HIGH)
                .taskStatus(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(2))
                .build();
    }

    @Test
    void shouldReturnAllTasks() {
        // Given
        Task task = getMockTask();

        when(taskRepository.findAll()).thenReturn(List.of(task));
        // When
        List<TaskResponseDto> tasks = taskService.getAllTasks();

        // Then
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Sample Task");
        assertThat(tasks.get(0).getPriority()).isEqualTo(Priority.HIGH);

    }

    @Test
    void shouldReturnTaskById() {
        // given
        Task task = getMockTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        TaskResponseDto result = taskService.getTaskById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Sample Task");
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        // given
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> taskService.getTaskById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void shouldCreateNewTask() {
        // given
        TaskRequestDto request = getMockTaskRequest();
        Task savedTask = getMockTask();
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // when
        TaskResponseDto result = taskService.createTask(request);

        // then
        assertThat(result.getTitle()).isEqualTo("Sample Task");
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldUpdateExistingTask() {
        // given
        Task existing = getMockTask();
        TaskRequestDto updateRequest = getMockTaskRequest();
        updateRequest.setTitle("Updated Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenReturn(existing);

        // when
        TaskResponseDto result = taskService.updateTask(1L, updateRequest);

        // then
        assertThat(result.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void shouldDeleteTask() {
        // given
        Long taskId = 1L;

        // when
        taskService.deleteTask(taskId);

        // then
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldReturnTasksByPriority() {
        Task task = getMockTask();
        when(taskRepository.findByPriority(Priority.HIGH)).thenReturn(List.of(task));

        List<TaskResponseDto> result = taskService.getTasksByPriority(Priority.HIGH);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void shouldReturnTasksByStatus() {
        Task task = getMockTask();
        when(taskRepository.findByTaskStatus(TaskStatus.PENDING)).thenReturn(List.of(task));

        List<TaskResponseDto> result = taskService.getTasksByStatus(TaskStatus.PENDING);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTaskStatus()).isEqualTo(TaskStatus.PENDING);
    }

}
