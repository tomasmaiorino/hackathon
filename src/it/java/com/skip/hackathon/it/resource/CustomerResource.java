package com.skip.hackathon.it.resource;

import static com.jayway.restassured.RestAssured.given;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skip.hackathon.CustomerControllerIT;
import com.skip.hackathon.util.CustomerTestBuilder;

import lombok.Getter;
import lombok.Setter;

public class CustomerResource {

	@JsonIgnore
	@Getter
	@Setter
	private Map<String, String> headers;

	public static CustomerResource build() {
		return new CustomerResource();
	}

	public CustomerResource assertFields() {

		if (Objects.isNull(name)) {
			name();
		}
		if (Objects.isNull(email)) {
			email();
		}
		if (Objects.isNull(status)) {
			status();
		}
		if (Objects.isNull(password)) {
			password();
		}
		return this;
	}

	private CustomerResource() {
	}

	@Getter
	private String name;

	@Getter
	private String email;

	@Getter
	private String password;

	@Getter
	private String status;

	public CustomerResource create() {
		assertFields();
		return given().headers(getHeaders()).contentType("application/json").body(this).when()
				.post(CustomerControllerIT.CUSTOMERS_END_POINT).as(CustomerResource.class);
	}

	public CustomerResource headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public CustomerResource password(final String password) {
		this.password = password;
		return this;
	}

	public CustomerResource password() {
		return password(CustomerTestBuilder.getPassword());
	}

	public CustomerResource name(final String name) {
		this.name = name;
		return this;
	}

	public CustomerResource name() {
		return name(CustomerTestBuilder.getName());
	}

	public CustomerResource email(final String email) {
		this.email = email;
		return this;
	}

	public CustomerResource email() {
		return email(CustomerTestBuilder.getValidEmail());
	}

	public CustomerResource status() {
		return status(CustomerTestBuilder.getCustomerStatusAsString());
	}

	public CustomerResource status(final String status) {
		this.status = status;
		return this;
	}

}
