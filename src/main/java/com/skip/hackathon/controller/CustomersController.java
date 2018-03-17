package com.skip.hackathon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skip.hackathon.model.Customer;
import com.skip.hackathon.resource.CustomerResource;
import com.skip.hackathon.service.BaseService;
import com.skip.hackathon.service.CustomerService;
import com.skip.parser.CustomerParser;
import com.skip.parser.IParser;

@RestController
@RequestMapping(value = "/api/v1/customers")
public class CustomersController extends BaseController<CustomerResource, Customer, Integer> {

	@Autowired
	private CustomerService service;

	@Autowired
	private CustomerParser parser;

	@Override
	public BaseService<Customer, Integer> getService() {
		return service;
	}

	@Override
	public IParser<CustomerResource, Customer> getParser() {
		return parser;
	}

}
