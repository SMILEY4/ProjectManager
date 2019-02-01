package com.ruegnerlukas.taskmanager.architecture;

public class Response<T> {


	public enum State {
		SUCCESS,
		FAIL,
		UNKNOWN
	}






	public final State state;
	public final String message;
	public final T value;




	public Response(State state, String message) {
		this(state, message, null);
	}




	public Response(State state, T value) {
		this(state, "", value);
	}




	public Response(State state, String message, T value) {
		this.state = state;
		this.message = message;
		this.value = value;
	}

}
