package com.ruegnerlukas.taskmanager.utils.observables;


import com.ruegnerlukas.taskmanager.data.Project;
import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;

import java.lang.ref.WeakReference;

public class ProjectProperty implements Property<Project>, WritableObjectValue<Project> {


	private static final Object DEFAULT_BEAN = null;
	private static final String DEFAULT_NAME = "";

	private final Object bean;
	private final String name;

	private Project value;

	private ObservableValue<? extends Project> observable = null;
	private InvalidationListener listener = null;
	private boolean valid = true;
	private ExpressionHelper<Project> helper = null;




	public ProjectProperty() {
		this(DEFAULT_BEAN, DEFAULT_NAME, null);
	}




	public ProjectProperty(Project initialValue) {
		this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
	}




	public ProjectProperty(Object bean, String name) {
		this(bean, name, null);
	}




	public ProjectProperty(Object bean, String name, Project initialValue) {
		this.bean = bean;
		this.name = name;
		this.value = initialValue;
	}



	@Override
	public Project get() {
		valid = true;
		return observable == null ? value : observable.getValue();
	}




	@Override
	public void set(Project newValue) {
		if (isBound()) {
			throw new RuntimeException((getBean() != null && getName() != null ? getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
		}
		if ((value == null) ? newValue != null : !value.equals(newValue)) {
			value = newValue;
			markInvalid();
		}
	}



	@Override
	public Project getValue() {
		return get();
	}




	@Override
	public void setValue(Project newValue) {
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
	public void addListener(ChangeListener<? super Project> listener) {
		helper = ExpressionHelper.addListener(helper, this, listener);
	}




	@Override
	public void removeListener(ChangeListener<? super Project> listener) {
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
	public void bindBidirectional(Property<Project> other) {
		Bindings.bindBidirectional(this, other);
	}




	@Override
	public void unbindBidirectional(Property<Project> other) {
		Bindings.unbindBidirectional(this, other);
	}




	@Override
	public void bind(ObservableValue<? extends Project> newObservable) {
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


		private final WeakReference<ProjectProperty> wref;




		public Listener(ProjectProperty ref) {
			this.wref = new WeakReference<>(ref);
		}




		@Override
		public void invalidated(Observable observable) {
			ProjectProperty ref = wref.get();
			if (ref == null) {
				observable.removeListener(this);
			} else {
				ref.markInvalid();
			}
		}

	}


}
