package com.skip.hackathon;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.skip.hackathon.it.resource.CustomerResource;
import com.skip.hackathon.it.resource.LoginResource;
import com.skip.hackathon.util.CustomerTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.skip.hackathon.util.CustomerTestBuilder.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.apache.commons.lang3.RandomUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackathonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class CustomerControllerIT extends BaseTestIT {

	public static final String POST_CUSTOMERS_END_POINT = "/api/v1/customers";

	public static final String GET_PUT_CUSTOMERS_END_POINT = "/api/v1/customers/{id}";

	public static final String CUSTOMERS_END_POINT_AUTH = "/api/v1/customers/auth";

	private static final String AUTHORIZATION_KEY = "Authorization";

	private static final String AUTHORIZATION_VALUE_PREFIX = "Bearer ";

	@LocalServerPort
	private int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	//

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().name(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name is required."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().name("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	//

	@Test
	public void save_EmptyPasswordGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().password("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The password must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("password"));
	}

	@Test
	public void save_SmallPasswordGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().password(SMALL_PASSWORD);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The password must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("password"));
	}

	@Test
	public void save_LargePasswordGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().password(LARGE_PASSWORD);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The password must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("password"));
	}

	//
	//
	@Test
	public void save_NullEmailGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().email(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The email is required."), MESSAGE_FIELD_KEY, is("email"));
	}

	@Test
	public void save_EmptyEmailGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().email("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The email is required."), MESSAGE_FIELD_KEY, is("email"));
	}

	@Test
	public void save_InvalidEmailGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields()
				.email(CustomerTestBuilder.getInvalidEmail());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("Invalid email."), MESSAGE_FIELD_KEY, is("email"));
	}

	//
	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields().status(INVALID_STATUS);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The status must be either 'INACTIVE' or 'ACTIVE'."));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveCustomer() {
		// Set Up
		CustomerResource resource = CustomerResource.build().assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(POST_CUSTOMERS_END_POINT)
				.then().statusCode(HttpStatus.CREATED.value()).body("name", is(resource.getName()), "password",
						nullValue(), "status", is(resource.getStatus()), "email", is(resource.getEmail()));

	}

	//
	@Test
	public void auth_EmptyPasswordGiven_ShouldReturnError() {
		// Set Up
		LoginResource resource = LoginResource.build().assertFields().password("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CUSTOMERS_END_POINT_AUTH)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The password is required."), MESSAGE_FIELD_KEY, is("password"));
	}

	@Test
	public void auth_SmallPasswordGiven_ShouldReturnError() {
		// Set Up
		LoginResource resource = LoginResource.build().assertFields().password(SMALL_PASSWORD);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CUSTOMERS_END_POINT_AUTH)
				.then().statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void auth_LargePasswordGiven_ShouldReturnError() {
		// Set Up
		LoginResource resource = LoginResource.build().assertFields().password(LARGE_PASSWORD);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CUSTOMERS_END_POINT_AUTH)
				.then().statusCode(HttpStatus.FORBIDDEN.value());
	}

	//
	//
	@Test
	public void auth_NullEmailGiven_ShouldReturnError() {
		// Set Up
		LoginResource resource = LoginResource.build().assertFields().email(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CUSTOMERS_END_POINT_AUTH)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The email is required."), MESSAGE_FIELD_KEY, is("email"));
	}

	@Test
	public void auth_EmptyEmailGiven_ShouldReturnError() {
		// Set Up
		LoginResource resource = LoginResource.build().assertFields().email("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CUSTOMERS_END_POINT_AUTH)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The email is required."), MESSAGE_FIELD_KEY, is("email"));
	}

	@Test
	public void auth_InvalidEmailGiven_ShouldReturnError() {
		// Set Up
		CustomerResource customerResource = CustomerResource.build().assertFields().create();
		LoginResource resource = LoginResource.build().email(customerResource.getEmail()).password();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CUSTOMERS_END_POINT_AUTH)
				.then().statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void auth_notFoundUserGiven_ShouldReturnToken() {
		// Set Up
		String password = CustomerTestBuilder.getPassword();
		CustomerResource customerResource = CustomerResource.build().password(password).create();
		LoginResource resource = LoginResource.build().email(customerResource.getEmail()).password(password);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CUSTOMERS_END_POINT_AUTH)
				.then().statusCode(HttpStatus.OK.value()).body("token", notNullValue());

	}

	@Test
	public void auth_validLoginGiven_ShouldReturnToken() {
		// Set Up
		String password = CustomerTestBuilder.getPassword();
		CustomerResource customerResource = CustomerResource.build().password(password).create();
		LoginResource resource = LoginResource.build().email(customerResource.getEmail()).password(password);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CUSTOMERS_END_POINT_AUTH)
				.then().statusCode(HttpStatus.OK.value()).body("token", notNullValue());

	}

	//

	@Test
	public void findById_InvalidRequestGiven_ShouldReturnError() {
		// Set Up
		given().contentType(ContentType.JSON).when().get(GET_PUT_CUSTOMERS_END_POINT, RandomUtils.nextInt(100, 200))
				.then().statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void findById_InvalidTokenGiven_ShouldReturnError() {
		// Set Up
		CustomerResource customerResource = CustomerResource.build().create();
		getHeader().put(AUTHORIZATION_KEY, AUTHORIZATION_VALUE_PREFIX + "ewrwerwer");

		given().headers(getHeader()).contentType(ContentType.JSON).when()
				.get(GET_PUT_CUSTOMERS_END_POINT, customerResource.getId()).then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void findById_NotFoundCustomerGiven_ShouldReturnError() {
		// Set Up
		LoginResource loginResource = LoginResource.build().auth();
		getHeader().put(AUTHORIZATION_KEY, AUTHORIZATION_VALUE_PREFIX + loginResource.getToken());

		given().headers(getHeader()).contentType(ContentType.JSON).when()
				.get(GET_PUT_CUSTOMERS_END_POINT, RandomUtils.nextInt(100, 200)).then()
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void findById_ValidCustomerGiven_ShouldReturnError() {
		// Set Up
		String password = CustomerTestBuilder.getPassword();
		CustomerResource customerResource = CustomerResource.build().password(password).create();

		LoginResource loginResource = LoginResource.build().email(customerResource.getEmail()).password(password)
				.auth();

		getHeader().put(AUTHORIZATION_KEY, AUTHORIZATION_VALUE_PREFIX + loginResource.getToken());

		// Do Test
		given().headers(getHeader()).contentType(ContentType.JSON).when()
				.get(GET_PUT_CUSTOMERS_END_POINT, customerResource.getId()).then().statusCode(HttpStatus.OK.value())
				.body("name", is(customerResource.getName()), "password", nullValue(), "status",
						is(customerResource.getStatus()), "email", is(customerResource.getEmail()));
	}

	@Test
	public void update_InvalidRequestGiven_ShouldReturnError() {
		// Set Up
		String password = CustomerTestBuilder.getPassword();
		CustomerResource customerResource = CustomerResource.build().password(password).create();

		given().body(customerResource).contentType(ContentType.JSON).when()
				.put(GET_PUT_CUSTOMERS_END_POINT, customerResource.getId()).then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}
}
