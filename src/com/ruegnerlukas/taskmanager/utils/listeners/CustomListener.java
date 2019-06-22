package com.ruegnerlukas.taskmanager.utils.listeners;

public interface CustomListener<T> {


	CustomListener<T> addTo(T observable);


	CustomListener<T> removeFrom(T observable);


	CustomListener<T> removeFromAll();


	void setSilenced(boolean silenced);


	boolean isSilenced();


}
