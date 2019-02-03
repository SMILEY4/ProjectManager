package com.ruegnerlukas.taskmanager.architecture;

public abstract class Request<T> {


	private final boolean onlyOnSuccess;




	public Request() {
		this(false);
	}




	public Request(boolean onlyOnSuccess) {
		this.onlyOnSuccess = onlyOnSuccess;
	}




	public void respond(Response response) {
		if (onlyOnSuccess) {
			if (response.state == Response.State.SUCCESS) {
				onResponse(response);
			}
		} else {
			onResponse(response);
		}
	}




	public abstract void onResponse(Response response);

}
