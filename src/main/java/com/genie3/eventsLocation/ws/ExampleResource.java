package com.genie3.eventsLocation.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/example")
public class ExampleResource {

	public static class ExampleClass {
		public String field;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/aircraft")
	public ExampleClass getExample() {
		ExampleClass instance = new ExampleClass();
		instance.field = "Test";

		return instance;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/aircraft")
	public void retrieveExample(ExampleClass instance) {
		System.out.println(instance.field);
	}

}
