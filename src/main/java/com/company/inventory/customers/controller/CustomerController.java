package com.company.inventory.customers.controller;

import com.company.inventory.customers.model.Customer;
import com.company.inventory.customers.response.CustomerResponseRest;
import com.company.inventory.customers.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200", "http://69.62.67.49:4200"})
@RestController
@RequestMapping("api/v1")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    /**
     * get all the customers
     * @return
     */
    @GetMapping("/customers")
    public ResponseEntity<CustomerResponseRest> searchCustomers () { return customerService.search();}

    /**
     * get customers by id
     * @param id
     * @return
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseRest> searchCustomersById (@PathVariable Long id) { return customerService.searchById(id); }

    /**
     * search by name
     * @param fullName
     * @return
     */
    @GetMapping("/customers/filter/{fullName}")
    public ResponseEntity<CustomerResponseRest> searchByName(@PathVariable String fullName){
        ResponseEntity<CustomerResponseRest> response = customerService.searchByFullName(fullName);
        return response;
    }

    /**
     * SAVE customers
     * @param customer
     * @return
     */
    @PostMapping("/customers")
    public ResponseEntity<CustomerResponseRest> save (@RequestBody Customer customer) { return customerService.save(customer); }

    /**
     * update customers
     * @param customer
     * @param id
     * @return
     */
    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseRest> update(@RequestBody Customer customer, @PathVariable Long id) { return customerService.update(customer, id); }

    /**
     * delete customers
     * @param id
     * @return
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseRest> delete(@PathVariable Long id) { return customerService.deleteById(id); }
}
