package com.skip.hackathon.util;
import com.skip.hackathon.util.BaseTestBuilder;
import com.skip.hackathon.model.Customer;
import com.skip.hackathon.model.Customer.CustomerStatus;
import com.skip.hackathon.resource.CustomerResource;
import com.skip.util.CustomerConstants;

public class CustomerTestBuilder extends BaseTestBuilder {

	public static final String INVALID_EMAIL = getInvalidEmail();
	public static final String LARGE_NAME = getLargeString(CustomerConstants.CUSTOMER_MAX_NAME_SIZE);
	public static final String SMALL_NAME = getSmallString(CustomerConstants.CUSTOMER_MIN_NAME_SIZE);
	public static final String LARGE_EMAIL = getLargeString(CustomerConstants.CUSTOMER_MAX_EMAIL_SIZE);
	public static final String SMALL_EMAIL = getSmallString(CustomerConstants.CUSTOMER_MIN_EMAIL_SIZE);
	public static final String LARGE_PASSWORD = getLargeString(CustomerConstants.CUSTOMER_MAX_PASSWORD_SIZE);
	public static final String SMALL_PASSWORD = getSmallString(CustomerConstants.CUSTOMER_MIN_PASSWORD_SIZE);

	public static Customer buildModel() {
		return buildModel(getName(), getValidEmail(), getPassword(), getCustomerStatus());
	}

	public static Customer buildModel(final String name, final String email, final String password,
			final CustomerStatus customerStatus) {
		return Customer.CustomerBuilder.Customer(name, email, password, customerStatus).build();
	}

	public static CustomerResource buildResource() {
		return buildResource(getName(), getValidEmail(), getPassword(), getCustomerStatusAsString());
	}

	public static CustomerResource buildResource(final String name, final String validEmail, final String password,
			final String customerStatusAsString) {
		CustomerResource resource = new CustomerResource();
		resource.setEmail(validEmail);
		resource.setName(name);
		resource.setPassword(password);
		resource.setStatus(customerStatusAsString);
		return resource;
	}

	public static String getName() {
		return getString(CustomerConstants.CUSTOMER_MAX_EMAIL_SIZE);
	}

	public static String getPassword() {
		return getString(CustomerConstants.CUSTOMER_MAX_PASSWORD_SIZE);
	}

	public static String getCustomerStatusAsString() {
		return getCustomerStatus().name();
	}

	public static CustomerStatus getCustomerStatus() {
		return CustomerStatus.ACTIVE;
	}

}
