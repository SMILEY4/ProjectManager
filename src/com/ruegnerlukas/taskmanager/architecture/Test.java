package com.ruegnerlukas.taskmanager.architecture;

public class Test {


	public static Test test = new Test();


	public void doStuff(Request<String> request) {
		request.respond(new Response<>(Response.State.SUCCESS, "some Message", "Hello World"));
	}


}
