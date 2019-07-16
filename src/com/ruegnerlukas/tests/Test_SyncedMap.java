package com.ruegnerlukas.tests;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedMap;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings ("Duplicates")
public class Test_SyncedMap {


	@Test
	void simpleLocalChangePrimitiveElements() {

		List<DataChange> changes = new ArrayList<>();
		DataHandler handler = new DataHandler() {
			@Override
			public void onLocalChange(DataChange change) {
				changes.add(change);
			}
		};


		SyncedMap<String, Integer> map = new SyncedMap<>("test.map", null, handler);

		map.put("element 1", 1);

		map.put("element 2", 2);

		map.remove("element 1");

		map.put("element 2", -2);

		map.remove("element 3");


//		Assertions.assertEquals(3, changes.size(), "Wrong amount of changes.");
//		if (changes.size() == 3) {
//			String status1 = TestUtils.checkListChange(changes.get(0), true, "element 1", null);
//			if (!status1.equals("OK")) {
//				Assertions.fail("Change 1: " + status1);
//			}
//			String status2 = TestUtils.checkListChange(changes.get(1), true, "element 2", null);
//			if (!status2.equals("OK")) {
//				Assertions.fail("Change 2: " + status2);
//			}
//			String status3 = TestUtils.checkListChange(changes.get(2), false, "element 1", null);
//			if (!status3.equals("OK")) {
//				Assertions.fail("Change 3: " + status3);
//			}
//		}

	}




	@Test
	void simpleLocalChangeSyncedElements() {

		List<DataChange> changes = new ArrayList<>();
		DataHandler handler = new DataHandler() {
			@Override
			public void onLocalChange(DataChange change) {
				changes.add(change);
			}
		};

		SyncedMap<String, SyncedProperty<String>> map = new SyncedMap<>("map", null, handler);
		SyncedProperty<String> property1 = new SyncedProperty<>("property1", map.getNode(), handler);
		SyncedProperty<String> property2 = new SyncedProperty<>("property2", map.getNode(), handler);
		SyncedProperty<String> property3 = new SyncedProperty<>("property3", map.getNode(), handler);


		map.put("element 1", property1);

		map.put("element 2", property2);

		map.remove("element 1");

		map.put("element 2", property3);

		map.remove("element 3");


//		Assertions.assertEquals(3, changes.size(), "Wrong amount of changes.");
//		if (changes.size() == 3) {
//			String status1 = TestUtils.checkListChange(changes.get(0), true, property1, null);
//			if (!status1.equals("OK")) {
//				Assertions.fail("Change 1: " + status1);
//			}
//			String status2 = TestUtils.checkListChange(changes.get(1), true, property2, null);
//			if (!status2.equals("OK")) {
//				Assertions.fail("Change 2: " + status2);
//			}
//			String status3 = TestUtils.checkListChange(changes.get(2), false, null, "property1");
//			if (!status3.equals("OK")) {
//				Assertions.fail("Change 3: " + status3);
//			}
//		}

	}




	@Test
	void nestedLocalChange() {

		List<DataChange> changes = new ArrayList<>();
		DataHandler handler = new DataHandler() {
			@Override
			public void onLocalChange(DataChange change) {
				changes.add(change);
			}
		};

		SyncedMap<String, SyncedProperty<String>> map = new SyncedMap<>("map", null, handler);
		SyncedProperty<String> property1 = new SyncedProperty<>("property1", map.getNode(), handler);
		SyncedProperty<String> property2 = new SyncedProperty<>("property2", map.getNode(), handler);

		property1.set("new value 1");
		property2.set("new value 2");


		Assertions.assertEquals(2, changes.size(), "Wrong amount of changes.");
		if (changes.size() == 2) {

			DataChange change1Nested = changes.get(0);
			String status1a = TestUtils.checkNestedChange(change1Nested, "map");
			if (!status1a.equals("OK")) {
				Assertions.fail("Nested: " + status1a);
			} else {
				DataChange change1Value = ((NestedChange) change1Nested).getNext();
				String status1b = TestUtils.checkValueChange(change1Value, "property1", "new value 1");
				if (!status1b.equals("OK")) {
					Assertions.fail("Value: " + status1b);
				}
			}

			DataChange change2Nested = changes.get(1);
			String status2a = TestUtils.checkNestedChange(change2Nested, "map");
			if (!status2a.equals("OK")) {
				Assertions.fail("Nested: " + status2a);
			} else {
				DataChange change2Value = ((NestedChange) change2Nested).getNext();
				String status2b = TestUtils.checkValueChange(change2Value, "property2", "new value 2");
				if (!status2b.equals("OK")) {
					Assertions.fail("Value: " + status2b);
				}
			}

		}

	}




	@Test
	void simpleExternalChangePrimitiveElements() {

		DataHandler handler = new DataHandler();

		SyncedMap<String, Integer> map = new SyncedMap<>("test.map", null, handler);

		handler.onExternalChange(DataChange.createMapChangeAdd("test.map", "key 1", 1));
		Assertions.assertEquals(1, map.size(), "Wrong map size after first add");
		Assertions.assertEquals(map.get("key 1"), 1, "Wrong element @\"key 1\" after first add");

		handler.onExternalChange(DataChange.createMapChangeAdd("test.map", "key 2", 2));
		Assertions.assertEquals(2, map.size(), "Wrong map size after second add");
		Assertions.assertEquals(map.get("key 1"), 1, "Wrong element @\"key 1\" after second add");
		Assertions.assertEquals(map.get("key 2"), 2, "Wrong element @\"key 2\" after second add");

		handler.onExternalChange(DataChange.createMapChangeRemove("test.map", "key 1"));
		Assertions.assertEquals(1, map.size(), "Wrong map size after remove");
		Assertions.assertNull(map.get("key 1"), "Wrong element @\"key 1\" after remove");

	}




	@Test
	void simpleExternalChangeSyncedElements() {

		DataHandler handler = new DataHandler();

		SyncedMap<String, SyncedProperty<String>> map = new SyncedMap<>("test.map", null, handler);
		SyncedProperty<String> property1 = new SyncedProperty<>("property1", map.getNode(), handler);
		SyncedProperty<String> property2 = new SyncedProperty<>("property2", map.getNode(), handler);

		handler.onExternalChange(DataChange.createMapChangeAdd("test.map", "prop 1", property1));
		Assertions.assertEquals(1, map.size(), "Wrong map size after first add");
		Assertions.assertEquals(map.get("prop 1"), property1, "Wrong element @\"prop 1\" after first add");

		handler.onExternalChange(DataChange.createMapChangeAdd("test.map", "prop 2", property2));
		Assertions.assertEquals(2, map.size(), "Wrong map size after second add");
		Assertions.assertEquals(map.get("prop 1"), property1, "Wrong element @\"prop 1\" after first add");
		Assertions.assertEquals(map.get("prop 2"), property2, "Wrong element @\"prop 2\" after first add");

		handler.onExternalChange(DataChange.createMapChangeRemove("test.map", "prop 1"));
		Assertions.assertEquals(1, map.size(), "Wrong map size after remove");
		Assertions.assertNull(map.get("prop 1"), "Wrong element @\"prop 1\" after remove");
	}




	@Test
	void nestedExternalChangeByIdentifier() {

		DataHandler handler = new DataHandler();

		SyncedMap<String, SyncedProperty<String>> map = new SyncedMap<>("test.map", null, handler);
		SyncedProperty<String> property1 = new SyncedProperty<>("property1", map.getNode(), handler);
		SyncedProperty<String> property2 = new SyncedProperty<>("property2", map.getNode(), handler);
		property1.setValue("original value 1");
		property2.setValue("original value 2");
		map.put("element 1", property1);
		map.put("element 2", property2);

		handler.onExternalChange(DataChange.createValueChange("test.map/property1", "new value 1"));
		handler.onExternalChange(DataChange.createValueChange("test.map/property2", "new value 2"));

		Assertions.assertEquals("new value 1", property1.get());
		Assertions.assertEquals("new value 2", property2.get());

	}

}
