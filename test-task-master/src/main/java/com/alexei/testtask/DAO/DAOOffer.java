package com.alexei.testtask.DAO;

import com.alexei.testtask.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DAOOffer extends JpaRepository<Offer, UUID> {

}
