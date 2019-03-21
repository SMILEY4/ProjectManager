package com.ruegnerlukas.taskmanager.architecture;

public class Response<T> {


	public enum State {
		SUCCESS,
		FAIL,
		UNKNOWN
	}






	public static final long WAIT_INTERVALL = 5;

	private State state;
	private T value;
	private volatile boolean complete = false;




	public Response<T> complete(T value) {
		return this.complete(value, State.SUCCESS);
	}




	public Response<T> complete(T value, State state) {
		if (!complete) {
			this.value = value;
			this.state = state;
			complete = true;
		}
		return this;
	}




	public State getState() {
		if (!complete) {
			waitForComplete();
		}
		return state;
	}




	public T getValue() {
		if (!complete) {
			waitForComplete();
		}
		return value;
	}




	private void waitForComplete() {
		try {
			do {
				Thread.sleep(WAIT_INTERVALL);
			} while (!complete);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}




	public boolean isComplete() {
		return complete;
	}


}
