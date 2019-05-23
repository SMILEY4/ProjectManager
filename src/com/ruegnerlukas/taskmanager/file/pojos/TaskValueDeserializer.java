package com.ruegnerlukas.taskmanager.file.pojos;

import com.google.gson.*;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.file.FileHandler;

import java.lang.reflect.Type;

public class TaskValueDeserializer implements JsonDeserializer<TaskValue<?>> {


	@Override
	public TaskValue<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		JsonObject jsonObject = json.getAsJsonObject();
		if(!jsonObject.has("type")) {
			return new TextValue("ERROR: " + json.toString());
		}

		String type = jsonObject.getAsJsonPrimitive("type").getAsString();

		Gson gson = FileHandler.buildGson();

		switch (type) {
			case "ID": {
				return gson.fromJson(jsonObject, IDValue.class);
			}
			case "DESCRIPTION": {
				return gson.fromJson(jsonObject, DescriptionValue.class);
			}
			case "CREATED": {
				return gson.fromJson(jsonObject, CreatedValue.class);
			}
			case "LAST_UPDATED": {
				return gson.fromJson(jsonObject, LastUpdatedValue.class);
			}
			case "FLAG": {
				return gson.fromJson(jsonObject, FlagValue.class);
			}
			case "TEXT": {
				return gson.fromJson(jsonObject, TextValue.class);
			}
			case "NUMBER": {
				return gson.fromJson(jsonObject, NumberValue.class);
			}
			case "BOOLEAN": {
				return gson.fromJson(jsonObject, BoolValue.class);
			}
			case "CHOICE": {
				return gson.fromJson(jsonObject, ChoiceValue.class);
			}
			case "DATE": {
				return gson.fromJson(jsonObject, DateValue.class);
			}
			case "DEPENDENCY": {
				return gson.fromJson(jsonObject, DependencyValue.class);
			}
			default: {
				return new TextValue("ERROR: " + json.toString());
			}
		}
	}

}
