package com.ruegnerlukas;

public class Test {


	public static void main(String[] args) {
		String str = doStuff(new Request<String>());
	}




	public static <T> T doStuff(Request<T> request) {
		return request.getValue();
	}




	static class Request<T> {


		public T getValue() {
			return null;
		}

	}

}
