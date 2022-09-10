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

    public List<TodoTask> getAllTodoTasks() {
        return todoTasks;
    }

    public List<TodoTask> findByNameAndPriorityAndStatusOfTask(String name, String status, String priority) {
        if(name.equals("") && !status.equals("ALL") && !priority.equals("ALL")){
            return findByStatusAndPriority(status, priority);
        }
        if(name.equals("") && status.equals("ALL") && !priority.equals("ALL")){
            return findByPriority(priority);
        }
        if(name.equals("") && !status.equals("ALL") && priority.equals("ALL")){
            return findByStatus(status);
        }
        if(!name.equals("") && status.equals("ALL") && !priority.equals("ALL")) {
            return findByNameAndPriority(name, priority);
        }
        if(!name.equals("") && !status.equals("ALL") && priority.equals("ALL")) {
            return findByNameAndStatus(name, status);
        }

        List<TodoTask> byNameAndPriorityList = findByName(name).stream().filter(task -> task.getPriority().equals(Priority.valueOf(priority))).toList();

        boolean statusTask;
        if (status.equals("DONE")){
            statusTask = true;
        } else {
            statusTask = false;
        }

        List<TodoTask> resultListByStatus = byNameAndPriorityList.stream()
                .filter(task -> task.getIsDone().equals(statusTask)).toList();

        return resultListByStatus;
    }

    public List<TodoTask> findByName(String name) {
        List<TodoTask> foundTasksByNameList = todoTasks.stream()
                .filter(task -> task.getName().toLowerCase().contains(name.toLowerCase())).toList();
        return foundTasksByNameList;
    }

    public List<TodoTask> findByPriority(String priority) {
        List<TodoTask> foundTasksByPriorityList = todoTasks.stream()
                .filter(task -> task.getPriority().equals(Priority.valueOf(priority))).toList();

        return foundTasksByPriorityList;
    }

    public List<TodoTask> findByStatus(String status) {
        Boolean statusTask;
        if (status.equals("DONE")){
            statusTask = true;
        } else {
            statusTask = false;
        }
        List<TodoTask> foundTasksByStatusList = todoTasks.stream()
                .filter(task -> task.getIsDone().equals(statusTask)).toList();

        return foundTasksByStatusList;
    }

    public List<TodoTask> findByStatusAndPriority(String status, String priority) {
        Priority priorityTask = Priority.valueOf(priority);
        List<TodoTask> result = findByStatus(status).stream()
                .filter(task -> task.getPriority().equals(priorityTask)).toList();
        return result;
    }

    public List<TodoTask> findByNameAndPriority(String name, String priority) {
        List<TodoTask> result = findByName(name).stream()
                .filter(task -> task.getPriority().equals(Priority.valueOf(priority))).toList();
        return result;
    }

    public List<TodoTask> findByNameAndStatus(String name, String status) {
        List<TodoTask> result = findByName(name).stream()
                .filter(task -> task.getIsDone().equals(status)).toList();
        return result;
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
        todoTasks.removeIf(task -> task.getId().equals(id));
    }

    public boolean existsById(Integer id) {
        return todoTasks.stream().anyMatch(task -> task.getId().equals(id));
    }

}
