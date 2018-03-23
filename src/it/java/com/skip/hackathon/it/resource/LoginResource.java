package com.skip.hackathon.it.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skip.hackathon.CustomerControllerIT;
import com.skip.hackathon.util.CustomerTestBuilder;
import com.skip.hackathon.util.LoginTestBuilder;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

import static com.jayway.restassured.RestAssured.given;

public class LoginResource {

	@JsonIgnore
	@Getter
	@Setter
	private Map<String, String> headers;

	public static LoginResource build() {
		return new LoginResource();
	}

	public LoginResource assertFields() {

		if (Objects.isNull(email)) {
			email();
		}
		if (Objects.isNull(password)) {
			password();
		}
		return this;
	}

	private LoginResource() {
	}

	@Getter
	private String email;

	@Getter
	private String password;

	@Getter
	@Setter
	private String token;

	public void assertAuth() {
		if (Objects.isNull(email) || Objects.isNull(password)) {
			String createPassword = LoginTestBuilder.getPassword();
			CustomerResource customer = CustomerResource.build().email().password(createPassword).create();
			this.email = customer.getEmail();
			this.password = createPassword;
		}
	}

	public LoginResource auth() {
		this.assertAuth();
		return given().contentType("application/json").body(this).when()
				.post(CustomerControllerIT.CUSTOMERS_END_POINT_AUTH).as(LoginResource.class);
	}

	public LoginResource create() {
		assertFields();
		return given().headers(getHeaders()).contentType("application/json").body(this).when()
				.post(CustomerControllerIT.POST_CUSTOMERS_END_POINT).as(LoginResource.class);
	}

	public LoginResource headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public LoginResource password(final String password) {
		this.password = password;
		return this;
	}

	public LoginResource password() {
		return password(CustomerTestBuilder.getPassword());
	}

	public LoginResource email(final String email) {
		this.email = email;
		return this;
	}

	public LoginResource email() {
		return email(CustomerTestBuilder.getValidEmail());
	}

}
