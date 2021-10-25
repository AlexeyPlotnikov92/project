package com.alexei.testtask.service.impl;

import com.alexei.testtask.DAO.DAOBank;
import com.alexei.testtask.DAO.DAOClient;
import com.alexei.testtask.DAO.DAOCredit;
import com.alexei.testtask.DAO.DAOOffer;
import com.alexei.testtask.entity.Bank;
import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.entity.Offer;
import com.alexei.testtask.exception.EntityNotFoundException;
import com.alexei.testtask.service.BankService;
import com.alexei.testtask.service.ClientFilter;
import com.alexei.testtask.service.ClientSorting;
import com.alexei.testtask.service.CreditSorting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Slf4j
public class BankServiceImpl implements BankService {

    private final DAOClient daoClient;
    private final DAOCredit daoCredit;
    private final DAOBank daoBank;
    private final DAOOffer daoOffer;

    public BankServiceImpl(DAOClient daoClient, DAOCredit daoCredit, DAOBank daoBank, DAOOffer daoOffer) {
        this.daoClient = daoClient;
        this.daoCredit = daoCredit;
        this.daoBank = daoBank;
        this.daoOffer = daoOffer;
    }

    @Override
    public List<Client> findAllClients() {
        return daoClient.findAll();
    }

    @Override
    public List<Client> findAllClients(ClientFilter clientFilter, ClientSorting sorting) {
        return daoClient.findAll(getSpecClient(clientFilter),
                Sort.by(Sort.Direction.valueOf(sorting.getDirection().name()), sorting.getColumn()));
    }

    @Override
    public Client findClientById(UUID id) {
        return daoClient.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Клиент с Id = " +
                "%d не найден", id)));
    }

    @Override
    public Client saveClient(Client client) {
        return daoClient.save(client);
    }

    @Override
    public void deleteClientById(UUID id) {
        daoClient.deleteById(id);
    }

    @Override
    public List<Client> clientWithoutBank() {
        return daoClient.clientsWithoutBank();
    }

    @Override
    public List<Credit> findAllCredits() {
        return daoCredit.findAll();
    }

    @Override
    public List<Credit> findAllCredits(CreditSorting sorting) {
        return daoCredit.findAll(Sort.by(
                Sort.Direction.valueOf(sorting.getDirection().name()), sorting.getColumn()));
    }

    @Override
    public Credit findCreditById(UUID id) {
        return daoCredit.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Кредит с Id = " +
                "%d не найден", id)));
    }

    @Override
    public Credit saveCredit(Credit credit) {
        if (credit.getCreditLimit() > 0 && credit.getInterestRate() >= 0) {
            return daoCredit.save(credit);
        } else {
            throw new IllegalArgumentException("only positive number");
        }
    }

    @Override
    public void deleteCreditById(UUID id) {
        daoCredit.deleteById(id);
    }

    @Override
    public List<Credit> creditWithoutBank() {
        return daoCredit.creditsWithoutBank();
    }

    @Override
    public List<Bank> findAllBanks() {
        return daoBank.findAll();
    }

    @Override
    public Bank findBankById(UUID id) {
        return daoBank.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Банк с Id = " +
                "%d не найден", id)));
    }

    @Override
    public Bank findOnlyBank() {
        if (findAllBanks().size() < 1) {
            throw new IllegalArgumentException("bank not created");
        } else {
            return findAllBanks().get(0);
        }
    }

    @Override
    public Bank saveBank(Bank bank) {
        if (bank.getId() == null) {
            if (findAllBanks().size() == 0) {
                return setClientsAndCredits(bank);
            } else {
                throw new IllegalArgumentException("extra bank");
            }
        } else {
            return setClientsAndCredits(bank);
        }
    }

    private Bank setClientsAndCredits(Bank bank) {
        for (Credit credit : bank.getCredits()) {
            credit.setBank(bank);
        }
        for (Client client : bank.getClients()) {
            client.setBank(bank);
        }
        return daoBank.save(bank);
    }


    @Override
    public void deleteBankById(UUID id) {
        daoBank.deleteById(id);
    }

    @Override
    public List<Offer> findAllOffer() {
        List<Offer> offers = new ArrayList<>();
        for (Offer offer : daoOffer.findAll()) {
            offers.add(new Offer(offer.getId(),
                    offer.getClient(),
                    offer.getCredit(),
                    offer.getCreditAmount()));
        }
        return offers;
    }

    @Override
    public Offer findOfferById(UUID id) {
        Offer o = daoOffer.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Кредитное предложение с Id = " +
                "%d не найдено", id)));
        Offer offer = new Offer(o.getId(), o.getClient(), o.getCredit(), o.getCreditAmount());
        return offer;

    }

    @Override
    public Offer saveOffer(Offer offer) {
        if (offer.getCreditAmount() <= offer.getCredit().getCreditLimit() && offer.getCreditAmount() > 0) {
            return daoOffer.save(offer);
        } else {
            throw new IllegalStateException("unexpected credit amount");
        }
    }

    @Override
    public void deleteOfferById(UUID id) {
        daoOffer.deleteById(id);
    }

    @Override
    public List<ClientSorting> getSortingClientsProperties() {
        return List.of(new ClientSorting(
                "имя", "foolName"
        ));
    }

    @Override
    public List<CreditSorting> getSortingCreditsProperties() {
        return List.of(new CreditSorting("сумма кредита", "creditLimit"),
                new CreditSorting("процентная ставка", "interestRate"));
    }

    private Specification<Client> getSpecClient(ClientFilter filter) {
        Specification<Client> specification = Specification.where(null);
        if (filter != null) {
            log.debug("Creating new Specification...");
            if (filter.getSearchName() != null) {
                log.debug("add possible foolName = {}", filter.getSearchName());
                specification =
                        specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get
                                ("foolName")), "%" + filter.getSearchName().toLowerCase(Locale.ROOT) + "%"));
            }
        }

        return specification;
    }
}
