package com.ruegnerlukas.taskmanager_old.architecture;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;

public class SyncRequest<T> extends Request<T> {


	public static final long DEFAULT_SLEEP_INTERVALL = 2;

	private volatile com.ruegnerlukas.taskmanager.architecture.Response<T> response;
	private long sleepIntervall = DEFAULT_SLEEP_INTERVALL;




	public SyncRequest() {
		this(DEFAULT_SLEEP_INTERVALL);
	}




	public SyncRequest(long sleepIntervall) {
		super(false);
		this.sleepIntervall = sleepIntervall;
	}




	@Override
	public void onResponse(com.ruegnerlukas.taskmanager.architecture.Response<T> response) {
		this.response = response;
	}




	public Response<T> getResponse() {
		try {
			while (response == null) {
				Thread.sleep(sleepIntervall);
			}
		} catch (InterruptedException e) {
			Logger.get().warn(e);
		}
		return response;
	}

}
