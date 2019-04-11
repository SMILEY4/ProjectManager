package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.attributes.*;

public class Logic {


	private static final Logic logic = new Logic();




	public static Logic get() {
		return logic;
	}




	public void createNewProject() {
		Data.get().setProject(new Project());

		// add fixed attributes
		for (TaskAttribute.Type type : TaskAttribute.Type.getFixedTypes()) {
			Data.get().getProject().data.attributes.add(new TaskAttribute(type.display + " Attribute", type));
		}

	}




	public void closeCurrentProject() {
		Data.get().setProject(null);
	}




	public void saveProject(Project project) {
		System.out.println("TODO: save current project."); // TODO
	}




	public void renameProject(Project project, String newName) {
		if (project != null) {
			project.settings.name.set(newName);
		}
	}




	public void lockSwitchTaskAttributes() {
		Project project = Data.get().getProject();
		if (project != null) {
			project.settings.attributesLocked.set(!project.settings.attributesLocked.get());
		}
	}




	public TaskAttribute createTaskAttribute(TaskAttribute.Type type) {
		Project project = Data.get().getProject();
		if (project != null && type != null) {
			return createTaskAttribute(type, "Attribute " + Integer.toHexString(("Attribute" + System.currentTimeMillis()).hashCode()));
		} else {
			return null;
		}
	}




	public TaskAttribute createTaskAttribute(TaskAttribute.Type type, String name) {
		Project project = Data.get().getProject();
		if (project != null && type != null) {
			TaskAttribute attribute = new TaskAttribute(name, type);
			project.data.attributes.add(attribute);
			return attribute;
		} else {
			return null;
		}
	}




	public boolean deleteTaskAttribute(TaskAttribute attribute) {
		Project project = Data.get().getProject();
		if (project != null && attribute != null) {
			return project.data.attributes.remove(attribute);
		} else {
			return false;
		}
	}




	public String renameTaskAttribute(TaskAttribute attribute, String newName) {
		Project project = Data.get().getProject();
		if (project != null && attribute != null) {

			for (TaskAttribute attrib : project.data.attributes) {
				if (attrib == attribute) {
					continue;
				}
				if (attrib.name.get().equalsIgnoreCase(newName)) {
					return attribute.name.get();
				}
			}

			attribute.name.set(newName);
			return attribute.name.get();

		} else {
			return null;
		}
	}




	public boolean setTaskAttributeType(TaskAttribute attribute, TaskAttribute.Type newType) {
		return setTaskAttributeType(attribute, newType, false);
	}




	public boolean setTaskAttributeType(TaskAttribute attribute, TaskAttribute.Type newType, boolean force) {
		Project project = Data.get().getProject();
		if (project != null && attribute != null) {

			if (force) {
				attribute.type.set(newType);
				attribute.initValues();
				return true;
			} else {
				if (!attribute.type.get().fixed && !newType.fixed) {
					attribute.type.set(newType);
					attribute.initValues();
					return true;
				} else {
					return false;
				}
			}

		} else {
			return false;
		}
	}




	public boolean setTaskAttributeValue(TaskAttribute attribute, String key, Object newValue) {
		Project project = Data.get().getProject();
		if (project != null && attribute != null) {
			attribute.values.put(key, newValue);
			return true;
		} else {
			return false;
		}
	}

}
