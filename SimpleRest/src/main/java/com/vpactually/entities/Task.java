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
public class Task implements BaseEntity{
    private Integer id;
    private String title;
    private String description;
    private LocalDate createdAt;
    private TaskStatus taskStatus;
    private User assignee;
    private Set<Label> labels;

    public Set<Label> getLabels() {
        if(labels == null) {
            labels = new HashSet<>();
        }
        return labels;
    }

    public void addLabel(Label label) {
        getLabels().add(label);
    }

    public void removeLabel(Label label) {
        getLabels().remove(label);
    }
}
