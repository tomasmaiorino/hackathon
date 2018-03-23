package com.skip.hackathon.service;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.skip.exceptions.BadRequestException;
import com.skip.exceptions.ResourceNotFoundException;
import com.skip.hackathon.model.Customer;
import com.skip.hackathon.repository.CustomerRepository;
import com.skip.hackathon.util.CustomerTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class CustomerServiceTest {

	@InjectMocks
	private CustomerService service;

	@Mock
	private CustomerRepository mockRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullCustomerGiven_ShouldThrowException() {
		// Set up
		Customer customer = null;

		// Do test
		try {
			service.save(customer);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_DuplicatedEmailGiven_ShouldThrowException() {
		// Set up
		Customer customer = CustomerTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

		// Do test
		try {
			service.save(customer);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(mockRepository).findByEmail(customer.getEmail());
		verify(mockRepository, times(0)).save(customer);
	}

	@Test
	public void save_ValidCustomerGiven_ShouldCreateCustomer() {
		// Set up
		Customer customer = CustomerTestBuilder.buildModel();

		// Expectations
		when(mockRepository.save(customer)).thenReturn(customer);
		when(mockRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());

		// Do test
		Customer result = service.save(customer);

		// Assertions
		verify(mockRepository).save(customer);
		verify(mockRepository).findByEmail(customer.getEmail());
		assertNotNull(result);
		assertThat(result, is(customer));
	}

	@Test
	public void findByEmail_NullCustomerEmailGiven_ShouldThrowException() {
		// Set up
		String token = null;

		// Do test
		try {
			service.findByEmail(token);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByEmail_EmptyCustomerEmailGiven_ShouldThrowException() {
		// Set up
		String token = "";

		// Do test
		try {
			service.findByEmail(token);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByEmail_CustomerNotFound_ShouldThrowException() {
		// Set up
		String email = CustomerTestBuilder.getValidEmail();

		// Expectations
		when(mockRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findByEmail(email);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findByEmail(email);
	}

	@Test
	public void findByEmail_CustomerFound_ShouldReturnCustomer() {
		// Set up
		String email = CustomerTestBuilder.getValidEmail();
		Customer customer = CustomerTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByEmail(email)).thenReturn(Optional.of(customer));

		// Do test
		Customer result = service.findByEmail(email);

		// Assertions
		verify(mockRepository).findByEmail(email);

		assertNotNull(result);
		assertThat(result, is(customer));
	}

	//
	@Test
	public void loadUserByUsername_EmptyCustomerEmailGiven_ShouldThrowException() {
		// Set up
		String token = "";

		// Do test
		try {
			service.loadUserByUsername(token);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void loadUserByUsername_CustomerNotFound_ShouldThrowException() {
		// Set up
		String email = CustomerTestBuilder.getValidEmail();

		// Expectations
		when(mockRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Do test
		try {
			service.loadUserByUsername(email);
			fail();
		} catch (UsernameNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findByEmail(email);
	}

	@Test
	public void loadUserByUsername_CustomerFound_ShouldReturnCustomer() {
		// Set up
		String email = CustomerTestBuilder.getValidEmail();
		Customer customer = CustomerTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByEmail(email)).thenReturn(Optional.of(customer));

		// Do test
		UserDetails result = service.loadUserByUsername(email);

		// Assertions
		verify(mockRepository).findByEmail(email);

		assertNotNull(result);
		assertThat(result, allOf(hasProperty("username", is(customer.getEmail())),
				hasProperty("password", is(customer.getPassword()))));
	}
}
