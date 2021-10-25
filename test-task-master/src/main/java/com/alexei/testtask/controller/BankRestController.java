package com.alexei.testtask.controller;

import com.alexei.testtask.DTO.AсkDto;
import com.alexei.testtask.DTO.BankDto;
import com.alexei.testtask.entity.Bank;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.factories.BankDtoFactory;
import com.alexei.testtask.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Transactional
@RestController
public class BankRestController {

    public static final String GET_BANKS = "/api/v1/banks";
    public static final String GET_BANK = "/api/v1/banks/{id}";
    public static final String CREATE_BANKS = "/api/v1/banks";
    public static final String EDIT_BANKS = "/api/v1/banks/{id}";
    public static final String DELETE_BANKS = "/api/v1/banks/{id}";
    private final BankDtoFactory bankDtoFactory;
    private final BankService bankService;

    public BankRestController(BankDtoFactory bankDtoFactory, BankService bankService) {
        this.bankDtoFactory = bankDtoFactory;
        this.bankService = bankService;
    }

    @GetMapping(GET_BANKS)
    public List<BankDto> getBanks() {
        List<Bank> banksEntity = bankService.findAllBanks();
        List<BankDto> banks = new ArrayList<>();
        for (Bank bank : banksEntity) {
            banks.add(bankDtoFactory.makeBankDto(bank));
        }
        return banks;
    }

    @GetMapping(GET_BANK)
    public BankDto getBank(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("bank with this Id %s was not found", id));
        }
        Bank bank = bankService.findBankById(UUID.fromString(id));
        return bankDtoFactory.makeBankDto(bank);

    }

    @PostMapping(CREATE_BANKS)
    public BankDto createBank(@RequestParam String name,
                              @RequestParam(required = false) String clientId,
                              @RequestParam(required = false) String creditId) {
        if (!clientId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") && StringUtils.isNotEmpty(clientId)) {
            throw new IllegalArgumentException(String.format("client with this Id %s was not found", clientId));
        }
        if (!creditId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") && StringUtils.isNotEmpty(creditId)) {
            throw new IllegalArgumentException(String.format("credit with this Id %s was not found", creditId));
        }
        List<Client> clients = new ArrayList<>();
        if (StringUtils.isNotEmpty(clientId)) {
            clients.add(bankService.findClientById(UUID.fromString(clientId)));
        }
        List<Credit> credits = new ArrayList<>();
        if (StringUtils.isNotEmpty(creditId)) {
            credits.add(bankService.findCreditById(UUID.fromString(creditId)));
        }
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("bank have not name");
        }
        Bank bank = new Bank(null, name, clients, credits);
        bank = bankService.saveBank(bank);
        log.info("create bank {}", bank.getId());
        log.info("clients {}", bank.getClients());
        log.info("credits {}", bank.getCredits());
        return bankDtoFactory.makeBankDto(bank);
    }

    @PatchMapping(EDIT_BANKS)
    public BankDto updateBank(@PathVariable String id,
                              @RequestParam String name,
                              @RequestParam(required = false) String clientId,
                              @RequestParam(required = false) String creditId) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("bank with this Id %s was not found", id));
        }
        if (!clientId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") && StringUtils.isNotEmpty(clientId)) {
            throw new IllegalArgumentException(String.format("client with this Id %s was not found", clientId));
        }
        if (!creditId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") && StringUtils.isNotEmpty(creditId)) {
            throw new IllegalArgumentException(String.format("credit with this Id %s was not found", creditId));
        }
        List<Client> clients = bankService.findBankById(UUID.fromString(id)).getClients();
        if (StringUtils.isNotEmpty(clientId)) {
            clients.add(bankService.findClientById(UUID.fromString(clientId)));
        }
        List<Credit> credits = bankService.findBankById(UUID.fromString(id)).getCredits();
        if (StringUtils.isNotEmpty(creditId)) {
            credits.add(bankService.findCreditById(UUID.fromString(creditId)));
        }
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("bank have not name");
        }
        Bank bank = new Bank(UUID.fromString(id), name, clients, credits);
        bank = bankService.saveBank(bank);
        log.info("create bank {}", bank.getId());
        log.info("clients {}", bank.getClients());
        log.info("credits {}", bank.getCredits());
        return bankDtoFactory.makeBankDto(bank);
    }

    @DeleteMapping(DELETE_BANKS)
    public AсkDto deleteOffer(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("bank with this Id %s was not found", id));
        }
        bankService.deleteBankById(UUID.fromString(id));
        return AсkDto.makeAnswer(true);
    }

}
