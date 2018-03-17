package com.skip.parser;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.skip.hackathon.model.Customer;
import com.skip.hackathon.model.Customer.CustomerStatus;
import com.skip.hackathon.model.Role;
import com.skip.hackathon.resource.CustomerResource;

@Component
public class CustomerParser implements IParser<CustomerResource, Customer> {

	@Override
	public Customer toModel(CustomerResource resource) {
		Assert.notNull(resource, "The resource must not be null!");
		Customer customer = Customer.CustomerBuilder.Customer(resource.getName(), resource.getEmail(),
				// bCryptPasswordEncoder.encode(resource.getPassword()),
				// CustomerStatus.valueOf(resource.getStatus()))
				resource.getPassword(), CustomerStatus.valueOf(resource.getStatus())).build();

		Role role = Role.RoleBuilder.Role(Role.Roles.USER).build();
		customer.addRole(role);

		return customer;
	}

	public CustomerResource toResource(final Customer customer) {
		Assert.notNull(customer, "The customer must not be null!");
		CustomerResource resource = new CustomerResource();
		resource.setId(customer.getId());
		resource.setEmail(customer.getEmail());
		resource.setName(customer.getName());
		resource.setStatus(customer.getStatus().toString());
		resource.setId(customer.getId());
		return resource;
	}

	@Override
	public Set<CustomerResource> toResources(Set<Customer> models) {
		// TODO Auto-generated method stub
		return null;
	}

}
