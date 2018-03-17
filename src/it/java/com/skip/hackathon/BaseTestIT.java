package com.skip.hackathon;

import java.util.HashMap;
import java.util.Map;

public class BaseTestIT {

	public static Map<String, String> header = null;
	protected static final String REQUIRED_NAME = "The name is required.";
	protected static final String INVALID_STATUS = "The status must be either 'INACTIVE' or 'ACTIVE'.";
	protected static final String REQUIRED_STATUS = "The status is required.";
	protected static final String MESSAGE_FIELD = "message";
	protected static final String STATUS_KEY = "status";
	protected static final String MESSAGE_CHECK_KEY = "[0].message";
	protected static final String MESSAGE_FIELD_KEY = "[0].field";

	private String itSecurityPass;

	public static final String ADMIN_TOKEN_HEADER = "AT";

	public Map<String, String> getHeader() {
		if (header == null) {
			header = new HashMap<>();
			header.put(ADMIN_TOKEN_HEADER, itSecurityPass);
		}
		return header;
	}
}
