package com.alexei.testtask.factories;

import com.alexei.testtask.DTO.CreditDTO;
import com.alexei.testtask.entity.Credit;
import org.springframework.stereotype.Component;

@Component
public class CreditDtoFactory {

    public CreditDTO makeCreditDto(Credit credit) {
        return new CreditDTO(
                credit.getId(),
                credit.getCreditLimit(),
                credit.getInterestRate(),
                credit.getBank().getId()
        );
    }
}
