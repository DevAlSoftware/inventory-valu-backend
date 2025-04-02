package com.company.inventory.customers.services;

import com.company.inventory.customers.model.Customer;
import com.company.inventory.customers.response.CustomerResponseRest;
import org.springframework.http.ResponseEntity;

public interface ICustomerService {

    public ResponseEntity<CustomerResponseRest> search();
    public ResponseEntity<CustomerResponseRest> searchById(Long id);
    public ResponseEntity<CustomerResponseRest> save(Customer customer);
    public ResponseEntity<CustomerResponseRest> update(Customer customer, Long id);
    public ResponseEntity<CustomerResponseRest> deleteById(Long id);
}
