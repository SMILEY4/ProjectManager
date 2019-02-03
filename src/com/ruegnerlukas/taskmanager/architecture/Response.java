package com.ruegnerlukas.taskmanager.architecture;

public class Response<T> {


	public enum State {
		SUCCESS,
		FAIL,
		UNKNOWN
	}






	private final State state;
	private final String message;
	private final T value;




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




	public T getValue() {
		return value;
	}




	public State getState() {
		return state;
	}




	public String getMessage() {
		return message;
	}

}
