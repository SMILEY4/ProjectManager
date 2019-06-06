package com.ruegnerlukas.taskmanager.file.pojos;

import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;

import java.util.ArrayList;
import java.util.List;

public class POJOProject {


	public static POJOProject fromProject(Project project) {

		POJOProject pojo = new POJOProject();

		// settings
		POJOSettings pojoSettings = new POJOSettings();
		pojo.settings = pojoSettings;
		pojoSettings.name = project.settings.name.get();
		pojoSettings.attribsLocked = project.settings.attributesLocked.get();
		pojoSettings.idCounter = project.settings.idCounter.get();

		// data
		POJOData pojoData = new POJOData();
		pojo.data = pojoData;
		pojoData.attributes = new ArrayList<>();
		pojoData.tasks = new ArrayList<>();

		// data.attributes
		for(TaskAttribute attribute : project.data.attributes) {
			pojoData.attributes.add(POJOAttribute.fromAttribute(attribute));
		}

		// data.tasks
		for(Task task : project.data.tasks) {
			pojoData.tasks.add(POJOTask.fromTask(task));
		}


		return pojo;
	}




	public static Project toProject(POJOProject pojo) {

		Project project = new Project();

		// settings
		project.settings.name.set(pojo.settings.name);
		project.settings.attributesLocked.set(pojo.settings.attribsLocked);
		project.settings.idCounter.set(pojo.settings.idCounter);

		// data.attributes
		for(POJOAttribute pojoAttribute : pojo.data.attributes) {
			project.data.attributes.add(POJOAttribute.toAttribute(pojoAttribute));
		}

		// data.tasks
		for(POJOTask pojoTask : pojo.data.tasks) {
			project.data.tasks.add(POJOTask.toTask(pojoTask, project.data.attributes));
		}

		return project;
	}




	public POJOSettings settings;
	public POJOData data;

}






class POJOSettings {


	public String name;
	public boolean attribsLocked;
	public int idCounter;

}






class POJOData {

	public List<POJOAttribute> attributes;
	public List<POJOTask> tasks;

}


