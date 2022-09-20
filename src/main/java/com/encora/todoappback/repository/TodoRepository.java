package com.encora.todoappback.repository;

import com.encora.todoappback.domain.Priority;
import com.encora.todoappback.domain.TodoTask;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TodoRepository {

    private Integer idCounter = 1;
    private List<TodoTask> todoTasks = new ArrayList<>();

    public List<TodoTask> getAllTodoTasks() {
        return todoTasks;
    }

    public List<TodoTask> getByNameTodoTasks(String name) {
        return findByName(todoTasks, name);
    }

    public List<TodoTask> getByNameAndStatusTodoTasks(String name, String status) {
        List<TodoTask> foundByNameList = findByName(todoTasks, name);
        List<TodoTask> resultList = findByStatus(foundByNameList, status);
        return resultList;
    }

    public List<TodoTask> getByNameAndPriorityTodoTasks(String name, String priority) {
        List<TodoTask> foundByNameList = findByName(todoTasks, name);
        List<TodoTask> resultList = findByPriority(foundByNameList, priority);
        return resultList;
    }

    public List<TodoTask> getByNameAndPriorityAndStatusOfTask(String name, String status, String priority) {
        List<TodoTask> foundByNameList = findByName(todoTasks, name);
        List<TodoTask> foundByPriorityList = findByPriority(foundByNameList, priority);
        List<TodoTask> resultList = findByStatus(foundByPriorityList, status);

        return resultList;
    }

    public List<TodoTask> getByStatusTodoTasks(String status) {
        return findByStatus(todoTasks, status);
    }

    public List<TodoTask> getByPriorityTodoTasks(String priority) {
        return findByPriority(todoTasks, priority);
    }

    public List<TodoTask> getByStatusAndPriorityTodoTasks(String status, String priority) {
        List<TodoTask> foundByStatusList = findByStatus(todoTasks, status);
        List<TodoTask> resultList = findByPriority(foundByStatusList, priority);

        return resultList;
    }

    public List<TodoTask> findByName(List<TodoTask> todoList, String name) {
        List<TodoTask> foundTasksByNameList = todoList.stream()
                .filter(task -> task.getName().toLowerCase().contains(name.toLowerCase())).toList();
        return foundTasksByNameList;
    }

    public List<TodoTask> findByPriority(List<TodoTask> todoList, String priority) {
        List<TodoTask> foundTasksByPriorityList = todoList.stream()
                .filter(task -> task.getPriority().equals(Priority.valueOf(priority))).toList();

        return foundTasksByPriorityList;
    }

    public List<TodoTask> findByStatus(List<TodoTask> todoList, String status) {
        Boolean statusTask = status.equals("DONE");

        List<TodoTask> foundTasksByStatusList = todoList.stream()
                .filter(task -> task.getIsDone().equals(statusTask)).toList();

        return foundTasksByStatusList;
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

    public HashMap<String, Object> getTodosMetrics(){

        HashMap<String, Object> metricsValues = new HashMap<>();

        int totalAverageTime = 0;
        int highPriorityAverageTime = 0;
        int mediumPriorityAverageTime = 0;
        int lowPriorityAverageTime = 0;

        int totalFinishedTodos = 0;
        int highFinishedTodos = 0;
        int mediumFinishedTodos = 0;
        int lowFinishedTodos = 0;

        for (TodoTask todo: todoTasks) {
            if (todo.getIsDone()) {
                Duration duration = Duration.between(todo.getCreationDate(), todo.getDoneDate());
                totalAverageTime += duration.getSeconds();
                totalFinishedTodos++;
                Priority priority = todo.getPriority();
                switch (priority) {
                    case HIGH -> {
                        highPriorityAverageTime += duration.getSeconds();
                        highFinishedTodos++;
                    }
                    case MEDIUM -> {
                        mediumPriorityAverageTime += duration.getSeconds();
                        mediumFinishedTodos++;
                    }
                    case LOW -> {
                        lowPriorityAverageTime += duration.getSeconds();
                        lowFinishedTodos++;
                    }
                }
            }
        }

        totalAverageTime = totalFinishedTodos > 0 ? totalAverageTime / totalFinishedTodos : 0;
        highPriorityAverageTime =  highFinishedTodos > 0 ? highPriorityAverageTime / highFinishedTodos : 0;
        mediumPriorityAverageTime = mediumFinishedTodos > 0 ? mediumPriorityAverageTime / mediumFinishedTodos : 0;
        lowPriorityAverageTime = lowFinishedTodos > 0 ? lowPriorityAverageTime / lowFinishedTodos : 0;

        metricsValues.put("total", formattedTime(totalAverageTime));
        metricsValues.put("highPriority", formattedTime(highPriorityAverageTime));
        metricsValues.put("mediumPriority", formattedTime(mediumPriorityAverageTime));
        metricsValues.put("lowPriority", formattedTime(lowPriorityAverageTime));

        return metricsValues;
    }

    public String formattedTime(Integer totalTimeInSeconds) {
        if (totalTimeInSeconds == 0) {
            return "No data!";
        }

        String formattedTimeResult = "";
        int timeLeftInSeconds = totalTimeInSeconds % 60;
        int timeInMinutes = (totalTimeInSeconds / 60) % 60;
        int timeInHours = ((totalTimeInSeconds / 60) / 60) % 24;
        int timeInDays = ((totalTimeInSeconds / 60) / 60) / 24;

        if(timeInDays == 0) {
            if (timeInHours == 0) {
                if (timeInMinutes == 0) {
                    formattedTimeResult = timeLeftInSeconds + " Seconds.";
                } else {
                    formattedTimeResult = timeInMinutes + ":" + timeLeftInSeconds + " Minutes.";
                }
            } else {
                formattedTimeResult = timeInHours + ":" + timeInMinutes + ":" + timeLeftInSeconds + " Hours.";
            }
        } else {
            formattedTimeResult = timeInDays + " Days, " + timeInHours + ":" + timeInMinutes + ":" + timeLeftInSeconds + " Hours.";
        }

        return formattedTimeResult;
    }

}
