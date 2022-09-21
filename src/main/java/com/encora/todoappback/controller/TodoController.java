package com.encora.todoappback.controller;

import com.encora.todoappback.domain.TodoTask;
import com.encora.todoappback.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos")
    public ResponseEntity<?> getTodoTasks(@RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "") String name,
                                          @RequestParam(required = false, defaultValue = "ALL") String status,
                                          @RequestParam(required = false, defaultValue = "ALL") String priority,
                                          @RequestParam(required = false, defaultValue = "") Integer sorting,
                                          @RequestParam(required = false, defaultValue = "ASC") String order) {

        HashMap<String, Object> response = todoService.getTodoTasks(page, name, status, priority, sorting, order);

        if (response == null) {
            response = new HashMap<>();
            response.put("httpStatus", 404);
            response.put("httpMessage", "There is no data available");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("httpStatus", 200);
        response.put("httpMessage", "OK");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        TodoTask todoTask = todoService.getById(id);
        HashMap<String, Object> response = new HashMap<>();

        if (todoTask == null) {
            response.put("httpStatus", 404);
            response.put("message", "NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("httpStatus", 200);
        response.put("message", "OK");
        response.put("data", todoTask);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/todos")
    public ResponseEntity<?> createNewTodoTask(@RequestBody TodoTask task) {
        TodoTask todoTask = todoService.createTodoTask(task);
        HashMap<String, Object> response = new HashMap<>();

        response.put("httpStatus", 201);
        response.put("httpMessage", "CREATED");
        response.put("data", todoTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateTodoTask(@PathVariable Integer id, @RequestBody TodoTask todoTask) {
        TodoTask updatedTodoTask = todoService.updateTodoTask(id, todoTask);
        return getResponseEntity(updatedTodoTask);
    }

    @PutMapping("/todos/{id}/done")
    public ResponseEntity<?> setTaskToDone(@PathVariable Integer id) {
        TodoTask updatedTodoTask = todoService.setTaskToDone(id);
        return getResponseEntity(updatedTodoTask);
    }

    @PutMapping("/todos/{id}/undone")
    public ResponseEntity<?> setTaskToUndone(@PathVariable Integer id) {
        TodoTask updatedTodoTask = todoService.setTaskToUndone(id);
        return getResponseEntity(updatedTodoTask);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTodoTask(@PathVariable Integer id) {
        HashMap<String, Object> response = new HashMap<>();

        if (todoService.getById(id) == null) {
            response.put("httpStatus", 404);
            response.put("httpMessage", "NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        todoService.deleteTodoTask(id);
        response.put("httpStatus", 200);
        response.put("httpMessage", "Resource deleted successfully");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/todos/metrics")
    public ResponseEntity<?> getTodosMetrics() {
        HashMap<String, Object> response = todoService.getTodosMetrics();

        if (response == null) {
            response = new HashMap<>();
            response.put("httpStatus", 404);
            response.put("httpMessage", "There is no data available");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("httpStatus", 200);
        response.put("httpMessage", "OK");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private ResponseEntity<?> getResponseEntity(TodoTask updatedTodoTask) {
        HashMap<String, Object> response = new HashMap<>();

        if (updatedTodoTask == null) {
            response = new HashMap<>();
            response.put("httpStatus", 404);
            response.put("httpMessage", "NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("data", updatedTodoTask);
        response.put("httpStatus", 200);
        response.put("httpMessage", "OK");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
