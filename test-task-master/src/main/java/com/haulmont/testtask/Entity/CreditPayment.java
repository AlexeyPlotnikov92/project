package com.haulmont.testtask.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditPayment {
    private String paymentDate;
    private Double amountPayment;
    private Double repaymentLoanBody;
    private Double interestRepayment;
}
