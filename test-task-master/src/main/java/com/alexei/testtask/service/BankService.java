package com.alexei.testtask.service;

import com.alexei.testtask.entity.Bank;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.entity.Offer;

import java.util.List;
import java.util.UUID;

public interface BankService {

    List<Client> findAllClients();

    List<Client> findAllClients(ClientFilter clientFilter, ClientSorting sorting);

    Client findClientById(UUID id);

    Client saveClient(Client client);

    void deleteClientById(UUID id);

    List<Client> clientWithoutBank();

    List<Credit> findAllCredits();

    List<Credit> findAllCredits(CreditSorting sorting);

    Credit findCreditById(UUID id);

    Credit saveCredit(Credit credit);

    void deleteCreditById(UUID id);

    List<Credit> creditWithoutBank();

    List<Bank> findAllBanks();

    Bank findBankById(UUID id);

    Bank findOnlyBank();

    Bank saveBank (Bank bank);

    void deleteBankById(UUID id);

    List<Offer> findAllOffer();

    Offer findOfferById(UUID id);

    Offer saveOffer (Offer offer);

    void deleteOfferById(UUID id);

    List<ClientSorting> getSortingClientsProperties();

    List<CreditSorting> getSortingCreditsProperties();





}
