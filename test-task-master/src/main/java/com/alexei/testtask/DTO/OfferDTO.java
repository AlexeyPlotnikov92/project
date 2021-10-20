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
public class OfferDTO {

    private UUID id;

    private Client client;

    private Credit credit;

    private Integer creditAmount;

    private List<Offer.CreditPayment> creditPayments;
}
