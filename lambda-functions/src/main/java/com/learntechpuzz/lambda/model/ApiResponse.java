package com.learntechpuzz.lambda.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApiResponse {

	private String message;

	public ApiResponse(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}
