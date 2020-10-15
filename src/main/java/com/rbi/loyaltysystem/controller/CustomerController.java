package com.rbi.loyaltysystem.controller;

import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> registerNewCustomer(@RequestBody final Customer customer) {
        return ResponseEntity.ok(customerService.addCustomer(customer));
    }

    @GetMapping
    public Customer getCustomer(@RequestParam Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping(path = "/details")
    public Customer getCustomerDetails(@RequestParam Long id) {
        return customerService.getDetails(id);
    }

}
