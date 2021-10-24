package com.alexei.testtask.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AсkDto {

    private Boolean answer;

    public static AсkDto makeAnswer(Boolean answer) {
        return new AсkDto(answer);
    }
}
