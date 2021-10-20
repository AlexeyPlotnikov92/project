package com.alexei.testtask.DTO;

import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.entity.Offer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {

    private UUID id;

    private ClientDto client;

    private CreditDto credit;

    private Integer creditAmount;

    private List<Offer.CreditPayment> creditPayments;
}
