package com.company.inventory.customers.dao;

import com.company.inventory.customers.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerDao extends CrudRepository<Customer, Long> {
}
