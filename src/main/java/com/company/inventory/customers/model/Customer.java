package com.company.inventory.customers.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name="customer")
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = -7372632643010046781L;
    /**
     *
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    private String document;

    private String phone;
}
