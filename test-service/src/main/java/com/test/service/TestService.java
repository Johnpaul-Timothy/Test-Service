package com.test.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.service.model.TestServiceResponse;

public class TestService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

	static Map<String, List<String>> contactsMap;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		LOGGER.debug("Event Request: {}", input);
		APIGatewayProxyResponseEvent apiProxyResponse = null;
		if (input.getHttpMethod().equalsIgnoreCase("GET")) {
			apiProxyResponse = handleAutoSuggestionRequest(input.getQueryStringParameters().get("cquery"));
		} else if (input.getHttpMethod().equalsIgnoreCase("POST")) {
			apiProxyResponse = handleGroupAnagramsRequest(input.getBody());
		}
		LOGGER.debug("Event Response: {}", apiProxyResponse);
		return apiProxyResponse;
	}

	private static APIGatewayProxyResponseEvent handleGroupAnagramsRequest(String requestString) {
		List<String> anagramList = Arrays.asList(requestString.split(","));
		HashMap<HashMap<Character, Integer>, ArrayList<String>> dataMap = new HashMap<>();
		for (String str : anagramList) {
			HashMap<Character, Integer> tempMap = new HashMap<>();
			for (int i = 0; i < str.length(); i++) {
				if (tempMap.containsKey(str.charAt(i))) {
					int x = tempMap.get(str.charAt(i));
					tempMap.put(str.charAt(i), ++x);
				} else {
					tempMap.put(str.charAt(i), 1);
				}
			}
			if (dataMap.containsKey(tempMap)) {
				dataMap.get(tempMap).add(str);
			} else {
				ArrayList<String> tempList = new ArrayList<>();
				tempList.add(str);
				dataMap.put(tempMap, tempList);
			}
		}
		List<ArrayList<String>> result = new ArrayList<>();
		for (HashMap<Character, Integer> temp : dataMap.keySet()) {
			result.add(dataMap.get(temp));
		}
		APIGatewayProxyResponseEvent proxyResponse = new APIGatewayProxyResponseEvent();
		TestServiceResponse resp = new TestServiceResponse();
		resp.setSource("Test Service");
		resp.setOutput(result.toString());
		ObjectMapper om = new ObjectMapper();
		try {
			proxyResponse.setBody(om.writeValueAsString(resp));
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while sending response: {}", e);
		}
		return proxyResponse;
	}

	private static APIGatewayProxyResponseEvent handleAutoSuggestionRequest(String requestString) {
		String contacts[] = { "john", "paul", "mani", "josh" };
		APIGatewayProxyResponseEvent proxyResponse = new APIGatewayProxyResponseEvent();
		TestServiceResponse resp = new TestServiceResponse();
		if (requestString != null && !requestString.isEmpty()) {
			if (contactsMap != null) {
				setContact(requestString, resp, contacts);
			} else {
				contactsMap = new HashMap<>();
				setContact(requestString, resp, contacts);
			}
		} else {
			resp.setContacts(null);
		}
		resp.setSource("Test Service");
		ObjectMapper om = new ObjectMapper();
		try {
			proxyResponse.setBody(om.writeValueAsString(resp));
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while sending response: {}", e);
		}
		return proxyResponse;
	}

	private static Boolean getContact(String query, String contacts[]) {
		List<String> contactList = new ArrayList<>();
		Boolean isFound = false;
		if (!contactsMap.containsKey(query)) {
			LOGGER.info("Data Not found - adding it");
			for (String contact : contacts) {
				String ct = contact.toLowerCase();
				if (ct.startsWith(query)) {
					contactList.add(ct);
					isFound = true;
				}
			}
			contactsMap.put(query, contactList);
		}
		return isFound;
	}

	private static void setContact(String requestString, TestServiceResponse resp, String[] contacts) {
		Boolean cstatus = getContact(requestString.toLowerCase(), contacts);
		if (!cstatus) {
			resp.setOutput("NO");
		}
		resp.setContacts(contactsMap);
	}

}
