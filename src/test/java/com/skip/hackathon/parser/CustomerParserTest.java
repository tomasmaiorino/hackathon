package com.skip.hackathon.parser;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.skip.hackathon.model.Customer;
import com.skip.hackathon.model.Customer.CustomerStatus;
import com.skip.hackathon.resource.CustomerResource;
import com.skip.hackathon.util.CustomerTestBuilder;
import com.skip.parser.CustomerParser;

@FixMethodOrder(MethodSorters.JVM)
public class CustomerParserTest {

	private CustomerParser parser = new CustomerParser();

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

		// Do test
		Customer result = parser.toModel(resource);

		// Assertions
		assertNotNull(result);
		assertThat(result, allOf(hasProperty("id", nullValue()), hasProperty("name", is(resource.getName())),
				hasProperty("email", is(resource.getEmail())), hasProperty("password", is(resource.getPassword())),
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
