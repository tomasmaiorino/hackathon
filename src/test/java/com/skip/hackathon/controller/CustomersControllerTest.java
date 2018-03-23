package com.skip.hackathon.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.skip.exceptions.BadRequestException;
import com.skip.hackathon.model.Customer;
import com.skip.hackathon.resource.AuthTokenResource;
import com.skip.hackathon.resource.CustomerResource;
import com.skip.hackathon.resource.LoginResource;
import com.skip.hackathon.service.CustomerService;
import com.skip.hackathon.service.JwtTokenUtil;
import com.skip.hackathon.util.CustomerTestBuilder;
import com.skip.hackathon.util.LoginTestBuilder;
import com.skip.parser.CustomerParser;

import io.jsonwebtoken.lang.Assert;

@FixMethodOrder(MethodSorters.JVM)
public class CustomersControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private CustomerService service;

	@Mock
	private CustomerParser parser;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	@InjectMocks
	private CustomersController controller;

	@Mock
	private Validator validator;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	public void save_InvalidCustomerResourceGiven_ShouldThrowException() {
		// Set up
		CustomerResource resource = CustomerTestBuilder.buildResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verifyZeroInteractions(service, parser);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void save_DuplicatedCustomerResourceGiven_ShouldSaveCustomer() {
		// Set up
		CustomerResource resource = CustomerTestBuilder.buildResource();
		Customer customer = CustomerTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(parser.toModel(resource)).thenReturn(customer);
		when(service.save(customer)).thenThrow(BadRequestException.class);

		// Do test
		try {
			controller.save(resource);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(service).save(customer);
		verify(parser).toModel(resource);

	}

	@Test
	public void save_ValidCustomerResourceGiven_ShouldSaveCustomer() {
		// Set up
		CustomerResource resource = CustomerTestBuilder.buildResource();
		Customer customer = CustomerTestBuilder.buildModel();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(parser.toModel(resource)).thenReturn(customer);
		when(service.save(customer)).thenReturn(customer);
		when(parser.toResource(customer)).thenReturn(resource);

		// Do test
		CustomerResource result = controller.save(resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(service).save(customer);
		verify(parser).toModel(resource);
		verify(parser).toResource(customer);

		assertNotNull(result);
		assertThat(result, is(resource));
	}

	@Test
	public void auth_InvalidLoginResourceGiven_ShouldThrowException() {
		// Set up
		LoginResource resource = LoginTestBuilder.buildLoginResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenThrow(new ValidationException());

		// Do test
		try {
			controller.auth(resource);
			fail();
		} catch (ValidationException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verifyZeroInteractions(authenticationManager, service, jwtTokenUtil);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auth_InvalidLoginAndPasswordGiven_ShouldThrowAuthenticationException() {
		// Set up
		LoginResource resource = LoginTestBuilder.buildLoginResource();

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(UsernameNotFoundException.class);

		// Do test
		try {
			controller.auth(resource);
			fail();
		} catch (UsernameNotFoundException e) {
		}

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verifyZeroInteractions(service, jwtTokenUtil);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void auth_validLoginAndPasswordGiven_ShouldReturnToken() {
		// Set up
		LoginResource resource = LoginTestBuilder.buildLoginResource();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null);
		Customer customer = CustomerTestBuilder.buildModel();
		String token = "43242342";

		// Expectations
		when(validator.validate(resource, Default.class)).thenReturn(Collections.emptySet());
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(usernamePasswordAuthenticationToken);
		when(service.findByEmail(resource.getEmail())).thenReturn(customer);
		when(jwtTokenUtil.doGenerateToken(customer.getEmail())).thenReturn(token);

		// Do test
		AuthTokenResource result = controller.auth(resource);

		// Assertions
		verify(validator).validate(resource, Default.class);
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(service).findByEmail(resource.getEmail());
		verify(jwtTokenUtil).doGenerateToken(customer.getEmail());

		Assert.notNull(result);
		assertThat(result, allOf(hasProperty("token", is(token))));
	}

}
