package com.ruegnerlukas.taskmanager.logic;

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




	public Response<TaskFlag> getFlag(String name) {
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
					return new Response<TaskFlag>().complete(flag);

				} else {
					return new Response<TaskFlag>().complete(null, Response.State.FAIL);
				}

			} else {
				return new Response<TaskFlag>().complete(null, Response.State.FAIL);
			}
		} else {
			return new Response<TaskFlag>().complete(null, Response.State.FAIL);
		}
	}




	public Response<TaskFlag[]> getAllFlags() {
		Project project = Logic.project.getProject();
		if (project != null) {
			TaskAttribute flagAttribute = Logic.attribute.findAttribute(TaskAttributeType.FLAG);
			if (flagAttribute != null) {
				FlagAttributeData flagData = (FlagAttributeData) flagAttribute.data;
				return new Response<TaskFlag[]>().complete(flagData.flags);
			} else {
				return new Response<TaskFlag[]>().complete(new TaskFlag[]{}, Response.State.FAIL);
			}
		} else {
			return new Response<TaskFlag[]>().complete(new TaskFlag[]{}, Response.State.FAIL);
		}
	}




	public Response<TaskFlag> getDefaultFlag() {
		Project project = Logic.project.getProject();
		if (project != null) {
			TaskAttribute flagAttribute = Logic.attribute.findAttribute(TaskAttributeType.FLAG);
			if (flagAttribute != null) {
				FlagAttributeData flagData = (FlagAttributeData) flagAttribute.data;
				return new Response<TaskFlag>().complete(flagData.defaultFlag);
			} else {
				return new Response<TaskFlag>().complete(null, Response.State.FAIL);
			}
		} else {
			return new Response<TaskFlag>().complete(null, Response.State.FAIL);
		}
	}


	//======================//
	//        SETTER        //
	//======================//


}
