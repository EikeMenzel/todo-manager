package org.databaseservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name = "todo")
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id")
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TodoStatus status = TodoStatus.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TodoPriority priority = TodoPriority.LOW;

    public TodoEntity(String text, CategoryEntity category, TodoStatus status, TodoPriority priority) {
        this.text = text;
        this.category = category;
        this.status = status;
        this.priority = priority;
    }
}
