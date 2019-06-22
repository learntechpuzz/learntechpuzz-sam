package com.learntechpuzz.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.learntechpuzz.lambda.model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class UserHandler implements RequestStreamHandler {

    private JSONParser parser = new JSONParser();
    private static final String DYNAMODB_TABLE_NAME = System.getenv("TABLE_NAME");

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDb = new DynamoDB(client);

        try {
            JSONObject event = (JSONObject) parser.parse(reader);

            if (event.get("body") != null) {

                User user = new User((String) event.get("body"));

                dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                    .putItem(new PutItemSpec().withItem(new Item()
                        .withString("Name", user.getName())
                        .withString("Email", user.getEmail())
                        .withString("Mobile", user.getMobile())
                        ));
            }

            JSONObject responseBody = new JSONObject();
            responseBody.put("message", "New user created");

            JSONObject headerJson = new JSONObject();
            headerJson.put("Access-Control-Allow-Methods", "POST");
            headerJson.put("Access-Control-Allow-Headers", "Authorization");
            headerJson.put("Access-Control-Allow-Origin", "*");

            responseJson.put("statusCode", 200);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());

        } catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }

}