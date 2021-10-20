package com.alexei.testtask.factories;

import com.alexei.testtask.DTO.BankDTO;
import com.alexei.testtask.entity.Bank;
import org.springframework.stereotype.Component;

@Component
public class BankDtoFactory {

    public BankDTO makeBankDto(Bank bank) {
        return new BankDTO(
                bank.getId(),
                bank.getName(),
                bank.getClients(),
                bank.getCredits()
        );
    }
}
