package com.haulmont.testtask.controller;

import com.haulmont.testtask.DAO.DAOBank;
import com.haulmont.testtask.DAO.DAOClient;
import com.haulmont.testtask.DAO.DAOCredit;
import com.haulmont.testtask.DAO.DAOOffer;
import com.haulmont.testtask.entity.Bank;
import com.haulmont.testtask.entity.Offer;
import com.haulmont.testtask.exception.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/offers")
public class OfferController {
    private final DAOOffer daoOffer;
    private final DAOBank daoBank;
    private final DAOCredit daoCredit;
    private final DAOClient daoClient;


    public OfferController(DAOOffer daoOffer, DAOBank daoBank, DAOCredit daoCredit, DAOClient daoClient) {
        this.daoOffer = daoOffer;
        this.daoBank = daoBank;
        this.daoCredit = daoCredit;
        this.daoClient = daoClient;
    }

    @GetMapping
    public ModelAndView getOffers() {
        Bank bank;
        if (daoBank.findAll().size()<1) {
            throw new IllegalArgumentException("bank not created");
        }
        else {
            bank = daoBank.findAll().get(0);
        }
        ModelAndView modelAndView = new ModelAndView("offers");
        modelAndView.addObject("offers", daoOffer.findAll());
        modelAndView.addObject("clients", bank.getClients());
        modelAndView.addObject("credits", bank.getCredits());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getOfferById(@PathVariable String id) {
        Offer offer = daoOffer.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Жанр с Id = " +
                "%d не найден", id)));
        ModelAndView modelAndView = new ModelAndView("offer");
        Bank bank = daoBank.findAll().get(0);
        modelAndView.addObject("offer", offer);
        modelAndView.addObject("offerId", id);
        modelAndView.addObject("clients", bank.getClients());
        modelAndView.addObject("credits", bank.getCredits());
        modelAndView.addObject("clientOffer", offer.getClient().getId());
        modelAndView.addObject("creditOffer", offer.getCredit().getId());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView createOffer(@RequestParam String clientId,
                                    @RequestParam String creditId,
                                    @RequestParam Integer creditAmount) {
        Offer offer = new Offer(null,
                daoClient.findById(clientId).orElseThrow(()-> new EntityNotFoundException(String.format("Жанр с Id = " +
                        "%d не найден", clientId))),
                daoCredit.findById(creditId).orElseThrow(()-> new EntityNotFoundException(String.format("Жанр с Id = " +
                        "%d не найден", creditId))),
                creditAmount);
        daoOffer.save(offer);
        return new ModelAndView("redirect:/admin/offers");
    }

    @PostMapping("/{id}")
    public ModelAndView updateOffer(@PathVariable String id,
                                    @RequestParam String clientId,
                                    @RequestParam String creditId,
                                    @RequestParam Integer creditAmount) {
        Offer offer = new Offer(id, daoClient.findById(clientId).orElseThrow(()-> new EntityNotFoundException(String.format("Жанр с Id = " +
                "%d не найден", clientId))), daoCredit.findById(creditId).orElseThrow(()-> new EntityNotFoundException(String.format("Жанр с Id = " +
                "%d не найден", creditId))), creditAmount);
        daoOffer.save(offer);
        return new ModelAndView("redirect:/admin/offers");
    }

    @PostMapping("/{id}/remove")
    public ModelAndView delete(@PathVariable String id) {
        daoOffer.deleteById(id);
        return new ModelAndView("redirect:/admin/offers");
    }
}
