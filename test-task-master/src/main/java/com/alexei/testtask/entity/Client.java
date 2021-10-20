package com.alexei.testtask.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;
    @Column(name = "fool_name")
    private String foolName;
    @Column(name = "telephone_number")
    private String telephoneNumber;
    @Column(name = "email")
    private String eMailAdress;
    @Column(name = "passport", unique = true)
    private Integer passportNumber;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "bank_id")
    private Bank bank;

}
