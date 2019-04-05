package com.ruegnerlukas.taskmanager.data;


import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;

public class Data {


	private static final Data data = new Data();




	public static Data get() {
		return data;
	}




	private Project project = null;
	private List<ChangeListener<? super Project>> projectListeners = new ArrayList<>();




	public void setProject(Project project) {
		if (this.project == project) {
			return;
		}
		final Project oldProject = this.project;
		this.project = project;
		for (ChangeListener<? super Project> listener : projectListeners) {
			listener.changed(null, oldProject, this.project);
		}
	}




	public Project getProject() {
		return this.project;
	}




	public void addProjectListener(ChangeListener<? super Project> listener) {
		projectListeners.add(listener);
	}




	public void removeProjectListener(ChangeListener<? super Project> listener) {
		projectListeners.remove(listener);
	}


}


