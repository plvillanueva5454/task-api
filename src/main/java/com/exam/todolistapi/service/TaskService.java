package com.exam.todolistapi.service;

import com.exam.todolistapi.exception.EmptyTaskListException;
import com.exam.todolistapi.exception.InvalidPositionException;
import com.exam.todolistapi.exception.NonExistentTaskException;
import com.exam.todolistapi.models.Task;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();

    void createTask(Task task);

    void updateTaskDetails(Long id, Task updatedTask) throws NonExistentTaskException;

    void deleteTask(Long id);

    List<Task> reorderTask(Long id, int newPosition) throws InvalidPositionException, NonExistentTaskException, EmptyTaskListException;

}
