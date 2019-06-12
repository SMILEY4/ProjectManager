package com.ruegnerlukas.taskmanager.file.plaindataobjects;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;

import java.util.ArrayList;
import java.util.List;

public class PDOProject {


	public static PDOProject fromProject(Project project) {

		PDOProject pdo = new PDOProject();

		// settings
		PDOSettings pojoSettings = new PDOSettings();
		pdo.settings = pojoSettings;
		pojoSettings.name = project.settings.name.get();
		pojoSettings.attribsLocked = project.settings.attributesLocked.get();
		pojoSettings.idCounter = project.settings.idCounter.get();

		// data
		PDOData pojoData = new PDOData();
		pdo.data = pojoData;
		pojoData.attributes = new ArrayList<>();
		pojoData.tasks = new ArrayList<>();

		// data.attributes
		for(TaskAttribute attribute : project.data.attributes) {
			pojoData.attributes.add(PDOAttribute.fromAttribute(attribute));
		}

		// data.tasks
		for(Task task : project.data.tasks) {
			pojoData.tasks.add(PDOTask.fromTask(task));
		}


		return pdo;
	}




	public static Project toProject(PDOProject pdo) {

		Project project = new Project();

		// settings
		project.settings.name.set(pdo.settings.name);
		project.settings.attributesLocked.set(pdo.settings.attribsLocked);
		project.settings.idCounter.set(pdo.settings.idCounter);

		// data.attributes
		for(PDOAttribute pojoAttribute : pdo.data.attributes) {
			project.data.attributes.add(PDOAttribute.toAttribute(pojoAttribute));
		}

		// data.tasks
		for(PDOTask pojoTask : pdo.data.tasks) {
			project.data.tasks.add(PDOTask.toTask(pojoTask, project.data.attributes));
		}

		return project;
	}




	public PDOSettings settings;
	public PDOData data;

}






class PDOSettings {


	public String name;
	public boolean attribsLocked;
	public int idCounter;

}






class PDOData {

	public List<PDOAttribute> attributes;
	public List<PDOTask> tasks;

}


