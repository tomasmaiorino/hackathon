package com.skip.hackathon.service;


import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skip.exceptions.BadRequestException;
import com.skip.exceptions.ResourceNotFoundException;
import com.skip.hackathon.model.Customer;
import com.skip.hackathon.repository.CustomerRepository;
import com.skip.hackathon.repository.IBaseRepository;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
@Slf4j
public class CustomerService extends BaseService<Customer, Integer> implements UserDetailsService {

	private static final String DUPLICATED_TOKEN = null;
	@Autowired
	private CustomerRepository repository;

	public IBaseRepository<Customer, Integer> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return Customer.class.getSimpleName();
	}

	@Override
	protected void saveValidation(final Customer model) {
		repository.findByEmail(model.getEmail()).ifPresent(c -> {
			throw new BadRequestException(DUPLICATED_TOKEN);
		});

	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Assert.hasText(email, "The email must not be empty!");
		log.info("loadUserByUsername user by email [{}] .", email);

		Customer customer = repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

		return new org.springframework.security.core.userdetails.User(customer.getEmail(), customer.getPassword(),
				Collections.emptyList());
	}

	private Optional<Customer> findOptionalByEmail(final String email) {
		log.info("Finding user by email [{}] .", email);
		Optional<Customer> optCustomer = repository.findByEmail(email);
		log.info("Model found [{}].", optCustomer.isPresent());
		return optCustomer;
	}

	public Customer findByEmail(final String email) {
		Assert.hasText(email, "The email must not be empty!");
		Customer customer = findOptionalByEmail(email).orElseThrow(() -> new ResourceNotFoundException(""));
		return customer;
	}

}
