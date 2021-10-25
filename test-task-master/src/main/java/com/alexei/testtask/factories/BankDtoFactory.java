package com.alexei.testtask.factories;

import com.alexei.testtask.DTO.BankDto;
import com.alexei.testtask.DTO.ClientDto;
import com.alexei.testtask.DTO.CreditDto;
import com.alexei.testtask.entity.Bank;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BankDtoFactory {

    private final ClientDtoFactory clientDtoFactory;
    private final CreditDtoFactory creditDtoFactory;

    public BankDtoFactory(ClientDtoFactory clientDtoFactory, CreditDtoFactory creditDtoFactory) {
        this.clientDtoFactory = clientDtoFactory;
        this.creditDtoFactory = creditDtoFactory;
    }


    public BankDto makeBankDto(Bank bank) {
        return new BankDto(
                bank.getId(),
                bank.getName(),
                clientDtoList(bank.getClients()),
                creditDtoList(bank.getCredits())
        );
    }

    public List<ClientDto> clientDtoList(List<Client> clients) {
        List<ClientDto> list = new ArrayList<>();
        for (Client client : clients) {
            ClientDto clientDto = clientDtoFactory.makeClientDto(client);
            list.add(clientDto);
        }
        return list;
    }

    public List<CreditDto> creditDtoList(List<Credit> credits) {
        List<CreditDto> list = new ArrayList<>();
        for (Credit credit : credits) {
            CreditDto creditDto = creditDtoFactory.makeCreditDto(credit);
            list.add(creditDto);
        }
        return list;
    }
}
