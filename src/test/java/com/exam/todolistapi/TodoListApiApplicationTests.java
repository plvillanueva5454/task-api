package com.exam.todolistapi;

import com.exam.todolistapi.exception.EmptyTaskListException;
import com.exam.todolistapi.exception.InvalidPositionException;
import com.exam.todolistapi.exception.NonExistentTaskException;
import com.exam.todolistapi.models.Task;
import com.exam.todolistapi.repository.TaskRepository;
import com.exam.todolistapi.service.TaskService;
import com.exam.todolistapi.service.impl.TaskServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class TodoListApiApplicationTests {

	public static TaskRepository taskRepository;
	public static TaskService taskService;

	@BeforeAll
	static void init(){
		taskRepository = mock(TaskRepository.class);
		taskService = new TaskServiceImpl(taskRepository);
	}

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("createTask() should create and return a task")
	void createTask_ShouldSaveToRepository(){
		Task task = new Task();
		task.setId(1L);
		task.setTaskName("Test Task");
		task.setTaskInstructions("Testing this method");

		taskService.createTask(task);

		verify(taskRepository).save(task);
	}

	@Test
	@DisplayName("getAllTasks() should return all tasks created")
	void getAllTasks_ShouldReturnListOfTasks(){
		taskService.getAllTasks();
		verify(taskRepository).findAll();
	}

	@Test
	@DisplayName("updateTaskDetails() should update task details")
	void updateTaskDetails_ShouldUpdateTheTaskDetails() throws NonExistentTaskException {
		Task task = new Task();
		task.setId(1L);
		task.setTaskName("Test Task");
		task.setTaskInstructions("Testing this method");

		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		taskService.updateTaskDetails(1L, task);
		verify(taskRepository).save(task);
	}

	@Test
	@DisplayName("reorderTask() should throw NonExistentTaskException when given task id does not exist in table")
	void updateTaskDetails_ShouldThrowNonExistentTaskExceptionWhenTaskIdGivenDoesNotExistInTable(){
		when(taskRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(NonExistentTaskException.class, () -> taskService.updateTaskDetails(1L, new Task()));
	}

	@Test
	@DisplayName("removeTask() should remove the task from the task list")
	void removeTask_ShouldRemoveTaskFromTheTaskList(){
		Task task = new Task();
		task.setId(1L);
		task.setTaskName("Test Task");
		task.setTaskInstructions("Testing this method");
		task.setPosition(1);

		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		taskService.deleteTask(1L);
		verify(taskRepository).delete(task);
	}

	@Test
	@DisplayName("reorderTask() should reorder the task and other tasks in the list")
	void reorderTask_ShouldReturnReorderedTaskList() throws NonExistentTaskException, InvalidPositionException, EmptyTaskListException {
		Task task1 = new Task();
		task1.setId(1L);
		task1.setTaskName("Test Task 1");
		task1.setTaskInstructions("Testing this method");
		task1.setPosition(1);

		Task task2 = new Task();
		task2.setId(2L);
		task2.setTaskName("Test Task 2");
		task2.setTaskInstructions("Testing this method");
		task2.setPosition(2);

		Task task3 = new Task();
		task3.setId(3L);
		task3.setTaskName("Test Task 3");
		task3.setTaskInstructions("Testing this method");
		task3.setPosition(3);

		Task task4 = new Task();
		task4.setId(4L);
		task4.setTaskName("Test Task 4");
		task4.setTaskInstructions("Testing this method");
		task4.setPosition(4);

		Task task5 = new Task();
		task5.setId(5L);
		task5.setTaskName("Test Task 5");
		task5.setTaskInstructions("Testing this method");
		task5.setPosition(5);

		List<Task> taskList = new ArrayList<>();
		taskList.add(task1);
		taskList.add(task2);
		taskList.add(task3);
		taskList.add(task4);
		taskList.add(task5);

		Task updatedTask1 = new Task();
		updatedTask1.setId(1L);
		updatedTask1.setTaskName("Test Task 1");
		updatedTask1.setTaskInstructions("Testing this method");
		updatedTask1.setPosition(2);

		Task updatedTask2 = new Task();
		updatedTask2.setId(2L);
		updatedTask2.setTaskName("Test Task 2");
		updatedTask2.setTaskInstructions("Testing this method");
		updatedTask2.setPosition(3);

		Task updatedTask3 = new Task();
		updatedTask3.setId(3L);
		updatedTask3.setTaskName("Test Task 3");
		updatedTask3.setTaskInstructions("Testing this method");
		updatedTask3.setPosition(1);

		Task updatedTask4 = new Task();
		updatedTask4.setId(4L);
		updatedTask4.setTaskName("Test Task 4");
		updatedTask4.setTaskInstructions("Testing this method");
		updatedTask4.setPosition(4);

		Task updatedTask5 = new Task();
		updatedTask5.setId(5L);
		updatedTask5.setTaskName("Test Task 5");
		updatedTask5.setTaskInstructions("Testing this method");
		updatedTask5.setPosition(5);

		List<Task> updatedTaskList = new ArrayList<>();
		updatedTaskList.add(updatedTask3);
		updatedTaskList.add(updatedTask1);
		updatedTaskList.add(updatedTask2);
		updatedTaskList.add(updatedTask4);
		updatedTaskList.add(updatedTask5);

		when(taskRepository.findAll()).thenReturn(taskList);
		when(taskRepository.findById(3L)).thenReturn(Optional.of(task3));
//		when(taskRepository.save())

		List<Task> testUpdatedTaskList = taskService.reorderTask(3L, 1);

		assertEquals(testUpdatedTaskList.get(0).getPosition(), updatedTaskList.get(0).getPosition());
		assertEquals(testUpdatedTaskList.get(1).getPosition(), updatedTaskList.get(1).getPosition());
		assertEquals(testUpdatedTaskList.get(2).getPosition(), updatedTaskList.get(2).getPosition());
		assertEquals(testUpdatedTaskList.get(3).getPosition(), updatedTaskList.get(3).getPosition());
		assertEquals(testUpdatedTaskList.get(4).getPosition(), updatedTaskList.get(4).getPosition());
	}

	@Test
	@DisplayName("reorderTask() should throw EmptyTaskListException when task list is empty")
	void reorderTask_ShouldThrowEmptyTaskListException() throws NonExistentTaskException, InvalidPositionException, EmptyTaskListException {
		List<Task> taskList = new ArrayList<>();
		when(taskRepository.findAll()).thenReturn(taskList);

		assertThrows(EmptyTaskListException.class, () -> taskService.reorderTask(1L, 3));
	}

	@Test
	@DisplayName("reorderTask() should throw InvalidPositionException when position value is negative or zero")
	void reorderTaskInTaskList_ShouldThrowInvalidPositionExceptionWhenPositionGivenIsZeroOrNegative(){
		List<Task> taskList = new ArrayList<>();
		taskList.add(new Task());
		when(taskRepository.findAll()).thenReturn(taskList);

		assertThrows(InvalidPositionException.class, () -> taskService.reorderTask(1L, 0));
	}

	@Test
	@DisplayName("reorderTask() should throw InvalidPositionException when given position is greater than task list size")
	void reorderTaskInTaskList_ShouldThrowInvalidPositionExceptionWhenPositionGivenIsGreaterThanTaskListSize(){
		List<Task> taskList = new ArrayList<>();
		taskList.add(new Task());
		taskList.add(new Task());
		when(taskRepository.findAll()).thenReturn(taskList);

		assertThrows(InvalidPositionException.class, () -> taskService.reorderTask(1L, 23));
	}

	@Test
	@DisplayName("reorderTask() should throw NonExistentTaskException when given task id does not exist in table")
	void reorderTaskInTaskList_ShouldThrowNonExistentTaskExceptionWhenTaskIdGivenDoesNotExistInTable(){
		List<Task> taskList = new ArrayList<>();
		taskList.add(new Task());
		taskList.add(new Task());
		when(taskRepository.findAll()).thenReturn(taskList);
		when(taskRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(NonExistentTaskException.class, () -> taskService.reorderTask(1L, 2));
	}

}
