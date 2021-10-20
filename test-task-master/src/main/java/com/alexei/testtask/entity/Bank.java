package com.alexei.testtask.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banks")
public class Bank implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(name = "name")
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank")
    private List<Client> clients;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank")
    private List<Credit> credits;

    public String toStringBank() {
        return "id=" + id +
                ", name='" + name;
    }
}
