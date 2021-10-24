package com.alexei.testtask.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditPayment {
    private String paymentDate;
    private Double amountPayment;
    private Double repaymentLoanBody;
    private Double interestRepayment;
}
