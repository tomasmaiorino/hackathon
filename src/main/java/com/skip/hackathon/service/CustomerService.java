package com.skip.hackathon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skip.hackathon.model.Customer;
import com.skip.hackathon.repository.CustomerRepository;
import com.skip.hackathon.repository.IBaseRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class CustomerService extends BaseService<Customer, Integer> {

	@Autowired
	private CustomerRepository repository;

	public IBaseRepository<Customer, Integer> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return Customer.class.getSimpleName();
	}

}
