package com.encora.todoappback.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
//import javax.persistence.Entity;

//@Entity
public class TodoTask {

     //@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalDate dueDate;
    private Boolean isDone;
    private LocalDateTime doneDate;
    private Priority priority;
    private LocalDateTime creationDate;

    public TodoTask() { }

    public TodoTask(Integer id, String name, LocalDate dueDate, Boolean isDone, LocalDateTime doneDate, Priority priority, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.dueDate = dueDate;
        this.isDone = isDone;
        this.doneDate = doneDate;
        this.priority = priority;
        this.creationDate = creationDate;
    }

    public TodoTask(Integer id, String name, Boolean isDone, LocalDateTime doneDate, Priority priority, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.isDone = isDone;
        this.doneDate = doneDate;
        this.priority = priority;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
