package com.skip.hackathon.resource;

import static com.skip.util.CustomerConstants.CUSTOMER_MAX_PASSWORD_SIZE;
import static com.skip.util.CustomerConstants.CUSTOMER_MIN_PASSWORD_SIZE;
import static com.skip.util.ErrorCodesConstants.INVALID_CUSTOMER_EMAIL;
import static com.skip.util.ErrorCodesConstants.INVALID_CUSTOMER_PASSWORD_SIZE;
import static com.skip.util.ErrorCodesConstants.REQUIRED_CUSTOMER_EMAIL;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class AuthenticationResource extends BaseResource {

	@Getter
	@Setter
	@NotEmpty(message = REQUIRED_CUSTOMER_EMAIL)
	@Email(message = INVALID_CUSTOMER_EMAIL)
	private String email;

	@Getter
	@Setter
	@Size(min = CUSTOMER_MIN_PASSWORD_SIZE, max = CUSTOMER_MAX_PASSWORD_SIZE, message = INVALID_CUSTOMER_PASSWORD_SIZE)
	private String password;

	@Getter
	@Setter
	private String token;

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
