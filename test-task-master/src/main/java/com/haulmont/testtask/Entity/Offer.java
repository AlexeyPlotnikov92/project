package com.haulmont.testtask.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    private String id;
    private Client client;
    private Credit credit;
    private Integer creditAmount;
    private List<CreditPayment> creditPayments;
    private String name;
    private String date;

    public Offer(String id, Client client, Credit credit, Integer creditAmount, String date) throws ParseException {
        this.id = id;
        this.client = client;
        this.credit = credit;
        this.creditAmount = creditAmount;
        this.date = date;
        this.creditPayments = loanCalculation(credit, creditAmount, date);
        name = "Кредит на  " + creditAmount + " рублей для " + client.getFoolName();
    }

    private List<CreditPayment> loanCalculation(Credit credit, Integer creditAmount, String date) throws ParseException {
        List<CreditPayment> creditPayments = new ArrayList<>();
        int fullSum = creditAmount + creditAmount * credit.getInterestRate() / 100;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date tempDate = dateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tempDate);
        for (int i = 0; i < 12; i++) {
            calendar.roll(Calendar.MONTH, +1);
            if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                calendar.roll(Calendar.YEAR, +1);
            }
            creditPayments.add(new CreditPayment(dateFormat.format(calendar.getTime()), fullSum * 1.0 / 12, creditAmount * 1.0 / 12, fullSum * 1.0 / 12 - creditAmount / 12));
        }
        return creditPayments;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreditPayment {
        private String paymentDate;
        private Double amountPayment;
        private Double repaymentLoanBody;
        private Double interestRepayment;
    }

}
