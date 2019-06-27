package com.learntechpuzz.lambda.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.learntechpuzz.lambda.exception.BadRequestException;
import com.learntechpuzz.lambda.exception.InternalErrorException;
import com.learntechpuzz.lambda.model.ApiResponse;
import com.learntechpuzz.lambda.model.User;

public class UserHandler extends AbstractHandler {

	private static final String DYNAMODB_TABLE_NAME = System.getenv("USER_TABLE_NAME");

	public void createUser(InputStream request, OutputStream response, Context context)
			throws BadRequestException, InternalErrorException {
		LambdaLogger logger = context.getLogger();

		JsonParser parser = new JsonParser();
		JsonObject inputObj;
		try {
			inputObj = parser.parse(IOUtils.toString(request, "UTF-8")).getAsJsonObject();
			logger.log("\ninputObj: " + inputObj);
		} catch (Exception e) {
			logger.log("\nError while reading request: " + e.getMessage());
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}

		JsonObject body = null;
		if (inputObj.get("body") != null) {
			body = inputObj.get("body").getAsJsonObject();
		} else {
			logger.log("\nInvalid input");
			throw new BadRequestException("Invalid input");
		}
		logger.log("\nbody: " + body);

		User user = getGson().fromJson(body, User.class);
		logger.log("\nuser: " + body);

		try {
			AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
			DynamoDB dynamoDb = new DynamoDB(client);
			logger.log("\nDYNAMODB_TABLE_NAME: " + DYNAMODB_TABLE_NAME);
			dynamoDb.getTable(DYNAMODB_TABLE_NAME)
					.putItem(new PutItemSpec().withItem(
							new Item().withString("UserName", user.getUserName()).withString("Name", user.getName())
									.withString("Email", user.getEmail()).withString("Mobile", user.getMobile())));
		} catch (Exception e) {
			logger.log("\nError while saving user: " + e.getMessage());
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}

		ApiResponse apiResponse = new ApiResponse("Success");

		String output = getGson().toJson(apiResponse);
		logger.log("\noutput: " + output);

		try {
			IOUtils.write(output, response, "UTF-8");
		} catch (final IOException e) {
			logger.log("\nError while writing response" + e.getMessage());
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}
	}

	public void createUserPostConfirmation(InputStream request, OutputStream response, Context context)
			throws BadRequestException, InternalErrorException {
		LambdaLogger logger = context.getLogger();

		JsonParser parser = new JsonParser();
		JsonObject inputObj;
		try {
			inputObj = parser.parse(IOUtils.toString(request, "UTF-8")).getAsJsonObject();
			logger.log("\ninputObj: " + inputObj);
		} catch (Exception e) {
			logger.log("\nError while reading request: " + e.getMessage());
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}

		JsonObject body = null;
		if (inputObj.get("request") != null) {
			body = inputObj.get("request").getAsJsonObject().get("userAttributes").getAsJsonObject();
		} else {
			logger.log("\nInvalid input");
			throw new BadRequestException("Invalid input");
		}
		logger.log("\nbody: " + body);


		try {
			AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
			DynamoDB dynamoDb = new DynamoDB(client);
			logger.log("\nDYNAMODB_TABLE_NAME: " + DYNAMODB_TABLE_NAME);
			dynamoDb.getTable(DYNAMODB_TABLE_NAME)
					.putItem(new PutItemSpec().withItem(
							new Item().withString("UserName", inputObj.get("userName").getAsString()).withString("Name", body.get("name").getAsString())
									.withString("Email", body.get("email").getAsString()).withString("Mobile", body.get("phone_number").getAsString())));
		} catch (Exception e) {
			logger.log("\nError while saving user: " + e.getMessage());
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}


		String output = getGson().toJson(inputObj);
		logger.log("\noutput: " + output);

		try {
			IOUtils.write(output, response, "UTF-8");
		} catch (final IOException e) {
			logger.log("\nError while writing response" + e.getMessage());
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}
	}
	
}