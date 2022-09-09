package com.encora.todoappback.repository;

import com.encora.todoappback.domain.Priority;
import com.encora.todoappback.domain.TodoTask;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TodoRepository {

    private Integer idCounter = 1;
    private List<TodoTask> todoTasks = new ArrayList<>();

    public void dummyData() {
        LocalDate dueDate = LocalDate.of(2022, 10, 13);
        LocalDateTime dateOfCreation = LocalDateTime.now();
        TodoTask task1 = new TodoTask();
        task1.setId(idCounter++);
        task1.setName("Mi primer todo task xdxd");
        task1.setDueDate(dueDate);
        task1.setIsDone(false);
        task1.setPriority(Priority.HIGH);
        task1.setCreationDate(dateOfCreation);

        todoTasks.add(task1);
    }

    public List<TodoTask> getAllTodoTasks() {
        if (todoTasks.size() == 0) {
            dummyData();
        }
        return todoTasks;
    }

    public TodoTask getById(Integer id) {
        if (!existsById(id)) return null;

        TodoTask foundTodoTask = todoTasks.stream()
                .filter(task -> task.getId().equals(id))
                .findAny()
                .orElse(null);
        return foundTodoTask;
    }

    public TodoTask save(TodoTask todoTask) {
        todoTask.setId(idCounter++);
        todoTasks.add(todoTask);

        return todoTask;
    }

    public void delete(Integer id) {
        //todoTasks.remove(id);
//        todoTasks.stream()
//                .filter(task -> !task.getId().equals(id))
//                .collect(Collectors.toList());
        todoTasks.removeIf(task -> task.getId().equals(id));
    }

    public boolean existsById(Integer id) {
        return todoTasks.stream().anyMatch(task -> task.getId().equals(id));
    }

}
