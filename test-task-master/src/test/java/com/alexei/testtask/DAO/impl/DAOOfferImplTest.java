package com.alexei.testtask.DAO.impl;

import com.alexei.testtask.ApplicationTest;
import com.alexei.testtask.DAO.DAOClient;
import com.alexei.testtask.DAO.DAOCredit;
import com.alexei.testtask.DAO.DAOOffer;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.entity.Offer;
import com.alexei.testtask.service.BankService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.UUID;

class DAOOfferImplTest extends ApplicationTest {
    @Autowired
    DAOClient daoClient;

    @Autowired
    DAOCredit daoCredit;

    @Autowired
    DAOOffer daoOffer;

    @Autowired
    BankService bankService;

    @Test
    void checkCrud() {
        int initialSize = daoOffer.findAll().size();
        Random random = new Random();
        String expectedFoolName = UUID.randomUUID().toString();
        String expectedTelephoneNumber = UUID.randomUUID().toString();
        String expectedEMail = UUID.randomUUID().toString();
        int expectedPasspotrNumber = random.nextInt(1000000);
        Client client = daoClient.save(new Client(null, expectedFoolName, expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber, null));

        int expectedCreditLimit = random.nextInt(100000);
        int expectedInterestRate = random.nextInt(100);
        Credit credit = daoCredit.save(new Credit(null, expectedCreditLimit, expectedInterestRate, null));

        int expectedCreditAmount = random.nextInt(expectedCreditLimit);
        Offer offer = daoOffer.save(new Offer(null, client, credit, expectedCreditAmount));
        Assertions.assertNotNull(offer.getId());
        Assertions.assertEquals(client, offer.getClient());
        Assertions.assertEquals(credit, offer.getCredit());
        Assertions.assertEquals(expectedCreditAmount, offer.getCreditAmount());

        Offer byId = daoOffer.findById(offer.getId()).orElseThrow();
        Assertions.assertEquals(client.getId(), byId.getClient().getId());
        Assertions.assertEquals(credit.getId(), byId.getCredit().getId());
        Assertions.assertEquals(expectedCreditAmount, offer.getCreditAmount());

        int updatedCreditAmount = random.nextInt(expectedCreditAmount);
        Client client1 = daoClient.save(new Client(null, expectedFoolName, expectedTelephoneNumber, expectedEMail, expectedPasspotrNumber + 1, null));
        Credit credit1 = daoCredit.save(new Credit(null, expectedCreditLimit + 1, expectedInterestRate - 1, null));
        daoOffer.save(new Offer(offer.getId(), client1, credit1, updatedCreditAmount));
        Assertions.assertEquals(initialSize + 1, daoOffer.findAll().size());

        byId = daoOffer.findById(offer.getId()).orElseThrow();
        Assertions.assertEquals(offer.getId(), byId.getId());
        Assertions.assertEquals(client1.getId(), byId.getClient().getId());
        Assertions.assertEquals(credit1.getId(), byId.getCredit().getId());
        Assertions.assertEquals(updatedCreditAmount, byId.getCreditAmount());
        Offer offer1 = new Offer(null, client, credit, expectedCreditLimit + 3);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            bankService.saveOffer(offer1);
        });
    }
}