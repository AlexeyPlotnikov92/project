package com.alexei.testtask.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditPaymentDto {
    private String paymentDate;
    private Double amountPayment;
    private Double repaymentLoanBody;
    private Double interestRepayment;
}
