package com.skip.hackathon.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer")
public class Customer extends BaseModel {

	private Customer() {

	}

	@Id
	@GeneratedValue
	@Getter
	private Integer id;

	public enum CustomerStatus {
		ACTIVE, INACTIVE;
	}

	@Getter
	@Column(nullable = false, length = 30)
	private String name;

	@Getter
	@Column(nullable = false, length = 30)
	private String email;

	@Getter
	@Column(nullable = false)
	private String password;

	@Column(name = "is_admin")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Getter
	@Setter
	public Boolean enable = true;

	@Getter
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private CustomerStatus status;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(final Set<Role> roles) {
		Assert.notEmpty(roles, "The roles must not be null or empty!");

		if (!CollectionUtils.isEmpty(this.getRoles())) {
			this.getRoles().clear();
		} else {
			this.roles = new HashSet<>();
		}
		if (!CollectionUtils.isEmpty(roles)) {
			this.roles.addAll(roles);
		}

		this.roles = roles;

	}

	public void setPassword(final String password) {
		Assert.hasText(password, "The password must not be null or empty!");
		this.password = password;
	}

	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null or empty!");
		this.name = name;
	}

	public void setEmail(final String email) {
		Assert.hasText(email, "The email must not be null or empty!");
		this.email = email;
	}

	public void setCustomerStatus(final CustomerStatus status) {
		Assert.notNull(status, "The status must not be null!");
		this.status = status;
	}

	public void addRole(final Role role) {
		Assert.notNull(role, "The role must not be null!");
		if (CollectionUtils.isEmpty(roles)) {
			roles = new HashSet<>();
		}
		roles.add(role);

	}

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
		Customer other = (Customer) obj;
		if (getId() == null || other.getId() == null) {
			return false;
		}
		return getId().equals(other.getId());
	}

	public static class CustomerBuilder {

		private final Customer customer;

		private CustomerBuilder(final String name, final String email, final String password,
				final CustomerStatus status) {
			customer = new Customer();
			customer.setEmail(email);
			customer.setName(name);
			customer.setPassword(password);
			customer.setCustomerStatus(status);

		}

		public static CustomerBuilder Customer(final String name, final String email, final String password,
				final CustomerStatus status) {
			return new CustomerBuilder(name, email, password, status);
		}

		public Customer build() {
			return customer;
		}
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
