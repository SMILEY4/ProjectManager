package com.ruegnerlukas.taskmanager.architecture;

public class SyncRequest extends Request {


	public static final long DEFAULT_SLEEP_MS = 4;

	private volatile Response response = null;
	private final long sleepMS;




	public SyncRequest() {
		this(DEFAULT_SLEEP_MS);
	}




	public SyncRequest(long sleepMS) {
		this.sleepMS = sleepMS;
	}




	@Override
	public void onResponse(Response response) {
		this.response = response;
	}




	public Response getResponse() {
		try {
			while (response == null) {
				Thread.sleep(sleepMS);
			}
			return response;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
