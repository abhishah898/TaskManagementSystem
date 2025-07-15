package com.learn.taskManagement.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskRequestDto {
    @NotNull(message = "Priority is required")
    private String priority;

    @NotNull(message = "Task Status is required")
    private String taskStatus;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 100, message = "Title must be between 3 to 100 char")
    private String title;

    @FutureOrPresent(message = "Due date must be today or a future date")
    private LocalDate dueDate;

    @Size(max = 500, message = "Description can not exceed 500 char")
    private String description;
}
