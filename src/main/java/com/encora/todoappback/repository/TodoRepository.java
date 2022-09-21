package com.encora.todoappback.repository;

import com.encora.todoappback.domain.Priority;
import com.encora.todoappback.domain.TodoTask;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.*;
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

    public List<TodoTask> sortingBy(List<TodoTask> todoTasksList, Integer sorting, String order) {
        switch (sorting) {
            case 1 -> {
                return sortByPriority(todoTasksList, order);
            }
            case 2 -> {
                return sortByDueDate(todoTasksList, order);
            }
            case 3 -> {
                //HERE: PRIORITY IS THE TOP-LEVEL HIERARCHY
                return sortByPriorityAndDueDate(todoTasksList);
            }
            case 4 -> {
                //HERE: DUE DATE IS THE TOP-LEVEL HIERARCHY
                return sortByDueDateAndPriority(todoTasksList);
            }
        }

        return todoTasksList;
    }

    public List<TodoTask> sortByPriority(List<TodoTask> todoTasksList, String order) {
        List<TodoTask> sortedByPriorityList = todoTasksList.stream()
                .sorted(Comparator.comparing(todo -> todo.getPriorityHierarchy()))
                .collect(Collectors.toList());

        if (order.equals("DESC")) {
            Collections.reverse(sortedByPriorityList);
        }

        return sortedByPriorityList;

    }

    public List<TodoTask> sortByDueDate(List<TodoTask> todoTasksList, String order) {
        List<TodoTask> sortedByDueDateList;

        if (order.equals("DESC")) {
            //IN DESCENDANT ORDER
            sortedByDueDateList = todoTasksList.stream()
                    .sorted(Comparator.comparing(TodoTask::getDueDate,
                            Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                    .collect(Collectors.toList());
            return sortedByDueDateList;
        }

        sortedByDueDateList = todoTasksList.stream()
                .sorted(Comparator.comparing(TodoTask::getDueDate,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        return sortedByDueDateList;
    }

    public List<TodoTask> sortByPriorityAndDueDate(List<TodoTask> todoTasksList) {
        List<TodoTask> sortedByPriorityAndDueDateList = todoTasksList.stream()
                .sorted(Comparator.comparing(TodoTask::getPriorityHierarchy)
                        .thenComparing(TodoTask::getDueDate,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        return sortedByPriorityAndDueDateList;
    }

    public List<TodoTask> sortByDueDateAndPriority(List<TodoTask> todoTasksList) {
        List<TodoTask> sortedByDueDateAndPriorityList = todoTasksList.stream()
                .sorted(Comparator.comparing(TodoTask::getDueDate,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(TodoTask::getPriorityHierarchy))
                .collect(Collectors.toList());

        return sortedByDueDateAndPriorityList;
    }


    public List<TodoTask> getTodoTasksByPage(List<TodoTask> todoTasksList, Integer page) {
        final int todoTasksPerPage = 10;
        int fromStart = (page - 1) * todoTasksPerPage;
        int toLimit = Math.min(fromStart + todoTasksPerPage, todoTasksList.size());
        List<TodoTask> paginatedList = todoTasksList.subList(fromStart, toLimit);

        return paginatedList;
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

        totalAverageTime = totalAverageTime > 0 ? totalAverageTime / totalFinishedTodos : 0;
        highPriorityAverageTime =  highPriorityAverageTime > 0 ? highPriorityAverageTime / highFinishedTodos : 0;
        mediumPriorityAverageTime = mediumPriorityAverageTime > 0 ? mediumPriorityAverageTime / mediumFinishedTodos : 0;
        lowPriorityAverageTime = lowPriorityAverageTime > 0 ? lowPriorityAverageTime / lowFinishedTodos : 0;

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
        int seconds = totalTimeInSeconds % 60;
        int minutes = (totalTimeInSeconds / 60) % 60;
        int hours = ((totalTimeInSeconds / 60) / 60) % 24;
        int days = ((totalTimeInSeconds / 60) / 60) / 24;

        if(days == 0) {
            if (hours == 0) {
                if (minutes == 0) {
                    formattedTimeResult = timeToString(seconds) + " Seconds";
                } else {
                    formattedTimeResult = timeToString(minutes) + ":" + timeToString(seconds) + " Minutes";
                }
            } else {
                formattedTimeResult = timeToString(hours) + ":" + timeToString(minutes) + ":" + timeToString(seconds) + " Hours";
            }
        } else {
            formattedTimeResult = days + " Days, " + timeToString(hours) + ":" + timeToString(minutes) + ":" + timeToString(seconds) + " Hours";
        }

        return formattedTimeResult;
    }

    public String timeToString(Integer time) {
        String formattedTimeString = time < 10 ? "0" + time : Integer.toString(time);
        return formattedTimeString;
    }


}
