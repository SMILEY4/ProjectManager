package com.ruegnerlukas.taskmanager.architecture;

public abstract class Request<T> {


	private final boolean onlyOnSuccess;




	public Request() {
		this(false);
	}




	public Request(boolean onlyOnSuccess) {
		this.onlyOnSuccess = onlyOnSuccess;
	}




	public void respond(Response<T> response) {
		if(!onlyOnSuccess || response.getState() == Response.State.SUCCESS) {
			onResponse(response);
		}
	}




	public abstract void onResponse(Response<T> response);


}


