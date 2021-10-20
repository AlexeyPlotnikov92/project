package com.alexei.testtask.DAO.impl;

import com.alexei.testtask.ApplicationTest;
import com.alexei.testtask.DAO.DAOCredit;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.service.BankService;
import com.alexei.testtask.service.CreditSorting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

class DAOCreditImplTest extends ApplicationTest {
    @Autowired
    DAOCredit daoCredit;

    @Autowired
    BankService bankService;

    @Test
    void checkCrud() {
        int initialSize = daoCredit.findAll().size();
        Random random = new Random();
        Integer expectedCreditLimit = random.nextInt(100000);
        Integer expectedInterestRate = random.nextInt(100);
        Credit credit = daoCredit.save(new Credit(null, expectedCreditLimit, expectedInterestRate, null));
        Assertions.assertNotNull(credit.getId());
        Assertions.assertEquals(expectedCreditLimit, credit.getCreditLimit());
        Assertions.assertEquals(expectedInterestRate, credit.getInterestRate());

        Assertions.assertEquals(initialSize + 1, daoCredit.findAll().size());

        Credit byId = daoCredit.findById(credit.getId()).orElseThrow();
        Assertions.assertEquals(credit.getId(), byId.getId());
        Assertions.assertEquals(expectedCreditLimit, byId.getCreditLimit());
        Assertions.assertEquals(expectedInterestRate, byId.getInterestRate());

        Integer updatedCreditLimit = random.nextInt(1000000);
        Integer updatedInterestRate = random.nextInt(100);
        daoCredit.save(new Credit(credit.getId(), updatedCreditLimit, updatedInterestRate, null));
        Assertions.assertEquals(initialSize + 1, daoCredit.findAll().size());
        Assertions.assertNotEquals(byId, daoCredit.findById(credit.getId()));

        byId = daoCredit.findById(credit.getId()).orElseThrow();
        Assertions.assertEquals(credit.getId(), byId.getId());
        Assertions.assertEquals(updatedCreditLimit, byId.getCreditLimit());
        Assertions.assertEquals(updatedInterestRate, byId.getInterestRate());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bankService.saveCredit((new Credit(null, -1, 10, null)));
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bankService.saveCredit((new Credit(null, 10, -1, null)));
        });

        daoCredit.deleteById(credit.getId());
        Assertions.assertEquals(initialSize, daoCredit.findAll().size());
    }

    @Test
    void checkSorting() {
        Random random = new Random();
        Integer expectedCreditLimit = random.nextInt(100000);
        Integer expectedInterestRate = random.nextInt(100);
        Credit credit = new Credit(null, expectedCreditLimit, expectedInterestRate - 1, null);
        Credit credit1 = new Credit(null, expectedCreditLimit + 1, expectedInterestRate, null);
        Credit credit2 = new Credit(null, expectedCreditLimit + 2, expectedInterestRate - 3, null);
        Credit credit3 = new Credit(null, expectedCreditLimit + 3, expectedInterestRate - 2, null);
        CreditSorting sorting = new CreditSorting("limit", "creditLimit", CreditSorting.Direction.ASC);
        CreditSorting sorting1 = new CreditSorting("limit", "creditLimit", CreditSorting.Direction.DESC);
        CreditSorting sorting2 = new CreditSorting("rate", "interestRate", CreditSorting.Direction.ASC);
        CreditSorting sorting3 = new CreditSorting("rate", "interestRate", CreditSorting.Direction.DESC);
        bankService.saveCredit(credit);
        bankService.saveCredit(credit1);
        bankService.saveCredit(credit2);
        bankService.saveCredit(credit3);
        try {
            Assertions.assertEquals(credit, bankService.findAllCredits(sorting).get(0));
            Assertions.assertEquals(credit3,bankService.findAllCredits(sorting1).get(0));
            Assertions.assertEquals(credit2,bankService.findAllCredits(sorting2).get(0));
            Assertions.assertEquals(credit1,bankService.findAllCredits(sorting3).get(0));
        }
        finally {
            bankService.deleteCreditById(credit.getId());
            bankService.deleteCreditById(credit1.getId());
            bankService.deleteCreditById(credit2.getId());
            bankService.deleteCreditById(credit3.getId());
        }
    }

}