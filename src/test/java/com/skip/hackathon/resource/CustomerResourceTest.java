package com.skip.hackathon.resource;

import static com.skip.hackathon.util.CustomerTestBuilder.INVALID_EMAIL;
import static com.skip.hackathon.util.CustomerTestBuilder.LARGE_NAME;
import static com.skip.hackathon.util.CustomerTestBuilder.LARGE_PASSWORD;
import static com.skip.hackathon.util.CustomerTestBuilder.SMALL_NAME;
import static com.skip.hackathon.util.CustomerTestBuilder.SMALL_PASSWORD;
import static com.skip.util.ErrorCodesConstants.INVALID_CUSTOMER_EMAIL;
import static com.skip.util.ErrorCodesConstants.INVALID_CUSTOMER_NAME_SIZE;
import static com.skip.util.ErrorCodesConstants.INVALID_CUSTOMER_PASSWORD_SIZE;
import static com.skip.util.ErrorCodesConstants.REQUIRED_CUSTOMER_EMAIL;
import static com.skip.util.ErrorCodesConstants.REQUIRED_CUSTOMER_NAME;
import static com.skip.util.ErrorCodesConstants.REQUIRED_CUSTOMER_PASSWORD;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.function.Supplier;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.skip.hackathon.util.BaseResourceTest;
import com.skip.hackathon.util.CustomerTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class CustomerResourceTest extends BaseResourceTest {

	private Supplier<? extends BaseResource> buildResourceFunction = CustomerTestBuilder::buildResource;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}

	@Test
	public void build_NullNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", null, REQUIRED_CUSTOMER_NAME);
	}

	@Test
	public void build_SmallNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", SMALL_NAME, INVALID_CUSTOMER_NAME_SIZE);
	}

	@Test
	public void build_LargeNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", LARGE_NAME, INVALID_CUSTOMER_NAME_SIZE);
	}

	@Test
	public void build_EmptyNameGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "name", "", INVALID_CUSTOMER_NAME_SIZE);
	}

	//
	// TODO

	@Test
	@Ignore
	public void build_NullPasswordGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "password", null, REQUIRED_CUSTOMER_PASSWORD);
	}

	@Test
	public void build_SmallPasswordGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "password", SMALL_PASSWORD, INVALID_CUSTOMER_PASSWORD_SIZE);
	}

	@Test
	public void build_LargePasswordGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "password", LARGE_PASSWORD, INVALID_CUSTOMER_PASSWORD_SIZE);
	}

	@Test
	public void build_EmptyPasswordGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "password", "", INVALID_CUSTOMER_PASSWORD_SIZE);
	}

	//

	@Test
	public void build_NullEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", null, REQUIRED_CUSTOMER_EMAIL);
	}

	@Test
	public void build_InvalidEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", INVALID_EMAIL, INVALID_CUSTOMER_EMAIL);
	}

	@Test
	public void build_EmptyEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", "", REQUIRED_CUSTOMER_EMAIL);
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		String name = CustomerTestBuilder.getName();
		String status = CustomerTestBuilder.getCustomerStatusAsString();
		String password = CustomerTestBuilder.getPassword();
		String email = CustomerTestBuilder.getValidEmail();

		CustomerResource result = CustomerTestBuilder.buildResource(name, email, password, status);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", nullValue()), hasProperty("name", is(name)), hasProperty("email", is(email)),
						hasProperty("password", is(password)), hasProperty("status", is(status))));
	}
}
