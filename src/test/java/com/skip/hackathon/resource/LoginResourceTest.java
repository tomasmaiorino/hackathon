package com.skip.hackathon.resource;

import static com.skip.hackathon.util.CustomerTestBuilder.INVALID_EMAIL;
import static com.skip.util.ErrorCodesConstants.INVALID_LOGIN_EMAIL;
import static com.skip.util.ErrorCodesConstants.REQUIRED_LOGIN_EMAIL;
import static com.skip.util.ErrorCodesConstants.REQUIRED_LOGIN_PASSWORD;
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
import com.skip.hackathon.util.LoginTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class LoginResourceTest extends BaseResourceTest {

	private Supplier<? extends BaseResource> buildResourceFunction = LoginTestBuilder::buildLoginResource;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}

	@Test
	@Ignore
	public void build_NullPasswordGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "password", null, REQUIRED_LOGIN_PASSWORD);
	}


	@Test
	public void build_EmptyPasswordGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "password", "", REQUIRED_LOGIN_PASSWORD);
	}

	//

	@Test
	public void build_NullEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", null, REQUIRED_LOGIN_EMAIL);
	}

	@Test
	public void build_InvalidEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", INVALID_EMAIL, INVALID_LOGIN_EMAIL);
	}

	@Test
	public void build_EmptyEmailGiven_ShouldThrowException() {
		// Set up
		checkResource(buildResourceFunction, "email", "", REQUIRED_LOGIN_EMAIL);
	}

	@Test
	public void build_AllValuesGiven_AllValuesShouldSet() {
		// Set up
		String password = LoginTestBuilder.getPassword();
		String email = LoginTestBuilder.getValidEmail();

		LoginResource result = LoginTestBuilder.buildResource(email, password);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("email", is(email)),
						hasProperty("password", is(password))));
	}
}
