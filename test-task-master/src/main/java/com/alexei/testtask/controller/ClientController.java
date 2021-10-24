package com.alexei.testtask.controller;

import com.alexei.testtask.entity.Client;
import com.alexei.testtask.service.BankService;
import com.alexei.testtask.service.ClientFilter;
import com.alexei.testtask.service.ClientSorting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/clients")
public class ClientController {
    private final BankService bankService;


    public ClientController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public ModelAndView getClients(
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "dir", required = false) String direction,
            @RequestParam(value = "search", required = false) String searchClient
    ) {
        List<ClientSorting> sortingProperties = bankService.getSortingClientsProperties();
        ClientSorting selectedSorting = getSelectedSorting(sortingProperties, order, direction);
        ClientFilter clientFilter = new ClientFilter(searchClient);
        ModelAndView modelAndView = new ModelAndView("clients");
        modelAndView.addObject("clients", bankService.findAllClients(clientFilter, selectedSorting));
        modelAndView.addObject("sorting", sortingProperties);
        modelAndView.addObject("selectedSorting", selectedSorting);
        modelAndView.addObject("searchClient", searchClient);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getClient(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
            throw new IllegalArgumentException(String.format("клиент с таким Id %s не найден", id));
        }
        ModelAndView modelAndView = new ModelAndView("client");
        modelAndView.addObject("client", bankService.findClientById(UUID.fromString(id)));
        return modelAndView;
    }

    @PostMapping
    public ModelAndView createClient(@RequestParam String foolName,
                                     @RequestParam String phoneNumber,
                                     @RequestParam String eMail,
                                     @RequestParam Integer passportNumber) {
        if (StringUtils.isNotEmpty(foolName)) {
            Client client = new Client(null, foolName, phoneNumber, eMail, passportNumber, null);
            bankService.saveClient(client);
        }
        return new ModelAndView("redirect:/admin/clients");
    }

    @PostMapping("/{id}")
    public ModelAndView updateClient(@PathVariable String id,
                                     @RequestParam String foolName,
                                     @RequestParam String phoneNumber,
                                     @RequestParam String eMail,
                                     @RequestParam Integer passportNumber) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
            throw new IllegalArgumentException(String.format("клиент с таким Id %s не найден", id));
        }
        if (StringUtils.isNotEmpty(foolName)) {
            Client client = new Client(UUID.fromString(id), foolName, phoneNumber, eMail, passportNumber,
                    bankService.findClientById(UUID.fromString(id)).getBank());
            bankService.saveClient(client);
        }
        return new ModelAndView("redirect:/admin/clients");
    }

    @PostMapping("/{id}/remove")
    public ModelAndView delete(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
            throw new IllegalArgumentException(String.format("клиент с таким Id %s не найден", id));
        }
        bankService.deleteClientById(UUID.fromString(id));
        return new ModelAndView("redirect:/admin/clients");
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
