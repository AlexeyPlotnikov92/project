package com.haulmont.testtask.DAO;

import com.haulmont.testtask.Entity.Offer;

import java.text.ParseException;
import java.util.List;

public interface DAOOffer {
    List<Offer> findAll();

    Offer findById(String id);

    Offer save(Offer offer) throws ParseException;

    void delete(String id);
}
