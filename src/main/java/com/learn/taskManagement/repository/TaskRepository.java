package com.learn.taskManagement.repository;


import com.learn.taskManagement.entity.Task;
import com.learn.taskManagement.enums.Priority;
import com.learn.taskManagement.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByPriority(Priority priority);

    List<Task> findByTaskStatus(TaskStatus status);
}
