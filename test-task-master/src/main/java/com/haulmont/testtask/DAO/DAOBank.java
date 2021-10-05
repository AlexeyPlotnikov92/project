package com.haulmont.testtask.DAO;

import com.haulmont.testtask.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DAOBank extends JpaRepository<Bank, String> {

    List<Bank> findAll();

    Optional<Bank> findById(String id);

    Bank save(Bank bank);

    void delete(String id);
}
