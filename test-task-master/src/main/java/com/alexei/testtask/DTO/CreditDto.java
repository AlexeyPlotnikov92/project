package com.alexei.testtask.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDto {

    private UUID id;

    private Integer creditLimit;

    private Integer interestRate;

    private UUID bankId;

}

