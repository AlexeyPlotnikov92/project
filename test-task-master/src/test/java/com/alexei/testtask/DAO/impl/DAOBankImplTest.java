package com.alexei.testtask.DAO.impl;

import com.alexei.testtask.ApplicationTest;
import com.alexei.testtask.DAO.DAOBank;
import com.alexei.testtask.DAO.DAOClient;
import com.alexei.testtask.DAO.DAOCredit;
import com.alexei.testtask.entity.Bank;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.service.BankService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

class DAOBankImplTest extends ApplicationTest {
    @Autowired
    DAOBank daoBank;

    @Autowired
    DAOClient daoClient;

    @Autowired
    DAOCredit daoCredit;

    @Autowired
    BankService bankService;

    @Test
    void checkCrud() {
        int initialSize = bankService.findAllBanks().size();
        Random random = new Random();
        String expectedFoolName = UUID.randomUUID().toString();
        String expectedTelephoneNumber = UUID.randomUUID().toString();
        String expectedEMail = UUID.randomUUID().toString();
        int expectedPasspotrNumber = random.nextInt(1000000);
        Client client = daoClient.save(new Client(null, expectedFoolName, expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber, null));

        Integer expectedCreditLimit = random.nextInt(100000);
        Integer expectedInterestRate = random.nextInt(100);
        Credit credit = daoCredit.save(new Credit(null, expectedCreditLimit, expectedInterestRate, null));

        String expectedBankName = UUID.randomUUID().toString();
        Bank bank = new Bank(null, expectedBankName, List.of(client), List.of(credit));
        bank = bankService.saveBank(bank);
        try {
            Assertions.assertNotNull(bank.getId());
            Assertions.assertEquals(expectedBankName, bank.getName());
            Assertions.assertEquals(client.getBank().getId(), bank.getId());
            Assertions.assertEquals(credit.getBank().getId(), bank.getId());

            int sizeClients = bank.getClients().size();
            int sizeCredits = bank.getCredits().size();
            Client client1 = daoClient.save(new Client(null, expectedFoolName, expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber + 1, null));
            Credit credit1 = daoCredit.save(new Credit(null, expectedCreditLimit, expectedInterestRate, null));
            bank = daoBank.save(new Bank(bank.getId(), expectedBankName, List.of(client, client1), List.of(credit, credit1)));
//            Assertions.assertEquals(sizeClients + 1, bank.getClients().size());
//            Assertions.assertEquals(sizeCredits + 1, bank.getCredits().size());

            Assertions.assertEquals(initialSize + 1, daoBank.findAll().size());

            Bank byId = bankService.findBankById(bank.getId());
            Assertions.assertEquals(expectedBankName, byId.getName());

            String updatedBankName = UUID.randomUUID().toString();
            daoBank.save(new Bank(bank.getId(), updatedBankName, List.of(client, client1), List.of(credit, credit1)));
            Assertions.assertEquals(initialSize + 1, daoBank.findAll().size());

            byId = bankService.findBankById(bank.getId());
            Assertions.assertEquals(bank.getId(), byId.getId());
            Assertions.assertEquals(updatedBankName, byId.getName());
        } finally {
            bankService.deleteBankById(bank.getId());
        }
        Assertions.assertEquals(initialSize, daoBank.findAll().size());
    }

    @Test
    void checkCreditAndClientOfBank() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String expectedFoolName = UUID.randomUUID().toString();
            String expectedTelephoneNumber = UUID.randomUUID().toString();
            String expectedEMail = UUID.randomUUID().toString();
            int expectedPasspotrNumber = random.nextInt(1000000);
            daoClient.save(new Client(null, expectedFoolName, expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber, null));
        }
        for (int i = 0; i < 10; i++) {
            Integer expectedCreditLimit = random.nextInt(100000);
            Integer expectedInterestRate = random.nextInt(100);
            daoCredit.save(new Credit(null, expectedCreditLimit, expectedInterestRate, null));
        }
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            clients.add(daoClient.findAll().get(i));
        }
        List<Credit> credits = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            credits.add(daoCredit.findAll().get(i));
        }
        String expectedBankName = UUID.randomUUID().toString();
        Bank bank = new Bank(null, expectedBankName, clients, credits);
        try {
            bank = daoBank.save(bank);
            Assertions.assertEquals(daoClient.findAll().size(), daoClient.clientsWithoutBank().size());
            Assertions.assertEquals(daoCredit.findAll().size(), daoCredit.creditsWithoutBank().size());
        } finally {
            daoBank.deleteById(bank.getId());
        }
    }
}