package model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DaoEntity {
    private long id;
    private String value;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

