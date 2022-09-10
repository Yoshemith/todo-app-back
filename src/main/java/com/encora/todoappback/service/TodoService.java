package com.encora.todoappback.service;

import com.encora.todoappback.domain.Priority;
import com.encora.todoappback.domain.TodoTask;
import com.encora.todoappback.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<TodoTask> getAllTodoTasks(String name, String status, String priority) {
        return todoRepository.getAllTodoTasks();

        //TODO: refactor this code and fix Enum conversion
//        if(!name.equals("") && (status.equals("ALL") || status.equals("")) && (priority.equals("ALL") || priority.equals(""))) {
//            return todoRepository.getAllTodoTasks();
//        }
//        return todoRepository.findByNameAndPriorityAndStatusOfTask(name, status, priority);
    }

    public Optional<TodoTask> getById(Integer id) {
        Optional<TodoTask> foundTodoTaskOptional = Optional.of(todoRepository.getById(id));

        return foundTodoTaskOptional;
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
            LocalDateTime doneDate = LocalDateTime.now();
            TodoTask task = todoTaskOptional.get();
            task.setIsDone(false);
            task.setDoneDate(null);

            return task;
        }

        return null;
    }
}
