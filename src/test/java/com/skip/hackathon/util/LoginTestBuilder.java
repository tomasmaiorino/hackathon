package com.skip.hackathon.util;

import com.skip.hackathon.resource.LoginResource;

public class LoginTestBuilder extends CustomerTestBuilder {

	public static LoginResource buildLoginResource() {
		return buildResource(getValidEmail(), getPassword());
	}

	public static LoginResource buildResource(final String validEmail, final String password) {
		LoginResource resource = new LoginResource();
		resource.setEmail(validEmail);
		resource.setPassword(password);
		return resource;
	}

}
