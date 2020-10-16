package com.rbi.loyaltysystem.controller;

import com.rbi.loyaltysystem.dto.InvestmentDto;
import com.rbi.loyaltysystem.model.Customer;
import com.rbi.loyaltysystem.model.Point;
import com.rbi.loyaltysystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(path = "/invest")
    public ResponseEntity<InvestmentDto> investPoints(@RequestBody final InvestmentDto investment) {
        return ResponseEntity.ok(customerService.invest(investment));
    }

    @GetMapping(path = "/invest")
    public ResponseEntity<List<InvestmentDto>> getInvestments(@RequestParam final long id) {
        return ResponseEntity.ok(customerService.findAllInvestments(id));
    }

    @PostMapping(path = "/income")
    public ResponseEntity<Customer> addIncome(@RequestParam final long id, final double income){
        return ResponseEntity.ok(customerService.addIncome(id, income));
    }

    @GetMapping(path = "/available/points")
    public ResponseEntity<List<Point>> getAvailablePoints(@RequestParam final long id) {
        return ResponseEntity.ok(customerService.getAllAvailablePoints(id));
    }

    @GetMapping(path = "/pending/points")
    public ResponseEntity<List<Point>> getPendingPoints(@RequestParam final long id) {
        return ResponseEntity.ok(customerService.getAllPendingPoints(id));
    }
}
