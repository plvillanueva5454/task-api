package com.exam.todolistapi.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class Task implements Comparable<Task>{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull
    private String taskName;

    @Column
    private String taskInstructions;

    @Column
    @NotNull
    private int position;

    @Override
    public int compareTo(Task compareTask) {
        int comparePosition = compareTask.getPosition();
        return this.position - comparePosition;
    }
}
