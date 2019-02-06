package com.ruegnerlukas.taskmanager.logic;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;

public class TaskFlagLogic {


	//======================//
	//       INTERNAL       //
	//======================//


	//======================//
	//        GETTER        //
	//======================//




	public void getFlag(String name, Request<TaskFlag> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			TaskAttribute flagAttribute = Logic.attribute.findAttribute(TaskAttributeType.FLAG);
			if (flagAttribute != null) {
				FlagAttributeData flagData = (FlagAttributeData) flagAttribute.data;
				TaskFlag flag = null;
				for (TaskFlag f : flagData.flags) {
					if (f.name.equals(name)) {
						flag = f;
						break;
					}
				}
				if (flag != null) {
					request.respond(new Response<>(Response.State.SUCCESS, flag));
				} else {
					request.respond(new Response<>(Response.State.FAIL, "No Flag with the name '" + name + "' found."));
				}

			} else {
				request.respond(new Response<>(Response.State.FAIL, "No FlagAttribute found."));
			}
		}
	}




	public void getAllFlags(Request<TaskFlag[]> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			TaskAttribute flagAttribute = Logic.attribute.findAttribute(TaskAttributeType.FLAG);
			if (flagAttribute != null) {
				FlagAttributeData flagData = (FlagAttributeData) flagAttribute.data;
				request.respond(new Response<>(Response.State.SUCCESS, flagData.flags));
			} else {
				request.respond(new Response<>(Response.State.FAIL, "No FlagAttribute found."));
			}
		}
	}




	public void getDefaultFlag(Request<TaskFlag> request) {
		Project project = Logic.project.getProject();
		if (project != null) {
			TaskAttribute flagAttribute = Logic.attribute.findAttribute(TaskAttributeType.FLAG);
			if (flagAttribute != null) {
				FlagAttributeData flagData = (FlagAttributeData) flagAttribute.data;
				request.respond(new Response<>(Response.State.SUCCESS, flagData.defaultFlag));
			} else {
				request.respond(new Response<>(Response.State.FAIL, "No FlagAttribute found."));
			}
		}
	}


	//======================//
	//        SETTER        //
	//======================//


}
