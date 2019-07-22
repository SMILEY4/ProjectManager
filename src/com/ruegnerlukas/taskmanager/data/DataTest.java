package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.utils.TestProjectFactory;

@SuppressWarnings ("Duplicates")
public class DataTest {


	public static void main(String[] args) {
		Project project = TestProjectFactory.build(
				"Test Project",
				new AttributeType[]{AttributeType.BOOLEAN, AttributeType.TEXT},
				new String[]{"Attribute Boolean", "Attribute Text"},
				5,
				true,
				100
		);
		System.out.println(project);
	}








//
//	static void testEvents() {
//
//		SyncedList<TaskAttribute> attributes = new SyncedList<>("project.data.attributes", null);
//
//		TaskAttribute attribute0 = new TaskAttribute("Test Attribute 0", AttributeType.BOOLEAN, attributes.getNode());
//		attribute0.values.put(AttributeValueType.CARD_DISPLAY_TYPE, new CardDisplayTypeValue(false));
//		attribute0.values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(new BoolValue(true)));
//		attributes.add(attribute0);
//
//		TaskAttribute attribute1 = new TaskAttribute("Test Attribute 1", AttributeType.NUMBER, attributes.getNode());
//		attribute1.values.put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(true));
//		attribute1.values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(new NumberValue(42)));
//		attributes.add(attribute1);
//
//
//		attribute1.values.remove(AttributeValueType.DEFAULT_VALUE);
//
//		attribute0.name.set("Another Name");
//
//		attributes.remove(attribute1);
//	}
//
//
//
//
//	static void testMaps() {
//
//		SyncedList<TaskAttribute> attributes = new SyncedList<>("project.data.attributes", null);
//		TaskAttribute attribute = new TaskAttribute("Test Attribute", AttributeType.BOOLEAN);
//		attribute.values.put(AttributeValueType.CARD_DISPLAY_TYPE, new CardDisplayTypeValue(false));
//		attribute.values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(new BoolValue(true)));
//		attributes.add(attribute);
//
//		System.out.println("values prev:");
//		for (Map.Entry<AttributeValueType, AttributeValue<?>> entry : attribute.values.entrySet()) {
//			System.out.println("  " + entry.getKey() + ": " + entry.getValue());
//		}
//
//
//		DataChange changeAdd = new NestedChange("project.data.attributes",
//				new NestedChange("Test Attribute",
//						new MapChange("values", true, AttributeValueType.USE_DEFAULT, new UseDefaultValue(true))));
//		DataHandler.onExternalChange(changeAdd);
//
//
//		DataChange changeRemove = new NestedChange("project.data.attributes",
//				new NestedChange("Test Attribute",
//						new MapChange("values", false, AttributeValueType.DEFAULT_VALUE, null)));
//		DataHandler.onExternalChange(changeRemove);
//
//
//		DataChange changeModify = new NestedChange("project.data.attributes",
//				new NestedChange("Test Attribute",
//						new MapChange("values", true, AttributeValueType.CARD_DISPLAY_TYPE, new CardDisplayTypeValue(true))));
//		DataHandler.onExternalChange(changeModify);
//
//
//		System.out.println();
//		System.out.println("values after:");
//		for (Map.Entry<AttributeValueType, AttributeValue<?>> entry : attribute.values.entrySet()) {
//			System.out.println("  " + entry.getKey() + ": " + entry.getValue());
//		}
//	}
//
//
//
//
//	static void testLists() {
//
//		SyncedList<TaskAttribute> attributes = new SyncedList<>("project.data.attributes", null);
//		TaskAttribute attribute0 = new TaskAttribute("Test Attribute", AttributeType.BOOLEAN);
//		TaskAttribute attribute1 = new TaskAttribute("Some Attribute", AttributeType.BOOLEAN);
//		attributes.add(attribute0);
//		attributes.add(attribute1);
//
//
//		System.out.println("List prev:");
//		for (TaskAttribute attribute : attributes) {
//			System.out.println("  " + attribute.name.get());
//		}
//
//
//		// REMOVE
//		DataHandler.onExternalChange(DataChange.createListChangeRemove("project.data.attributes", "Test Attribute"));
//
//
//		// ADD
//		DataHandler.onExternalChange(DataChange.createListChangeAdd("project.data.attributes", new TaskAttribute("New Attribute", AttributeType.NUMBER)));
//
//
//		System.out.println();
//		System.out.println("List after:");
//		for (TaskAttribute attribute : attributes) {
//			System.out.println("  " + attribute.name.get());
//		}
//
//	}
//
//
//
//
//	static void testNested() {
//
//		SyncedList<TaskAttribute> attributes = new SyncedList<>("project.data.attributes", null);
//		TaskAttribute attribute0 = new TaskAttribute("Test Attribute", AttributeType.BOOLEAN);
//		TaskAttribute attribute1 = new TaskAttribute("Some Attribute", AttributeType.BOOLEAN);
//		attributes.add(attribute0);
//		attributes.add(attribute1);
//
//
//		System.out.println("name prev 0 = " + attribute0.name.get());
//		System.out.println("type prev 0 = " + attribute0.type.get());
//		System.out.println("name prev 1 = " + attribute1.name.get());
//		System.out.println("type prev 1 = " + attribute1.type.get());
//
//
//		// CHANGE NAME
//		DataHandler.onExternalChange(
//				new NestedChange("project.data.attributes",
//						new NestedChange("Some Attribute",
//								new ValueChange("name", "Another Name")
//						)
//				)
//		);
//		DataHandler.onExternalChange(DataChange.createValueChange("project.data.attributes/Test Attribute/name", "New Name"));
//
//
//		// CHANGE TYPE
//		DataHandler.onExternalChange(DataChange.createValueChange("project.data.attributes/Another Name/type", AttributeType.TEXT));
//		DataHandler.onExternalChange(DataChange.createValueChange("project.data.attributes/New Name/type", AttributeType.NUMBER));
//
//
//		System.out.println();
//		System.out.println("name after 0 = " + attribute0.name.get());
//		System.out.println("type after 0 = " + attribute0.type.get());
//		System.out.println("name after 1 = " + attribute1.name.get());
//		System.out.println("type after 1 = " + attribute1.type.get());
//	}


}
