package com.learn.taskManagement.service;

import com.learn.taskManagement.dto.TaskRequestDto;
import com.learn.taskManagement.dto.TaskResponseDto;
import com.learn.taskManagement.enums.Priority;
import com.learn.taskManagement.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    // create new task
    TaskResponseDto createTask(TaskRequestDto taskRequestDto);

    // Get all tasks
    List<TaskResponseDto> getAllTasks();

    // Get Single task by ID
    TaskResponseDto getTaskById(Long id);

    // Update an existing task
    TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto);

    // Delete a task
    void deleteTask(Long id);

    // Get tasks filtered by Priority
    List<TaskResponseDto> getTasksByPriority(Priority priority);

    // Get tasks filtered by status
    List<TaskResponseDto> getTasksByStatus(TaskStatus status);

}


