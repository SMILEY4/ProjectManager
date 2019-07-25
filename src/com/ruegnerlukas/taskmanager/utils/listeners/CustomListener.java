package com.ruegnerlukas.taskmanager.utils.listeners;

public interface CustomListener<T> {


	/**
	 * Adds this listener a the given observable object.
	 *
	 * @return this listener for chaining
	 */
	CustomListener<T> addTo(T observable);


	/**
	 * Removes this listener from the the given observable object.
	 *
	 * @return this listener for chaining
	 */
	CustomListener<T> removeFrom(T observable);


	/**
	 * Removes this listener from all observable objects.
	 *
	 * @return this listener for chaining
	 */
	CustomListener<T> removeFromAll();


	/**
	 * Mute/Unmute this listener. A muted listener will not fire.
	 */
	void setMuted(boolean muted);


	/**
	 * Whether this listener is mtuted. A muted listener will not fire.
	 */
	boolean isMuted();


}
