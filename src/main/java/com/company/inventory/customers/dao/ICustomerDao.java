package com.company.inventory.customers.dao;

import com.company.inventory.customers.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICustomerDao extends CrudRepository<Customer, Long> {

    @Query("select c from Customer c where c.fullName like %?1%")
    List<Customer> findByFullNameLike(String fullName);

    List<Customer> findByFullNameContainingIgnoreCase(String fullName);
}
