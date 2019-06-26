package com.learntechpuzz.lambda.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AbstractHandler {

	protected Gson getGson() {
		return new GsonBuilder()
				// .enableComplexMapKeySerialization()
				// .serializeNulls()
				// .setDateFormat(DateFormat.LONG)
				// .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting().create();
	}

}
