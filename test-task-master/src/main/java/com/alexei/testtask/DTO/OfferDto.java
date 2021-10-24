package com.alexei.testtask.DTO;

import com.alexei.testtask.entity.CreditPayment;
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

    private List<CreditPayment> creditPayments;
}
