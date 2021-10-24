package com.alexei.testtask.controller;

import com.alexei.testtask.entity.Bank;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/admin/banks")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }


    @GetMapping
    public ModelAndView getBanks() {
        ModelAndView modelAndView = new ModelAndView("banks");
        modelAndView.addObject("banks", bankService.findAllBanks());
        modelAndView.addObject("clients", bankService.findAllClients());
        modelAndView.addObject("credits", bankService.findAllCredits());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getBankById(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("банк с таким Id %s не найден", id));
        }
        ModelAndView modelAndView = new ModelAndView("bank");
        Bank bank = bankService.findBankById(UUID.fromString(id));
        modelAndView.addObject("bank", bank);
        modelAndView.addObject("credits", bankService.creditWithoutBank());
        modelAndView.addObject("clients", bankService.clientWithoutBank());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView createBank(@RequestParam String name,
                                   @RequestParam(required = false) String clientId,
                                   @RequestParam(required = false) String creditId) {
        if (!clientId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("клиент с таким Id %s не найден", clientId));
        }
        if (!creditId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("кредит с таким Id %s не найден", creditId));
        }
        List<Client> clients = new ArrayList<>();
        if (StringUtils.isNotEmpty(clientId)) {
            clients.add(bankService.findClientById(UUID.fromString(clientId)));
        }
        List<Credit> credits = new ArrayList<>();
        if (StringUtils.isNotEmpty(creditId)) {
            credits.add(bankService.findCreditById(UUID.fromString(creditId)));
        }
        if (StringUtils.isNotEmpty(name)) {
            Bank bank = new Bank(null, name, clients, credits);
            bankService.saveBank(bank);
            log.info("create bank {}", bank.getId());
            log.info("clients {}", bank.getClients());
            log.info("credits {}", bank.getCredits());
        }
        return new ModelAndView("redirect:/admin/banks");
    }

    @PostMapping("/{id}")
    public ModelAndView updateBank(@PathVariable String id,
                                   @RequestParam String name,
                                   @RequestParam(required = false) String clientId,
                                   @RequestParam(required = false) String creditId) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("банк с таким Id %s не найден", id));
        }
        if (!clientId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("клиент с таким Id %s не найден", clientId));
        }
        if (!creditId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("кредит с таким Id %s не найден", creditId));
        }
        List<Client> clients = bankService.findBankById(UUID.fromString(id)).getClients();
        if (StringUtils.isNotEmpty(clientId)) {
            clients.add(bankService.findClientById(UUID.fromString(clientId)));
        }
        List<Credit> credits = bankService.findBankById(UUID.fromString(id)).getCredits();
        if (StringUtils.isNotEmpty(creditId)) {
            credits.add(bankService.findCreditById(UUID.fromString(creditId)));
        }
        if (StringUtils.isNotEmpty(name)) {
            Bank bank = new Bank(UUID.fromString(id), name, clients, credits);
            bankService.saveBank(bank);
            log.info("update bank{}", bank.getId());
        }
        return new ModelAndView("redirect:/admin/banks");
    }

    @PostMapping("/{id}/remove")
    public ModelAndView delete(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("банк с таким Id %s не найден", id));
        }
        log.info("delete bank{}", bankService.findBankById(UUID.fromString(id)));
        bankService.deleteBankById(UUID.fromString(id));
        return new ModelAndView("redirect:/admin/banks");
    }

}
