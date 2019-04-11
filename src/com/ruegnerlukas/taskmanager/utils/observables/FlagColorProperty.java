package com.ruegnerlukas.taskmanager.utils.observables;


import com.ruegnerlukas.taskmanager.data.TaskFlag;
import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;

import java.lang.ref.WeakReference;

public class FlagColorProperty implements Property<TaskFlag.FlagColor>, WritableObjectValue<TaskFlag.FlagColor> {


	private static final Object DEFAULT_BEAN = null;
	private static final String DEFAULT_NAME = "";

	private final Object bean;
	private final String name;

	private TaskFlag.FlagColor value;

	private ObservableValue<? extends TaskFlag.FlagColor> observable = null;
	private InvalidationListener listener = null;
	private boolean valid = true;
	private ExpressionHelper<TaskFlag.FlagColor> helper = null;




	public FlagColorProperty() {
		this(DEFAULT_BEAN, DEFAULT_NAME, null);
	}




	public FlagColorProperty(TaskFlag.FlagColor initialValue) {
		this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
	}




	public FlagColorProperty(Object bean, String name) {
		this(bean, name, null);
	}




	public FlagColorProperty(Object bean, String name, TaskFlag.FlagColor initialValue) {
		this.bean = bean;
		this.name = name;
		this.value = initialValue;
	}



	@Override
	public TaskFlag.FlagColor get() {
		valid = true;
		return observable == null ? value : observable.getValue();
	}




	@Override
	public void set(TaskFlag.FlagColor newValue) {
		if (isBound()) {
			throw new RuntimeException((getBean() != null && getName() != null ? getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
		}
		if ((value == null) ? newValue != null : !value.equals(newValue)) {
			value = newValue;
			markInvalid();
		}
	}



	@Override
	public TaskFlag.FlagColor getValue() {
		return get();
	}




	@Override
	public void setValue(TaskFlag.FlagColor newValue) {
		set(newValue);
	}




	@Override
	public void addListener(InvalidationListener listener) {
		helper = ExpressionHelper.addListener(helper, this, listener);
	}




	@Override
	public void removeListener(InvalidationListener listener) {
		helper = ExpressionHelper.removeListener(helper, listener);
	}




	@Override
	public void addListener(ChangeListener<? super TaskFlag.FlagColor> listener) {
		helper = ExpressionHelper.addListener(helper, this, listener);
	}




	@Override
	public void removeListener(ChangeListener<? super TaskFlag.FlagColor> listener) {
		helper = ExpressionHelper.removeListener(helper, listener);
	}




	protected void fireValueChangedEvent() {
		ExpressionHelper.fireValueChangedEvent(helper);
	}




	private void markInvalid() {
		if (valid) {
			valid = false;
			invalidated();
			fireValueChangedEvent();
		}
	}




	protected void invalidated() {
	}




	@Override
	public void bindBidirectional(Property<TaskFlag.FlagColor> other) {
		Bindings.bindBidirectional(this, other);
	}




	@Override
	public void unbindBidirectional(Property<TaskFlag.FlagColor> other) {
		Bindings.unbindBidirectional(this, other);
	}




	@Override
	public void bind(ObservableValue<? extends TaskFlag.FlagColor> newObservable) {
		if (newObservable == null) {
			throw new NullPointerException("Cannot bind to null");
		}
		if (!newObservable.equals(observable)) {
			unbind();
			observable = newObservable;
			if (listener == null) {
				listener = new Listener(this);
			}
			observable.addListener(listener);
			markInvalid();
		}
	}




	@Override
	public void unbind() {
		if (observable != null) {
			value = observable.getValue();
			observable.removeListener(listener);
			observable = null;
		}
	}




	@Override
	public boolean isBound() {
		return observable != null;
	}




	@Override
	public Object getBean() {
		return this.bean;
	}




	@Override
	public String getName() {
		return this.name;
	}







	private static class Listener implements InvalidationListener {


		private final WeakReference<FlagColorProperty> wref;




		public Listener(FlagColorProperty ref) {
			this.wref = new WeakReference<>(ref);
		}




		@Override
		public void invalidated(Observable observable) {
			FlagColorProperty ref = wref.get();
			if (ref == null) {
				observable.removeListener(this);
			} else {
				ref.markInvalid();
			}
		}

	}


}
