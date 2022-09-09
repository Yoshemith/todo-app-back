package com.encora.todoappback.controller;

import com.encora.todoappback.domain.TodoTask;
import com.encora.todoappback.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodoTasks() {
        List<TodoTask> todoTasks = todoService.getAllTodoTasks();
        return ResponseEntity.ok(todoTasks);     //ResponseEntity.status(HttpStatus.OK).body(todoTasks);
    }

    //take a look at this, how would you send the error if there is no id (btw here you're returning an Optional)
    @GetMapping("/todos/{id}")
    public Optional<TodoTask> getById(@PathVariable Integer id) {
        Optional<TodoTask> todoTask = todoService.getById(id);
        return todoTask;
    }

    @PostMapping("/todos")
    public ResponseEntity<?> createNewTodoTask(@RequestBody TodoTask task) {
        TodoTask todoTask = todoService.createTodoTask(task);

        return ResponseEntity.ok(todoTask);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateTodoTask(@PathVariable Integer id, @RequestBody TodoTask todoTask) {
        TodoTask updatedTodoTask = todoService.updateTodoTask(id, todoTask);

        return ResponseEntity.ok(updatedTodoTask);
    }
    @PutMapping("/todos/{id}/done")
    public ResponseEntity<?> setTaskToDone(@PathVariable Integer id) {
        TodoTask updatedTodoTask = todoService.setTaskToDone(id);

        return ResponseEntity.ok(updatedTodoTask);
    }

    @PutMapping("/todos/{id}/undone")
    public ResponseEntity<?> setTaskToUndone(@PathVariable Integer id) {
        TodoTask updatedTodoTask = todoService.setTaskToUndone(id);

        return ResponseEntity.ok(updatedTodoTask);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTodoTask(@PathVariable Integer id) {
        todoService.deleteTodoTask(id);

        return ResponseEntity
                .status(HttpStatus.GONE)
                .body("Todo Task Deleted!");
    }

}