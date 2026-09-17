package dev.triacontakaihenagon.tasktrackerapi.entity;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tasks")
public class Task {
    public Task() {
    }

    public Task(TaskRequest request) {
        this.title = request.getTitle();
        this.status = (request.getStatus() != null) ? request.getStatus() : TaskStatus.TODO;
        this.priority = (request.getPriority() != null) ? request.getPriority() : TaskPriority.LOW;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.MEDIUM;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "task_labels",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private List<Label> labels = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}