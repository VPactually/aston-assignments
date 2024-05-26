package com.vpactually.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Label implements BaseEntity{
    private Integer id;
    private String name;
    private LocalDate createdAt;
    private Set<Task> tasks = new HashSet<>();

    public Label(Integer id) {
        this.id = id;
    }

    public Set<Task> getTasks() {
        if (tasks == null) {
            tasks = new HashSet<>();
        }
        return tasks;
    }

    public void addTask(Task task) {
        getTasks().add(task);
    }

    public void removeTask(Task task) {
        getTasks().remove(task);
    }
}
