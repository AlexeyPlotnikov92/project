package com.alexei.testtask.DAO.impl;

import com.alexei.testtask.ApplicationTest;
import com.alexei.testtask.DAO.DAOClient;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.service.BankService;
import com.alexei.testtask.service.ClientFilter;
import com.alexei.testtask.service.ClientSorting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;
import java.util.UUID;

class DAOClientImplTest extends ApplicationTest {
    @Autowired
    DAOClient daoClient;
    @Autowired
    BankService bankService;

    @Test
    void checkClientsCrud() {
        int initialSize = daoClient.findAll().size();
        Random random = new Random();
        String expectedFoolName = UUID.randomUUID().toString();
        String expectedTelephoneNumber = UUID.randomUUID().toString();
        String expectedEMail = UUID.randomUUID().toString();
        Integer expectedPasspotrNumber = random.nextInt(1000000);
        Client client = daoClient.save(new Client(null, expectedFoolName, expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber, null));
        Assertions.assertNotNull(client.getId());
        Assertions.assertEquals(expectedFoolName, client.getFoolName());
        Assertions.assertEquals(expectedTelephoneNumber, client.getTelephoneNumber());
        Assertions.assertEquals(expectedEMail, client.getEMailAdress());
        Assertions.assertEquals(expectedPasspotrNumber, client.getPassportNumber());

        Assertions.assertEquals(initialSize + 1, daoClient.findAll().size());

        Client byId = daoClient.findById(client.getId()).orElseThrow();
        Assertions.assertEquals(client.getId(), byId.getId());
        Assertions.assertEquals(expectedFoolName, byId.getFoolName());
        Assertions.assertEquals(expectedTelephoneNumber, byId.getTelephoneNumber());
        Assertions.assertEquals(expectedEMail, byId.getEMailAdress());
        Assertions.assertEquals(expectedPasspotrNumber, byId.getPassportNumber());

        String updatedFoolName = UUID.randomUUID().toString();
        String updatedTelephoneNumber = UUID.randomUUID().toString();
        String updatedEMail = UUID.randomUUID().toString();
        Integer updatedPassportNumber = random.nextInt(1000000);
        daoClient.save(new Client(client.getId(), updatedFoolName, updatedTelephoneNumber, updatedEMail, updatedPassportNumber, null));
        Assertions.assertEquals(initialSize + 1, daoClient.findAll().size());
        Assertions.assertNotEquals(byId, daoClient.findById(client.getId()));

        byId = daoClient.findById(client.getId()).orElseThrow();
        Assertions.assertEquals(client.getId(), byId.getId());
        Assertions.assertEquals(updatedFoolName, byId.getFoolName());
        Assertions.assertEquals(updatedTelephoneNumber, byId.getTelephoneNumber());
        Assertions.assertEquals(updatedEMail, byId.getEMailAdress());
        Assertions.assertEquals(updatedPassportNumber, byId.getPassportNumber());

        daoClient.deleteById(client.getId());
        Assertions.assertEquals(initialSize, daoClient.findAll().size());
    }

    @Test
    void checkSortandFilter() {
        Random random = new Random();
        String expectedTelephoneNumber = UUID.randomUUID().toString();
        String expectedEMail = UUID.randomUUID().toString();
        Integer expectedPasspotrNumber = random.nextInt(1000000);
        Client client = new Client(null, "aaa", expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber, null);
        Client client1 = new Client(null, "bbb", expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber + 1, null);
        Client client2 = new Client(null, "zzz", expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber + 2, null);
        Client client3 = new Client(null, "ddd", expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber + 3, null);
        ClientSorting sorting = new ClientSorting("name", "foolName", ClientSorting.Direction.ASC);
        ClientSorting sortingDesc = new ClientSorting("name", "foolName", ClientSorting.Direction.DESC);
        daoClient.save(client);
        daoClient.save(client1);
        daoClient.save(client2);
        daoClient.save(client3);
        ClientFilter filter = new ClientFilter("aa");
        Client client4 = new Client(null, "aaf", expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber + 4, null);
        daoClient.save(client4);
        List<Client> sortingClients = bankService.findAllClients(null, sorting);
        List<Client>sortingClientsDesc = bankService.findAllClients(null, sortingDesc);
        List<Client> sortingAndFilter = bankService.findAllClients(filter, sorting);
        try {
            Assertions.assertEquals(client, sortingClients.get(0));
            Assertions.assertEquals(client2, sortingClientsDesc.get(0));
            Assertions.assertEquals(sortingAndFilter.size(), 2);
        }
        finally {
            daoClient.deleteById(client.getId());
            daoClient.deleteById(client1.getId());
            daoClient.deleteById(client2.getId());
            daoClient.deleteById(client3.getId());
            daoClient.deleteById(client4.getId());
        }
    }

}