package fucarrentingsystem.repository;

import fucarrentingsystem.entity.Customer;

public class CustomerRepository extends GenericRepository<Customer> {

    public CustomerRepository() {
        super(Customer.class);
    }
}
