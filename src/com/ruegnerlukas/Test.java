package com.ruegnerlukas;

import com.ruegnerlukas.taskmanager.architecture.Response;

public class Test {


	public static void main(String[] args) throws InterruptedException {
		new Test();
	}




	private Test() throws InterruptedException {
		System.out.println("===========");


		System.out.println("Call");
		Response<String> response = func(1, 5, true);
		System.out.println("Return");

		Thread.sleep(5000);
		System.out.println("===========");
	}




	public Response<String> func(long min, long max, boolean parallel) {

		if (min <= max) {

			Response<String> response = new Response<>();

			Thread t = new Thread(() -> {
				try {
					System.out.println("Start");
					Thread.sleep(1000);
					long sum = min+max;
					Thread.sleep(1000);
					System.out.println("Done");
					response.complete("sum = " + sum, Response.State.SUCCESS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			t.start();

			return response;

		} else {
			return new Response<String>().complete("Failed", Response.State.FAIL);
		}


	}


}
