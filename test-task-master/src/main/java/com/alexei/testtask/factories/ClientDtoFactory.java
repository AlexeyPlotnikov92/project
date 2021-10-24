package com.alexei.testtask.factories;

import com.alexei.testtask.DTO.ClientDto;
import com.alexei.testtask.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientDtoFactory {

    public ClientDto makeClientDto(Client client) {
        return new ClientDto(
                client.getId(),
                client.getFoolName(),
                client.getTelephoneNumber(),
                client.getEMailAdress(),
                client.getPassportNumber(),
                client.getBank() != null ? client.getBank().getId() : null
        );
    }
}
