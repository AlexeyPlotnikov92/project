package com.haulmont.testtask.DAO;

import com.haulmont.testtask.entity.Offer;

import java.util.List;

public interface DAOOffer {
    List<Offer> findAll();

    Offer findById(String id);

    Offer save(Offer offer);

    void delete(String id);
}
