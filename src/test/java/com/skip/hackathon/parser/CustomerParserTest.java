package com.skip.hackathon.parser;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.skip.hackathon.model.Customer;
import com.skip.hackathon.model.Customer.CustomerStatus;
import com.skip.hackathon.resource.CustomerResource;
import com.skip.hackathon.util.CustomerTestBuilder;
import com.skip.parser.CustomerParser;

public class CustomerParserTest {


	@InjectMocks
	private CustomerParser parser;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullResourceGiven_ShouldThrowException() {
		// Set up
		CustomerResource resource = null;

		// Do test
		parser.toModel(resource);
	}

	@Test
	public void toModel_ValidResourceGiven_ShouldCreateCustomerModel() {
		// Set up
		CustomerResource resource = CustomerTestBuilder.buildResource();
		String newPass = CustomerTestBuilder.getPassword();
		
		//Expectations
		when(bCryptPasswordEncoder.encode(resource.getPassword())).thenReturn(newPass);

		// Do test
		Customer result = parser.toModel(resource);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", nullValue()), hasProperty("name", is(resource.getName())),
				hasProperty("email", is(resource.getEmail())), hasProperty("password", is(newPass)),
				hasProperty("status", is(CustomerStatus.valueOf(resource.getStatus())))));

	}

	@Test(expected = IllegalArgumentException.class)
	public void toResource_NullCustomerGiven_ShouldThrowException() {
		// Set up
		Customer customer = null;

		// Do test
		parser.toResource(customer);
	}

	@Test
	public void toResource_ValidCustomerGiven_ShouldCreateResourceModel() {
		// Set up
		Customer customer = CustomerTestBuilder.buildModel();

		// Do test
		CustomerResource result = parser.toResource(customer);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", is(customer.getId())), hasProperty("name", is(customer.getName())),
						hasProperty("email", is(customer.getEmail())), hasProperty("password", nullValue()),
						hasProperty("status", is(CustomerStatus.ACTIVE.name()))));

	}

}
