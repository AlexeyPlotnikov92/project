package com.alexei.testtask.DTO;

import com.alexei.testtask.entity.Client;
import com.alexei.testtask.entity.Credit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDTO {

    private UUID id;

    private String name;

    private List<Client> clients;

    private List<Credit> credits;
}
