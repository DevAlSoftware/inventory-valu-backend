package com.company.inventory.customers.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseRest extends ResponseRest{

    private CustomerResponse customerResponse = new CustomerResponse();
}
