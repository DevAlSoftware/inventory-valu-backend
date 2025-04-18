package com.company.inventory.customers.response;

import com.company.inventory.ResponseRest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseRest extends ResponseRest {

    private CustomerResponse customerResponse = new CustomerResponse();

    public CustomerResponse getCustomerResponse() {
        return customerResponse;
    }

    public void setCustomerResponse(CustomerResponse customerResponse) {
        this.customerResponse = customerResponse;
    }
}
