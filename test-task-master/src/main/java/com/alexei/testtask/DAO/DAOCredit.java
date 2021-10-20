package com.alexei.testtask.DAO;

import com.alexei.testtask.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DAOCredit extends JpaRepository<Credit, UUID> {

    @Query(value = "SELECT * FROM credits WHERE bank_id IS NULL",
            nativeQuery = true)
    List<Credit> creditsWithoutBank();

}
