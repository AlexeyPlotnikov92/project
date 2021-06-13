package com.haulmont.testtask.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

    public Offer(String id, Client client, Credit credit, Integer creditAmount) {
        this.id = id;
        this.client = client;
        this.credit = credit;
        this.creditAmount = creditAmount;
        this.creditPayments = loanCalculation(credit, creditAmount);
        name = "Кредит на  " + creditAmount + " рублей для " + client.getFoolName();
    }

    private List<CreditPayment> loanCalculation(Credit credit, Integer creditAmount) {
        List<CreditPayment> creditPayments = new ArrayList<>();
        int fullSum = creditAmount + creditAmount * credit.getInterestRate() / 100;
        Calendar calendar = new GregorianCalendar();
        for (int i = 0; i < 12; i++) {
            calendar.roll(Calendar.MONTH, +1);
            if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                calendar.roll(Calendar.YEAR, +1);
            }
            creditPayments.add(new CreditPayment(calendar.getTime(), fullSum * 1.0 / 12, creditAmount * 1.0 / 12, fullSum * 1.0 / 12 - creditAmount / 12));
        }
        return creditPayments;
    }

}
