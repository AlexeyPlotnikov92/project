package com.alexei.testtask.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditSorting {

    private String displayName;
    private String column;
    private Direction direction;

    public CreditSorting (String displayName, String column) {
        this.displayName = displayName;
        this.column = column;
    }

    public enum Direction {
        ASC,
        DESC
    }
}
