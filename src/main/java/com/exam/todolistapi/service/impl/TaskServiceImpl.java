package com.exam.todolistapi.service.impl;

import com.exam.todolistapi.exception.EmptyTaskListException;
import com.exam.todolistapi.exception.InvalidPositionException;
import com.exam.todolistapi.exception.NonExistentTaskException;
import com.exam.todolistapi.models.Task;
import com.exam.todolistapi.repository.TaskRepository;
import com.exam.todolistapi.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public void createTask(Task task) {
        int position = taskRepository.findAll().size() + 1;
        task.setPosition(position);
        taskRepository.save(task);
    }

    @Override
    public void updateTaskDetails(Long id, Task updatedTask) throws NonExistentTaskException {
        Optional<Task> foundTask = taskRepository.findById(id);

        if(foundTask.isPresent()) {
            Task existingTask = foundTask.get();
            existingTask.setTaskName(updatedTask.getTaskName());
            existingTask.setTaskInstructions(updatedTask.getTaskInstructions());

            taskRepository.save(existingTask);
        }
        else
            throw new NonExistentTaskException("Task doesn't exist, please try again");
    }

    @Override
    public void deleteTask(Long id){
        Optional<Task> foundTask = taskRepository.findById(id);

        foundTask.ifPresent(task -> taskRepository.delete(task));
    }

    @Override
    public List<Task> reorderTask(Long id, int newPosition) throws InvalidPositionException, NonExistentTaskException, EmptyTaskListException {
        List<Task> currentTaskList = taskRepository.findAll();
        Optional<Task> toBeUpdatedTask = taskRepository.findById(id);
        List<Task> updatedTaskList = new ArrayList<>();
        //sort all data first after retrieving from db
        //do this by using sort() for collections
        Collections.sort(currentTaskList);

        if(currentTaskList.size() == 0)
            throw new EmptyTaskListException("Task list is empty, please create some tasks first");

        if(newPosition <= 0)
            throw new InvalidPositionException("Negative position detected, please try again");

        if(newPosition > currentTaskList.size())
            throw new InvalidPositionException("No existing task in position of task list, please try again");

        if(toBeUpdatedTask.isPresent()){
            if(toBeUpdatedTask.get().getPosition() == newPosition)
                return currentTaskList;

            //after sorting get index of task with the same value as new position
            //loop on starting index and add +1 to all of their position except task with same value of id
            for(int i = newPosition - 1;i < currentTaskList.size();i++){
                if(currentTaskList.get(i).getId().equals(id))
                    continue;

                if(!currentTaskList.get(i).getId().equals(id) &&
                        currentTaskList.get(i).getPosition() < toBeUpdatedTask.get().getPosition()) {
                    currentTaskList.get(i).setPosition(currentTaskList.get(i).getPosition() + 1);
                    taskRepository.save(currentTaskList.get(i));
                }
            }

            //update the task with new position
            toBeUpdatedTask.get().setPosition(newPosition);
            taskRepository.save(toBeUpdatedTask.get());

            //sort reordered list
            updatedTaskList = taskRepository.findAll();
            Collections.sort(updatedTaskList);
            for(Task updatedTask : updatedTaskList){
                System.out.println(updatedTask.getId());
                System.out.println(updatedTask.getTaskName());
                System.out.println(updatedTask.getPosition());
            }
        }
        else
            throw new NonExistentTaskException("Task doesn't exist, please try again");

        return updatedTaskList;
    }
}
