package com.haulmont.testtask.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credits")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "credit_limit")
    private Integer creditLimit;
    @Column(name = "interest_rate")
    private Integer InterestRate;
    @Column(name = "bank_id")
    private String bankId;
    private String name;

    public Credit(String id, Integer creditLimit, Integer interestRate, String bankId) {
        this.id = id;
        this.creditLimit = creditLimit;
        this.InterestRate = interestRate;
        this.bankId = bankId;
        name = creditLimit + " рублей, " + interestRate + "%ставка";
    }
}
