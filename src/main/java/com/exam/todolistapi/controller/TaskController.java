package com.exam.todolistapi.controller;

import com.exam.todolistapi.exception.EmptyTaskListException;
import com.exam.todolistapi.exception.InvalidPositionException;
import com.exam.todolistapi.exception.NonExistentTaskException;
import com.exam.todolistapi.models.Task;
import com.exam.todolistapi.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lawadvisor/tasklist")
public class TaskController {

    @Autowired
    TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    List<Task> getTasks(){
        return taskService.getAllTasks();
    }

    @PostMapping("/createTask")
    public void createTask(@RequestBody Task task){
        taskService.createTask(task);
    }

    @PutMapping("/updateTask")
    public void updateTask(@RequestParam Long id, @RequestBody Task task) throws NonExistentTaskException {
        taskService.updateTaskDetails(id, task);
    }

    @DeleteMapping("/deleteTask/{id}")
    public void deleteTask(@PathVariable("id") Long id){
        taskService.deleteTask(id);
    }

    @PutMapping("/reorderTask")
    public List<Task> reorderTask(@RequestParam Long id, @RequestParam int newPosition) throws NonExistentTaskException, InvalidPositionException, EmptyTaskListException {
        return taskService.reorderTask(id, newPosition);
    }

    @GetMapping("/test")
    public String test(){
        return "Test controller access success";
    }

}
