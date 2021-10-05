package com.haulmont.testtask.DAO;

import com.haulmont.testtask.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DAOCredit extends JpaRepository<Credit, String> {
    List<Credit> findAll();

    Optional<Credit> findById(String id);

    Credit save(Credit credit);

    void delete(String id);

//    List<Credit> findCreditsOfBank(String id);
//
//    List<Credit> findCreditsWithoutBank(String id);
}
