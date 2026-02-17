package ru.albert.consoletodo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "dao_data")
@Data
public class DaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="value", nullable = false, length = 500)
    private String value;
    @CreationTimestamp
    @Column(name = "createdAt")
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(name = "updatedAt")
    private Timestamp updatedAt;

    public DaoEntity() {
    }

    public DaoEntity(String value) {
        this.value = value;
    }
}