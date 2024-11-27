package com.enoca.ecommerce.services;

import com.enoca.ecommerce.dtos.CustomerRequestDTO;
import com.enoca.ecommerce.dtos.CustomerResponseDTO;
import com.enoca.ecommerce.entities.Customer;
import com.enoca.ecommerce.mappers.CustomerMapper;
import com.enoca.ecommerce.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    // Create a new customer
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        if(customerRepository.existsByEmail(customerRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        Customer customer = customerMapper.toEntity(customerRequestDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }

    // Retrieve all customers
    public List<CustomerResponseDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Retrieve a customer by ID
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
        return customerMapper.toResponseDTO(customer);
    }

    // Update a customer
    @Transactional
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));

        if(!customer.getEmail().equals(customerRequestDTO.getEmail()) &&
                customerRepository.existsByEmail(customerRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        customerMapper.updateEntity(customer, customerRequestDTO);
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(updatedCustomer);
    }

    // Delete a customer
    public void deleteCustomer(Long id) {
        if(!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }
}
