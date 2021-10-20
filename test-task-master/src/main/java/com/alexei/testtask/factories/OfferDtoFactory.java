package com.alexei.testtask.factories;

import com.alexei.testtask.DTO.OfferDto;
import com.alexei.testtask.entity.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferDtoFactory {

    private final ClientDtoFactory clientDtoFactory;
    private final CreditDtoFactory creditDtoFactory;

    public OfferDtoFactory(ClientDtoFactory clientDtoFactory, CreditDtoFactory creditDtoFactory) {
        this.clientDtoFactory = clientDtoFactory;
        this.creditDtoFactory = creditDtoFactory;
    }

    public OfferDto makeOfferDto(Offer offer) {
        return new OfferDto(
                offer.getId(),
                clientDtoFactory.makeClientDto(offer.getClient()),
                creditDtoFactory.makeCreditDto(offer.getCredit()),
                offer.getCreditAmount(),
                offer.getCreditPayments()
        );
    }
}
