package com.haulmont.testtask.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String id;
    private String foolName;
    private String telephoneNumber;
    private String eMailAdress;
    private Integer passportNumber;
    private String bankId;
}
