package com.ruegnerlukas.taskmanager.file.serialization;

import com.google.gson.*;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.file.FileHandler;

import java.lang.reflect.Type;

public class AttributeValueDeserializer implements JsonDeserializer<AttributeValue<?>> {


	@Override
	public AttributeValue<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		JsonObject jsonObject = json.getAsJsonObject();

		if (!(jsonObject.has("type") && jsonObject.has("value"))) {
			Logger.get().warn("Could not deserialize AttributeValue: Invalid input: " + jsonObject.toString());
			return null;
		}

		String strType = jsonObject.getAsJsonPrimitive("type").getAsString();
		AttributeValueType type = AttributeValueType.valueOf(strType);

		Gson gson = FileHandler.buildGson();

		switch (type) {
			case USE_DEFAULT: {
				return gson.fromJson(jsonObject, UseDefaultValue.class);
			}
			case DEFAULT_VALUE: {
				return gson.fromJson(jsonObject, DefaultValue.class);
			}
			case CARD_DISPLAY_TYPE: {
				return gson.fromJson(jsonObject, CardDisplayTypeValue.class);
			}
			case NUMBER_DEC_PLACES: {
				return gson.fromJson(jsonObject, NumberDecPlacesValue.class);
			}
			case NUMBER_MIN: {
				return gson.fromJson(jsonObject, NumberMinValue.class);
			}
			case NUMBER_MAX: {
				return gson.fromJson(jsonObject, NumberMaxValue.class);
			}
			case CHOICE_VALUES: {
				return gson.fromJson(jsonObject, ChoiceListValue.class);
			}
			case FLAG_LIST: {
				return gson.fromJson(jsonObject, FlagListValue.class);
			}
			case TEXT_MULTILINE: {
				return gson.fromJson(jsonObject, TextMultilineValue.class);
			}
			default: {
				Logger.get().warn("Could not deserialize AttributeValue: Unknown type: " + type);
				return null;
			}
		}

	}

}
