package com.learntechpuzz.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.learntechpuzz.lambda.model.User;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class UserHandler implements RequestStreamHandler {

	private JSONParser parser = new JSONParser();
	private static final String DYNAMODB_TABLE_NAME = System.getenv("TABLE_NAME");

	@SuppressWarnings("unchecked")
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		LambdaLogger logger = context.getLogger();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		JSONObject responseJson = new JSONObject();

		// Response header
		JSONObject headerJson = new JSONObject();
		headerJson.put("Access-Control-Allow-Origin", "*");
		responseJson.put("headers", headerJson);

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDB dynamoDb = new DynamoDB(client);
		logger.log("dynamoDb: " + dynamoDb);
		try {
			JSONObject event = (JSONObject) parser.parse(reader);

			logger.log("body: " + event.get("body"));
			if (event.get("body") != null) {

				User user = new User((String) event.get("body"));
				logger.log("user: " + user);
				dynamoDb.getTable(DYNAMODB_TABLE_NAME)
						.putItem(new PutItemSpec().withItem(
								new Item().withString("UserName", user.getUserName()).withString("Name", user.getName())
										.withString("Email", user.getEmail()).withString("Mobile", user.getMobile())));

				JSONObject bodyJson = new JSONObject();
				bodyJson.put("message", "New user created");
				responseJson.put("body", bodyJson);
				responseJson.put("statusCode", 200);
			}else {
				responseJson.put("statusCode", 400);
			}

		} catch (ParseException pex) {
			responseJson.put("statusCode", 400);
			responseJson.put("exception", pex);
		} catch (Exception ex) {
			responseJson.put("statusCode", 500);
			responseJson.put("exception", ex);
		}

		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		writer.write(responseJson.toString());
		writer.close();
	}

}