package com.hong.vertx;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.apex.RoutingContext;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.vertx.model.Person;

public class PersonJsonApiHandler {

	private static Map<String, Person> persons = new HashMap<String, Person>();
	static {
		persons.put("1", new Person("홍길동", 32));
		persons.put("2", new Person("나철수", 28));
	}

	public void handleGetPersons(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			response.putHeader("content-type", "application/json; charset=utf-8").end(objectMapper.writeValueAsString(persons.values()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void handleAddPerson(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();

		String name = routingContext.request().getParam("name");
		int age = Integer.parseInt(routingContext.request().getParam("age"));

		System.out.println("name : " + name + "\tage + " + age);

		Person person = new Person();
		person.setName(name);
		person.setAge(age);

		persons.put(String.valueOf(persons.size() + 1), person);

		response.end();

	}

}
