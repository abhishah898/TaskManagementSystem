package com.learn.taskManagement.controller;

import com.learn.taskManagement.dto.TaskRequestDto;
import com.learn.taskManagement.dto.TaskResponseDto;
import com.learn.taskManagement.enums.Priority;
import com.learn.taskManagement.enums.TaskStatus;
import com.learn.taskManagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // Create new task
    @PostMapping("/task")
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        TaskResponseDto createdTask = taskService.createTask(taskRequestDto);
        return ResponseEntity.ok(createdTask);
    }

    // Get all tasks
    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Get Tasks by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // Update existing task
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDto requestDto) {
        return ResponseEntity.ok(taskService.updateTask(id, requestDto));
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // Get tasks by priority
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByPriority(@PathVariable Priority priority) {
        return ResponseEntity.ok(taskService.getTasksByPriority(priority));
    }

    // Get tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByStatus(@PathVariable TaskStatus status) {
        System.out.println("HERE: " + status);
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }
}
