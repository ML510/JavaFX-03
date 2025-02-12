package controller.customer;

import model.Customer;

import java.util.List;

public interface CustomerService {

    boolean addCustomer (Customer customer);
    boolean updateCustomer (Customer customer);
    boolean deleteCustomer (Customer customer);
    Customer searchCustomer (String id);
    List<Customer> getAll();




}
