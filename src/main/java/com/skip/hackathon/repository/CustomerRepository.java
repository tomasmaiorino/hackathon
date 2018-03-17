package com.skip.hackathon.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skip.hackathon.model.Customer;

@Transactional(propagation = Propagation.MANDATORY)
public interface CustomerRepository extends Repository<Customer, Integer>, IBaseRepository<Customer, Integer> {

}
