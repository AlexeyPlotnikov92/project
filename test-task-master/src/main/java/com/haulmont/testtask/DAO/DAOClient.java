package com.haulmont.testtask.DAO;

import com.haulmont.testtask.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DAOClient extends JpaRepository<Client, String> {

    List<Client> findAll();

    Optional<Client> findById(String id);

    Client save(Client client);

    void delete(String id);

//    List<Client> findClientsOfBank(String id);
//
//    List<Client> findClientWithoutBank(String id);
}
