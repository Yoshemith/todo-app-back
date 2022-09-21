package com.encora.todoappback.service;

import com.encora.todoappback.domain.TodoTask;
import com.encora.todoappback.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public HashMap<String, Object> getTodoTasks(Integer page, String name, String status, String priority) {

        if (todoRepository.getAllTodoTasks().size() == 0) {
            return null;
        }

        HashMap<String, Object> result = new HashMap<>();
        List<TodoTask> todoTasksList;

        if(!name.equals("") && status.equals("ALL") && priority.equals("ALL")) {
            todoTasksList = todoRepository.getByNameTodoTasks(name);
        } else if(!name.equals("") && !status.equals("ALL") && priority.equals("ALL")){
            todoTasksList = todoRepository.getByNameAndStatusTodoTasks(name, status);
        } else if(!name.equals("") && status.equals("ALL") && !priority.equals("ALL")){
            todoTasksList = todoRepository.getByNameAndPriorityTodoTasks(name, priority);
        } else if(!name.equals("") && !status.equals("ALL") && !priority.equals("ALL")){
            todoTasksList = todoRepository.getByNameAndPriorityAndStatusOfTask(name, status, priority);
        } else if(!status.equals("ALL") && priority.equals("ALL")){
            todoTasksList = todoRepository.getByStatusTodoTasks(status);
        } else if(status.equals("ALL") && !priority.equals("ALL")){
            todoTasksList = todoRepository.getByPriorityTodoTasks(priority);
        } else if(!status.equals("ALL") && !priority.equals("ALL")){
            todoTasksList = todoRepository.getByStatusAndPriorityTodoTasks(status, priority);
        } else {
            todoTasksList = todoRepository.getAllTodoTasks();
        }

        //TODO: Add a method here that'll do the sorting

        List<TodoTask> data = todoRepository.getTodoTasksByPage(todoTasksList, page);
        int totalPages = (int) Math.ceil(todoTasksList.size() / 10d);

        result.put("data", data);
        result.put("totalTodos", data.size());
        result.put("totalPages", totalPages);
        result.put("page", page);
        result.put("name", name);
        result.put("status", status);
        result.put("priority", priority);

        return result;
    }

    public TodoTask getById(Integer id) {
        return todoRepository.getById(id);
    }

    public TodoTask createTodoTask(TodoTask task) {
        //LocalDate dueDate = LocalDate.of(2022, 10, 13);
        LocalDateTime dateOfCreation = LocalDateTime.now();

        TodoTask todoTask = new TodoTask();
        todoTask.setIsDone(false);
        todoTask.setName(task.getName());
        todoTask.setCreationDate(dateOfCreation);
        todoTask.setDueDate(task.getDueDate());
        todoTask.setPriority(task.getPriority());

        todoTask = todoRepository.save(todoTask);

        return todoTask;
    }

    public TodoTask updateTodoTask(Integer id,TodoTask todoTask) {
        Optional<TodoTask> todoTaskOptional = todoRepository.getAllTodoTasks()
                .stream()
                .filter(task -> task.getId().equals(id))
                .findAny();
        if (todoTaskOptional.isPresent()) {
            TodoTask task = todoTaskOptional.get();
            task.setName(todoTask.getName());
            task.setDueDate(todoTask.getDueDate());
            task.setPriority(todoTask.getPriority());

            return task;
        }
        return null;
    }

    public void deleteTodoTask(Integer id) {
        todoRepository.delete(id);
    }

    public TodoTask setTaskToDone(Integer id) {
        Optional<TodoTask> todoTaskOptional = todoRepository.getAllTodoTasks()
                .stream()
                .filter(task -> task.getId().equals(id))
                .findAny();
        if (todoTaskOptional.isPresent()) {
            LocalDateTime doneDate = LocalDateTime.now();
            TodoTask task = todoTaskOptional.get();
            task.setIsDone(true);
            task.setDoneDate(doneDate);

            return task;
        }
        return null;
    }

    public TodoTask setTaskToUndone(Integer id) {
        Optional<TodoTask> todoTaskOptional = todoRepository.getAllTodoTasks()
                .stream()
                .filter(task -> task.getId().equals(id))
                .findAny();
        if (todoTaskOptional.isPresent()) {
            TodoTask task = todoTaskOptional.get();
            task.setIsDone(false);
            task.setDoneDate(null);

            return task;
        }
        return null;
    }

    public HashMap<String, Object> getTodosMetrics() {
        HashMap<String, Object> result = new HashMap<>();

        if (todoRepository.getAllTodoTasks().size() > 0) {
            HashMap<String, Object> data = todoRepository.getTodosMetrics();
            result.put("averageTimeBy", data);

            return  result;
        }

        return null;
    }
}
