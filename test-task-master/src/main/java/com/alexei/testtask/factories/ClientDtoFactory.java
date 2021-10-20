package com.alexei.testtask.factories;

import com.alexei.testtask.DTO.ClientDTO;
import com.alexei.testtask.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientDtoFactory {

    public ClientDTO makeClientDto (Client client) {
        return new ClientDTO(
                client.getId(),
                client.getFoolName(),
                client.getTelephoneNumber(),
                client.getEMailAdress(),
                client.getPassportNumber(),
                client.getBank().getId()
        );
    }
}
