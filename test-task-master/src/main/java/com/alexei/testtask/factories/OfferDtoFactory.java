package com.alexei.testtask.factories;

import com.alexei.testtask.DTO.OfferDTO;
import com.alexei.testtask.entity.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferDtoFactory {

    public OfferDTO makeOfferDto(Offer offer) {
        return new OfferDTO(
                offer.getId(),
                offer.getClient(),
                offer.getCredit(),
                offer.getCreditAmount(),
                offer.getCreditPayments()
        );
    }
}
