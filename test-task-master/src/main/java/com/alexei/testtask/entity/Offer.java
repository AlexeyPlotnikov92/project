package com.alexei.testtask.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offers")
public class Offer implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    private Credit credit;
    @Column(name = "credit_amount")
    private Integer creditAmount;
    @Transient
    private List<CreditPayment> creditPayments;


    public Offer(UUID id, Client client, Credit credit, Integer creditAmount) {
        this.id = id;
        this.client = client;
        this.credit = credit;
        this.creditAmount = creditAmount;
        this.creditPayments = loanCalculation(credit, creditAmount);
    }

    public String toStringOffer() {
        return "Кредит на  " + creditAmount + " рублей для " + client.getFoolName();
    }

    private List<CreditPayment> loanCalculation(Credit credit, Integer creditAmount) {
        List<CreditPayment> creditPayments = new ArrayList<>();
        int fullSum = creditAmount + creditAmount * credit.getInterestRate() / 100;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        for (int i = 0; i < 12; i++) {
            calendar.roll(Calendar.MONTH, +1);
            if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                calendar.roll(Calendar.YEAR, +1);
            }
            creditPayments.add(new CreditPayment(dateFormat.format(calendar.getTime()), fullSum * 1.0 / 12, creditAmount * 1.0 / 12, fullSum * 1.0 / 12 - creditAmount / 12));
        }
        return creditPayments;
    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class CreditPayment {
//        private String paymentDate;
//        private Double amountPayment;
//        private Double repaymentLoanBody;
//        private Double interestRepayment;
//    }

}
