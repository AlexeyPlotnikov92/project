package com.alexei.testtask.controller;

import com.alexei.testtask.DTO.AskDto;
import com.alexei.testtask.DTO.CreditDTO;
import com.alexei.testtask.DTO.OfferDTO;
import com.alexei.testtask.entity.Bank;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.entity.Offer;
import com.alexei.testtask.factories.OfferDtoFactory;
import com.alexei.testtask.service.BankService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@RestController
public class OfferRestController {

    public static final String FETCH_OFFERS = "/api/v1/offers";
    public static final String CREATE_OFFERS = "/api/v1/offers";
    public static final String EDIT_OFFERS = "/api/v1/offers/{id}";
    public static final String DELETE_OFFERS = "/api/v1/offers/{id}";
    private final OfferDtoFactory offerDtoFactory;
    private final BankService bankService;

    public OfferRestController(OfferDtoFactory offerDtoFactory, BankService bankService) {
        this.offerDtoFactory = offerDtoFactory;
        this.bankService = bankService;
    }

    @GetMapping(FETCH_OFFERS)
    public List<OfferDTO> getOffers() {
        Bank bank;
        if (bankService.findAllBanks().size() < 1) {
            throw new IllegalArgumentException("bank not created");
        } else {
            bank = bankService.findOnlyBank();
        }
        List<Offer> offersEntity = bankService.findAllOffer();
        List<OfferDTO> offers = new ArrayList<>();
        for (Offer offer : offersEntity) {
            offers.add(offerDtoFactory.makeOfferDto(offer));
        }
        return offers;
    }

    @PostMapping(CREATE_OFFERS)
    public OfferDTO createOffer(@RequestParam String clientId,
                                 @RequestParam String creditId,
                                 @RequestParam Integer creditAmount) {
        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(creditId)) {
           throw new IllegalArgumentException("offer have not client or credit");
        }
        Offer offer = new Offer(null,
                bankService.findClientById(UUID.fromString(clientId)),
                bankService.findCreditById(UUID.fromString(creditId)),
                creditAmount);
        bankService.saveOffer(offer);
        return offerDtoFactory.makeOfferDto(offer);
    }

    @PatchMapping(EDIT_OFFERS)
    public OfferDTO updateOffer(@PathVariable String id,
                                  @RequestParam String clientId,
                                  @RequestParam String creditId,
                                  @RequestParam Integer creditAmount) {
        Offer offer = new Offer(UUID.fromString(id),
                bankService.findClientById(UUID.fromString(clientId)),
                bankService.findCreditById(UUID.fromString(creditId)),
                creditAmount);
        bankService.saveOffer(offer);
        return offerDtoFactory.makeOfferDto(offer);
    }

    @DeleteMapping(DELETE_OFFERS)
    public AskDto deleteOffer(@PathVariable String id) {
        bankService.deleteOfferById(UUID.fromString(id));
        return AskDto.makeAnswer(true);
    }
}
