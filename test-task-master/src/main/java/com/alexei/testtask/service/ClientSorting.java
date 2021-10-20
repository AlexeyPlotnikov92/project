package com.alexei.testtask.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientSorting {

    private String displayName;
    private String column;
    private Direction direction;

    public ClientSorting(String displayName, String column) {
        this.displayName = displayName;
        this.column = column;
    }

    public enum Direction {
        ASC,
        DESC
    }
}
