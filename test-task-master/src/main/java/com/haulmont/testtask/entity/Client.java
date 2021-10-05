package com.haulmont.testtask.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "fool_name")
    private String foolName;
    @Column(name = "telephone_number")
    private String telephoneNumber;
    @Column(name = "email")
    private String eMailAdress;
    @Column(name = "passport", unique = true)
    private Integer passportNumber;
    @Column(name = "bank_id")
    private String bankId;
}
