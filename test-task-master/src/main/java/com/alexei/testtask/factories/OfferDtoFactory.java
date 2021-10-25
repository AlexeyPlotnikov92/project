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

//    private List<CreditPaymentDto> makeCreditPaymentList(List<CreditPayment> offerCreditPays) {
//        List<CreditPaymentDto> creditPayments = new ArrayList<>();
//        for (CreditPayment creditPayment : offerCreditPays) {
//            makeCreditPayment(creditPayment);
//        }
//        return creditPayments;
//    }
//
//    private CreditPaymentDto makeCreditPayment(CreditPayment creditPayment) {
//        return new CreditPaymentDto(creditPayment.getPaymentDate(),
//                creditPayment.getAmountPayment(),
//                creditPayment.getRepaymentLoanBody(),
//                creditPayment.getInterestRepayment());
//    }
}
