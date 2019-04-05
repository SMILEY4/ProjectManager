package com.ruegnerlukas.taskmanager.utils.observables;


import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;

import java.lang.ref.WeakReference;

public class AttributeTypeProperty implements Property<TaskAttribute.Type>, WritableObjectValue<TaskAttribute.Type> {


	private static final Object DEFAULT_BEAN = null;
	private static final String DEFAULT_NAME = "";

	private final Object bean;
	private final String name;

	private TaskAttribute.Type value;

	private ObservableValue<? extends TaskAttribute.Type> observable = null;
	private InvalidationListener listener = null;
	private boolean valid = true;
	private ExpressionHelper<TaskAttribute.Type> helper = null;




	public AttributeTypeProperty() {
		this(DEFAULT_BEAN, DEFAULT_NAME, null);
	}




	public AttributeTypeProperty(TaskAttribute.Type initialValue) {
		this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
	}




	public AttributeTypeProperty(Object bean, String name) {
		this(bean, name, null);
	}




	public AttributeTypeProperty(Object bean, String name, TaskAttribute.Type initialValue) {
		this.bean = bean;
		this.name = name;
		this.value = initialValue;
	}



	@Override
	public TaskAttribute.Type get() {
		valid = true;
		return observable == null ? value : observable.getValue();
	}




	@Override
	public void set(TaskAttribute.Type newValue) {
		if (isBound()) {
			throw new java.lang.RuntimeException((getBean() != null && getName() != null ? getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
		}
		if ((value == null) ? newValue != null : !value.equals(newValue)) {
			value = newValue;
			markInvalid();
		}
	}



	@Override
	public TaskAttribute.Type getValue() {
		return get();
	}




	@Override
	public void setValue(TaskAttribute.Type newValue) {
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
	public void addListener(ChangeListener<? super TaskAttribute.Type> listener) {
		helper = ExpressionHelper.addListener(helper, this, listener);
	}




	@Override
	public void removeListener(ChangeListener<? super TaskAttribute.Type> listener) {
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
	public void bindBidirectional(Property<TaskAttribute.Type> other) {
		Bindings.bindBidirectional(this, other);
	}




	@Override
	public void unbindBidirectional(Property<TaskAttribute.Type> other) {
		Bindings.unbindBidirectional(this, other);
	}




	@Override
	public void bind(ObservableValue<? extends TaskAttribute.Type> newObservable) {
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


		private final WeakReference<AttributeTypeProperty> wref;




		public Listener(AttributeTypeProperty ref) {
			this.wref = new WeakReference<>(ref);
		}




		@Override
		public void invalidated(Observable observable) {
			AttributeTypeProperty ref = wref.get();
			if (ref == null) {
				observable.removeListener(this);
			} else {
				ref.markInvalid();
			}
		}

	}


}
