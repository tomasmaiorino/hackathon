package com.skip.hackathon.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.skip.hackathon.model.Customer;
import com.skip.hackathon.resource.AuthTokenResource;
import com.skip.hackathon.resource.CustomerResource;
import com.skip.hackathon.resource.LoginResource;
import com.skip.hackathon.service.BaseService;
import com.skip.hackathon.service.CustomerService;
import com.skip.hackathon.service.JwtTokenUtil;
import com.skip.parser.CustomerParser;
import com.skip.parser.IParser;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/customers")
public class CustomersController extends BaseController<CustomerResource, Customer, Integer> {

	@Autowired
	private CustomerService service;

	@Autowired
	private CustomerParser parser;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public BaseService<Customer, Integer> getService() {
		return service;
	}

	@Override
	public IParser<CustomerResource, Customer> getParser() {
		return parser;
	}

	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE, value = "/auth")
	@ResponseStatus(OK)
	public AuthTokenResource auth(@RequestBody final LoginResource resource) {

		log.info("Recieved a request to auth a resource [{}].", resource);

		validate(resource, Default.class);

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(resource.getEmail(), resource.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final Customer customer = service.findByEmail(resource.getEmail());
		final String token = jwtTokenUtil.doGenerateToken(customer.getEmail());
		return new AuthTokenResource(token);
	}

}
