package com.alexei.testtask.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AskDto {

    private Boolean answer;

    public static AskDto makeAnswer(Boolean answer) {
        return new AskDto(answer);
    }
}
