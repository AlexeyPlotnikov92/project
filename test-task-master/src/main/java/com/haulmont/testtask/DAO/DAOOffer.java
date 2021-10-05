package com.haulmont.testtask.DAO;

import com.haulmont.testtask.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DAOOffer extends JpaRepository<Offer, String> {
    List<Offer> findAll();

    Optional<Offer> findById(String id);

    Offer save(Offer offer);

    void delete(String id);
}
