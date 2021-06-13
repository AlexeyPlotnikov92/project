package com.haulmont.testtask.controller;

import com.haulmont.testtask.DAO.DAOBank;
import com.haulmont.testtask.DAO.DAOClient;
import com.haulmont.testtask.DAO.DAOCredit;
import com.haulmont.testtask.DAO.DAOOffer;
import com.haulmont.testtask.Entity.Bank;
import com.haulmont.testtask.Entity.Client;
import com.haulmont.testtask.Entity.Credit;
import com.haulmont.testtask.Entity.Offer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/banks")
public class BankController {
    private final DAOBank daoBank;
    private final DAOClient daoClient;
    private final DAOCredit daoCredit;
    private final DAOOffer daoOffer;

    public BankController(DAOBank daoBank, DAOClient daoClient, DAOCredit daoCredit, DAOOffer daoOffer) {
        this.daoBank = daoBank;
        this.daoClient = daoClient;
        this.daoCredit = daoCredit;
        this.daoOffer = daoOffer;
    }


    @GetMapping
    public ModelAndView getBanks() {
        ModelAndView modelAndView = new ModelAndView("banks");
        modelAndView.addObject("banks", daoBank.findAll());
        modelAndView.addObject("clients", daoClient.findAll());
        modelAndView.addObject("credits", daoCredit.findAll());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getBankById(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("bank");
        Bank bank = daoBank.findById(id);
        modelAndView.addObject("bank", bank);
        modelAndView.addObject("bankId", id);
        modelAndView.addObject("clients", daoClient.findAll());
        modelAndView.addObject("credits", daoCredit.findAll());
        modelAndView.addObject("bankClients", daoClient.findClients(id));
        modelAndView.addObject("bankCredits", daoCredit.findCredits(id));
        return modelAndView;
    }

    @PostMapping
    public ModelAndView createBank(@RequestParam String name,
                                   @RequestParam String clientId,
                                   @RequestParam String creditId) {
        List<Client> clients = new ArrayList<>();
        clients.add(daoClient.findById(clientId));
        List<Credit> credits = new ArrayList<>();
        credits.add(daoCredit.findById(creditId));
        Bank bank = new Bank(null, name, clients, credits);
        daoBank.save(bank);
        ModelAndView modelAndView = new ModelAndView("banks");
        modelAndView.addObject("banks", daoBank.findAll());
        return modelAndView;
    }

    @PostMapping("/{id}")
    public ModelAndView updateBank(@PathVariable String id,
                                   @RequestParam String name,
                                   @RequestParam String clientId,
                                   @RequestParam String creditId) {
        List<Client> clients = daoBank.findById(id).getClients();
        clients.add(daoClient.findById(clientId));
        List<Credit> credits = daoBank.findById(id).getCredits();
        credits.add(daoCredit.findById(creditId));
        Bank bank = new Bank(id, name, clients, credits);
        daoBank.save(bank);
        return new ModelAndView("redirect:/admin/banks/{id}");
    }

    @PostMapping("/{id}/remove")
    public ModelAndView delete(@PathVariable String id) {
        return new ModelAndView("redirect:/admin/banks");
    }

    @GetMapping("/{id}/offers")
    public ModelAndView getOffers(@PathVariable(value = "id") String id) {
        ModelAndView modelAndView = new ModelAndView("offers");
        Bank bank = daoBank.findAll().get(0);
        modelAndView.addObject("offers", daoOffer.findAll());
        modelAndView.addObject("bank", bank);
        modelAndView.addObject("clients", bank.getClients());
        modelAndView.addObject("credits", bank.getCredits());
        return modelAndView;
    }

    @GetMapping("/{id}/offers/{offerId}")
    public ModelAndView getOfferById(@PathVariable(value = "id") String bankId,
                                     @PathVariable(value = "offerId") String offerId) {
        Offer offer = daoOffer.findById(offerId);
        ModelAndView modelAndView = new ModelAndView("offer");
        Bank bank = daoBank.findAll().get(0);
        modelAndView.addObject("offer", offer);
        modelAndView.addObject("offerId", offerId);
//        modelAndView.addObject("clients", service.findAllClients());
//        modelAndView.addObject("credits", service.findAllCredits());
        modelAndView.addObject("clients", bank.getClients());
        modelAndView.addObject("credits", bank.getCredits());
        modelAndView.addObject("clientOffer", offer.getClient());
        modelAndView.addObject("creditOffer", offer.getCredit());
        return modelAndView;
    }

    @PostMapping("/{id}/offers")
    public ModelAndView createOffer(@RequestParam String clientId,
                                    @RequestParam String creditId,
                                    @RequestParam Integer creditAmount) {
        Offer offer = new Offer(null,
                daoClient.findById(clientId),
                daoCredit.findById(creditId),
                creditAmount);
        daoOffer.save(offer);
        ModelAndView modelAndView = new ModelAndView("offers");
        modelAndView.addObject("offers", daoOffer.findAll());
        return modelAndView;
    }

    @PostMapping("/{id}/offers/{offerId}")
    public ModelAndView updateOffer(@PathVariable String id,
                                    @RequestParam String clientId,
                                    @RequestParam String creditId,
                                    @RequestParam Integer creditAmount) {
        Offer offer = new Offer(id, daoClient.findById(clientId), daoCredit.findById(creditId), creditAmount);
        daoOffer.save(offer);
        ModelAndView modelAndView = new ModelAndView("offer");
        modelAndView.addObject("offer", daoOffer.findById(id));
        return modelAndView;
    }

    @PostMapping("/{id}/offers/{offerId}/remove")
    public ModelAndView deleteOffer(@PathVariable(value = "offerId") String id) {
        daoOffer.delete(id);
        return new ModelAndView("redirect:/admin/offers");
    }
}
