package com.alexei.testtask.factories;

import com.alexei.testtask.DTO.CreditDto;
import com.alexei.testtask.entity.Credit;
import org.springframework.stereotype.Component;

@Component
public class CreditDtoFactory {

    public CreditDto makeCreditDto(Credit credit) {
        return new CreditDto(
                credit.getId(),
                credit.getCreditLimit(),
                credit.getInterestRate(),
                credit.getBank().getId()
        );
    }
}
