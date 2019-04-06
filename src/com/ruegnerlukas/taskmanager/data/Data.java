package com.ruegnerlukas.taskmanager.data;


import com.ruegnerlukas.taskmanager.utils.observables.ProjectProperty;

public class Data {


	private static final Data data = new Data();




	public static Data get() {
		return data;
	}




	private ProjectProperty projectProperty = new ProjectProperty();




	public void setProject(Project project) {
		if (this.projectProperty.get() != project) {
			projectProperty.set(project);
		}

	}




	public ProjectProperty projectProperty() {
		return this.projectProperty;
	}




	public Project getProject() {
		return this.projectProperty.get();
	}


}


