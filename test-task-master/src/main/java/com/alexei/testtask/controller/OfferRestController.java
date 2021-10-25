package com.alexei.testtask.controller;

import com.alexei.testtask.DTO.AсkDto;
import com.alexei.testtask.DTO.OfferDto;
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

    public static final String GET_OFFERS = "/api/v1/offers";
    public static final String GET_OFFER = "/api/v1/offers/{id}";
    public static final String CREATE_OFFERS = "/api/v1/offers";
    public static final String EDIT_OFFERS = "/api/v1/offers/{id}";
    public static final String DELETE_OFFERS = "/api/v1/offers/{id}";
    private final OfferDtoFactory offerDtoFactory;
    private final BankService bankService;

    public OfferRestController(OfferDtoFactory offerDtoFactory, BankService bankService) {
        this.offerDtoFactory = offerDtoFactory;
        this.bankService = bankService;
    }

    @GetMapping(GET_OFFERS)
    public List<OfferDto> getOffers() {
        List<Offer> offersEntity = bankService.findAllOffer();
        List<OfferDto> offers = new ArrayList<>();
        for (Offer offer : offersEntity) {
            offers.add(offerDtoFactory.makeOfferDto(offer));
        }
        return offers;
    }

    @GetMapping(GET_OFFER)
    public OfferDto getOffer(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("offer with this Id %s was not found", id));
        }
        Offer offer = bankService.findOfferById(UUID.fromString(id));
        return offerDtoFactory.makeOfferDto(offer);
    }

    @PostMapping(CREATE_OFFERS)
    public OfferDto createOffer(@RequestParam String clientId,
                                @RequestParam String creditId,
                                @RequestParam Integer creditAmount) {
        if (!clientId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("client with this Id %s was not found", clientId));
        }
        if (!creditId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("credit with this Id %s was not found", creditId));
        }
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
    public OfferDto updateOffer(@PathVariable String id,
                                @RequestParam Integer creditAmount) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("offer with this Id %s was not found", id));
        }
        Offer offer = new Offer(UUID.fromString(id),
                bankService.findOfferById(UUID.fromString(id)).getClient(),
                bankService.findOfferById(UUID.fromString(id)).getCredit(),
                creditAmount);
        bankService.saveOffer(offer);
        return offerDtoFactory.makeOfferDto(offer);
    }

    @DeleteMapping(DELETE_OFFERS)
    public AсkDto deleteOffer(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("offer with this Id %s was not found", id));
        }
        bankService.deleteOfferById(UUID.fromString(id));
        return AсkDto.makeAnswer(true);
    }
}
