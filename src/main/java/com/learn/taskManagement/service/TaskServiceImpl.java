package com.learn.taskManagement.service;

import com.learn.taskManagement.dto.TaskRequestDto;
import com.learn.taskManagement.dto.TaskResponseDto;
import com.learn.taskManagement.entity.Task;
import com.learn.taskManagement.enums.Priority;
import com.learn.taskManagement.enums.TaskStatus;
import com.learn.taskManagement.exception.ResourceNotFoundException;
import com.learn.taskManagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Task task = mapToEntity(taskRequestDto);
        Task saved = taskRepository.save(task);
        return mapToDto(saved);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository
                .findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
        return mapToDto(task);
    }

    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setDueDate(taskRequestDto.getDueDate());
        task.setPriority(Priority.valueOf(taskRequestDto.getPriority().toUpperCase()));
        task.setTaskStatus(TaskStatus.valueOf(taskRequestDto.getTaskStatus().toUpperCase()));

        Task updated = taskRepository.save(task);
        return mapToDto(updated);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskResponseDto> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByTaskStatus(status)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // Helper Function to map from DTO to entity
    public Task mapToEntity(TaskRequestDto dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .priority(Priority.valueOf(dto.getPriority().toUpperCase()))
                .taskStatus(TaskStatus.valueOf(dto.getTaskStatus().toUpperCase()))
                .build();
    }

    // Helper function to map from entity to dto
    public TaskResponseDto mapToDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .taskStatus(task.getTaskStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdateAt())
                .build();
    }
}
