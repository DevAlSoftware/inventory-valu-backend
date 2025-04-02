package com.company.inventory.customers.response;

import com.company.inventory.categories.model.Category;
import com.company.inventory.customers.model.Customer;
import lombok.Data;

import java.util.List;

@Data
public class CustomerResponse {

    private List<Customer> customer;
}
