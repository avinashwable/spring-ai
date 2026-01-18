package com.avi.spring.aiworkshop.tools.act;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class TaskManagementTools {
    public record TaskResult(Long taskId, String title, String status, String assignee, String details) {
    }

    public record Task(Long id, String title, TaskStatus taskStatus, String assignee, String description) {
    }

    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        ON_HOLD,
        CANCELLED
    }

    private final Map<Long, Task> taskStore = new ConcurrentHashMap<>();
    private final AtomicLong taskIdGenerator = new AtomicLong(1);

    @Tool(description = "Create a new task with title, assignee, and description")
    public TaskResult createTask(String title, String assignee, String description) {
        Long taskId = taskIdGenerator.getAndIncrement();
        Task task = new Task(taskId, title, TaskStatus.PENDING, assignee, description);
        taskStore.put(taskId, task);
        // save to db, send notifications, etc.
        log.info("Created new task: {}", task);
        return new TaskResult(taskId, title, task.taskStatus.name(), assignee, description);
    }

    @Tool(description = "Assign or reassign task to a different person")
    public TaskResult assignTask(Long taskId, String assignee) {
        Task task = taskStore.get(taskId);
        if (task == null) {
            return new TaskResult(taskId, "", "NOT_FOUND", "", "Task not found");
        }
        Task updatedTask = new Task(task.id, task.title, task.taskStatus, assignee, task.description);
        taskStore.put(taskId, updatedTask);
        // update in db, send notifications, etc.
        log.info("Assigned task: {}", updatedTask);
        return new TaskResult(taskId, task.title, task.taskStatus.name(), assignee, "Task reassigned to " + assignee);
    }
}
