package com.alexei.testtask.controller;

import com.alexei.testtask.entity.Bank;
import com.alexei.testtask.entity.Offer;
import com.alexei.testtask.service.BankService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/admin/offers")
public class OfferController {

    private final BankService bankService;


    public OfferController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public ModelAndView getOffers() {
        Bank bank;
        if (bankService.findAllBanks().size() < 1) {
            throw new IllegalArgumentException("bank not created");
        } else {
            bank = bankService.findOnlyBank();
        }
        ModelAndView modelAndView = new ModelAndView("offers");
        modelAndView.addObject("offers", bankService.findAllOffer());
        modelAndView.addObject("clients", bank.getClients());
        modelAndView.addObject("credits", bank.getCredits());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getOfferById(@PathVariable String id) {
        Offer offer = bankService.findOfferById(UUID.fromString(id));
        ModelAndView modelAndView = new ModelAndView("offer");
        Bank bank = bankService.findOnlyBank();
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
        if (StringUtils.isNotEmpty(clientId) && StringUtils.isNotEmpty(creditId)) {
            Offer offer = new Offer(null,
                    bankService.findClientById(UUID.fromString(clientId)),
                    bankService.findCreditById(UUID.fromString(creditId)),
                    creditAmount);
            bankService.saveOffer(offer);
        } else {
            throw new IllegalArgumentException("offer don't have client or credit");
        }
        return new ModelAndView("redirect:/admin/offers");
    }

    @PostMapping("/{id}")
    public ModelAndView updateOffer(@PathVariable String id,
                                    @RequestParam String clientId,
                                    @RequestParam String creditId,
                                    @RequestParam Integer creditAmount) {
        Offer offer = new Offer(UUID.fromString(id),
                bankService.findClientById(UUID.fromString(clientId)),
                bankService.findCreditById(UUID.fromString(creditId)),
                creditAmount);
        bankService.saveOffer(offer);
        return new ModelAndView("redirect:/admin/offers");
    }

    @PostMapping("/{id}/remove")
    public ModelAndView delete(@PathVariable String id) {
        bankService.deleteOfferById(UUID.fromString(id));
        return new ModelAndView("redirect:/admin/offers");
    }
}
