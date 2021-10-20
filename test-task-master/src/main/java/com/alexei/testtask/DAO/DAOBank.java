package com.alexei.testtask.DAO;

import com.alexei.testtask.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DAOBank extends JpaRepository<Bank, UUID> {

//    List<Bank> findAll();
//
//    Optional<Bank> findById(UUID id);
//
//    Bank save(Bank bank);
//
//    void deleteB(String id);
}
