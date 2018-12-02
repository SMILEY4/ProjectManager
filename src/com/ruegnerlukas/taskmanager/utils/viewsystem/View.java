package com.ruegnerlukas.taskmanager.utils.viewsystem;

public abstract class View {
	
	public final String id;
	protected IViewLoader loader;
	protected IViewService service;
	protected IViewController controller;
	
	public View(String id, IViewLoader loader, IViewService service) {
		this.id = id;
		this.loader = loader;
		this.service = service;
	}
	
}
