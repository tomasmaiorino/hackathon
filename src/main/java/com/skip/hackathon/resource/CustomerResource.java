package com.skip.hackathon.resource;

import static com.skip.util.CustomerConstants.CUSTOMER_MAX_NAME_SIZE;
import static com.skip.util.CustomerConstants.CUSTOMER_MAX_PASSWORD_SIZE;
import static com.skip.util.CustomerConstants.CUSTOMER_MIN_NAME_SIZE;
import static com.skip.util.CustomerConstants.CUSTOMER_MIN_PASSWORD_SIZE;
import static com.skip.util.ErrorCodesConstants.INVALID_CUSTOMER_EMAIL;
import static com.skip.util.ErrorCodesConstants.INVALID_CUSTOMER_NAME_SIZE;
import static com.skip.util.ErrorCodesConstants.INVALID_CUSTOMER_PASSWORD_SIZE;
import static com.skip.util.ErrorCodesConstants.INVALID_STATUS;
import static com.skip.util.ErrorCodesConstants.REQUIRED_CUSTOMER_EMAIL;
import static com.skip.util.ErrorCodesConstants.REQUIRED_CUSTOMER_NAME;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class CustomerResource extends BaseResource {

	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_CUSTOMER_NAME)
	@Size(min = CUSTOMER_MIN_NAME_SIZE, max = CUSTOMER_MAX_NAME_SIZE, message = INVALID_CUSTOMER_NAME_SIZE)
	private String name;

	@Getter
	@Setter
	@NotEmpty(message = REQUIRED_CUSTOMER_EMAIL)
	@Email(message = INVALID_CUSTOMER_EMAIL)
	private String email;

	@Getter
	@Setter
	@Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_STATUS)
	private String status;

	@Getter
	@Setter
	@Size(min = CUSTOMER_MIN_PASSWORD_SIZE, max = CUSTOMER_MAX_PASSWORD_SIZE, message = INVALID_CUSTOMER_PASSWORD_SIZE)
	private String password;

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		CustomerResource other = (CustomerResource) obj;
		if (getId() == null || other.getId() == null) {
			return false;
		}
		return getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
