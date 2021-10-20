package com.alexei.testtask.DAO;

import com.alexei.testtask.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DAOClient extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {

    @Query(value = "SELECT * FROM clients WHERE bank_id IS NULL",
            nativeQuery = true)
    List<Client> clientsWithoutBank();

}
