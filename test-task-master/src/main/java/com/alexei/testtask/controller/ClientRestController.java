package com.alexei.testtask.controller;

import com.alexei.testtask.DTO.AсkDto;
import com.alexei.testtask.DTO.ClientDto;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.factories.ClientDtoFactory;
import com.alexei.testtask.service.BankService;
import com.alexei.testtask.service.ClientFilter;
import com.alexei.testtask.service.ClientSorting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@RestController
public class ClientRestController {

    public static final String GET_CLIENTS = "/api/v1/clients";
    public static final String GET_CLIENT = "/api/v1/clients/{id}";
    public static final String CREATE_CLIENTS = "/api/v1/clients";
    public static final String EDIT_CLIENTS = "/api/v1/clients/{id}";
    public static final String DELETE_CLIENTS = "/api/v1/clients/{id}";
    private final ClientDtoFactory clientDtoFactory;
    private final BankService bankService;

    public ClientRestController(ClientDtoFactory clientDtoFactory, BankService bankService) {
        this.clientDtoFactory = clientDtoFactory;
        this.bankService = bankService;
    }

    @GetMapping(GET_CLIENTS)
    public List<ClientDto> getClients(
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "dir", required = false) String direction,
            @RequestParam(value = "search", required = false) String searchClient
    ) {
        List<ClientSorting> sortingProperties = bankService.getSortingClientsProperties();
        ClientSorting selectedSorting = getSelectedSorting(sortingProperties, order, direction);
        ClientFilter clientFilter = new ClientFilter(searchClient);
        List<Client> clientsEntity = bankService.findAllClients(clientFilter,selectedSorting);
        List<ClientDto> clients = new ArrayList<>();
        for (Client client:clientsEntity) {
            clients.add(clientDtoFactory.makeClientDto(client));
        }
        return clients;
    }

    @GetMapping(GET_CLIENT)
    public ClientDto getClient(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
            throw new IllegalArgumentException(String.format("клиент с таким Id %s не найден", id));
        }
        Client client = bankService.findClientById(UUID.fromString(id));
        return clientDtoFactory.makeClientDto(client);
    }



    @PostMapping(CREATE_CLIENTS)
    public ClientDto createClient(@RequestParam String foolName,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String eMail,
                                  @RequestParam Integer passportNumber) {
        if (StringUtils.isEmpty(foolName)) {
            throw new IllegalArgumentException("foolName can't be empty");
        }
        Client client = new Client(null, foolName, phoneNumber, eMail, passportNumber, null);
        client = bankService.saveClient(client);
        return clientDtoFactory.makeClientDto(client);
    }

    @PatchMapping(EDIT_CLIENTS)
    public ClientDto updateClient(@PathVariable String id,
                                  @RequestParam String foolName,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String eMail,
                                  @RequestParam Integer passportNumber) {
        if (StringUtils.isEmpty(foolName)) {
            throw new IllegalArgumentException("foolName can't be empty");
        }
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
            throw new IllegalArgumentException(String.format("клиент с таким Id %s не найден", id));
        }
        Client client = new Client(UUID.fromString(id), foolName, phoneNumber, eMail, passportNumber,
                bankService.findClientById(UUID.fromString(id)).getBank());
        client = bankService.saveClient(client);
        return clientDtoFactory.makeClientDto(client);
    }

    @DeleteMapping(DELETE_CLIENTS)
    public AсkDto deleteClient(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
            throw new IllegalArgumentException(String.format("клиент с таким Id %s не найден", id));
        }
        bankService.deleteClientById(UUID.fromString(id));
        return AсkDto.makeAnswer(true);
    }

    private ClientSorting getSelectedSorting(List<ClientSorting> sortingProperties, String order, String dir) {
        if (StringUtils.isNotEmpty(order)) {
            ClientSorting.Direction direction;
            try {
                direction = ClientSorting.Direction.valueOf(dir);
            } catch (IllegalArgumentException e) {
                direction = ClientSorting.Direction.ASC;
            }
            ClientSorting clientSorting = sortingProperties.stream()
                    .filter(sp -> sp.getColumn().equals(order))
                    .findFirst()
                    .orElse(null);
            if (clientSorting != null) {
                clientSorting.setDirection(direction);
            } else {
                clientSorting = new ClientSorting("id", "id", ClientSorting.Direction.ASC);
            }
            return clientSorting;
        } else {
            return new ClientSorting("id", "id", ClientSorting.Direction.ASC);
        }
    }
}
