package com.ruegnerlukas.tests;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedList;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings ("Duplicates")
public class Test_SyncedList {


	@Test
	void simpleLocalChangePrimitiveElements() {

		List<DataChange> changes = new ArrayList<>();
		DataHandler handler = new DataHandler(null) {
			@Override
			public void onLocalChange(DataChange change) {
				changes.add(change);
			}
		};


		SyncedList<String> list = new SyncedList<>("test.list", null, handler);

		list.add("element 1");

		list.add("element 2");

		list.remove("element 1");

		list.remove("element 1");

		list.remove("element 3");


		Assertions.assertEquals(3, changes.size(), "Wrong amount of changes.");
		if (changes.size() == 3) {
			String status1 = TestUtils.checkListChange(changes.get(0), true, "element 1", null);
			if (!status1.equals("OK")) {
				Assertions.fail("Change 1: " + status1);
			}
			String status2 = TestUtils.checkListChange(changes.get(1), true, "element 2", null);
			if (!status2.equals("OK")) {
				Assertions.fail("Change 2: " + status2);
			}
			String status3 = TestUtils.checkListChange(changes.get(2), false, "element 1", null);
			if (!status3.equals("OK")) {
				Assertions.fail("Change 3: " + status3);
			}
		}

	}




	@Test
	void simpleLocalChangeSyncedElements() {

		List<DataChange> changes = new ArrayList<>();
		DataHandler handler = new DataHandler(null) {
			@Override
			public void onLocalChange(DataChange change) {
				changes.add(change);
			}
		};

		SyncedList<SyncedProperty<String>> list = new SyncedList<>("list", null, handler);
		SyncedProperty<String> property1 = new SyncedProperty<>("property1", list.getNode(), handler);
		SyncedProperty<String> property2 = new SyncedProperty<>("property2", list.getNode(), handler);
		SyncedProperty<String> property3 = new SyncedProperty<>("property3", list.getNode(), handler);


		list.add(property1);

		list.add(property2);

		list.remove(property1);

		list.remove(property1);

		list.remove(property3);


		Assertions.assertEquals(3, changes.size(), "Wrong amount of changes.");
		if (changes.size() == 3) {
			String status1 = TestUtils.checkListChange(changes.get(0), true, property1, null);
			if (!status1.equals("OK")) {
				Assertions.fail("Change 1: " + status1);
			}
			String status2 = TestUtils.checkListChange(changes.get(1), true, property2, null);
			if (!status2.equals("OK")) {
				Assertions.fail("Change 2: " + status2);
			}
			String status3 = TestUtils.checkListChange(changes.get(2), false, null, "property1");
			if (!status3.equals("OK")) {
				Assertions.fail("Change 3: " + status3);
			}
		}

	}




	@Test
	void nestedLocalChange() {

		List<DataChange> changes = new ArrayList<>();
		DataHandler handler = new DataHandler(null) {
			@Override
			public void onLocalChange(DataChange change) {
				changes.add(change);
			}
		};

		SyncedList<SyncedProperty<String>> list = new SyncedList<>("list", null, handler);
		SyncedProperty<String> property1 = new SyncedProperty<>("property1", list.getNode(), handler);
		SyncedProperty<String> property2 = new SyncedProperty<>("property2", list.getNode(), handler);

		property1.set("new value 1");
		property2.set("new value 2");


		Assertions.assertEquals(2, changes.size(), "Wrong amount of changes.");
		if (changes.size() == 2) {

			DataChange change1Nested = changes.get(0);
			String status1a = TestUtils.checkNestedChange(change1Nested, "list");
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
			String status2a = TestUtils.checkNestedChange(change2Nested, "list");
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

		DataHandler handler = new DataHandler(null);

		SyncedList<String> list = new SyncedList<>("test.list", null, handler);

		handler.onExternalChange(DataChange.createListChangeAdd("test.list", (Object) "element 1"));
		Assertions.assertEquals(1, list.size(), "Wrong list size after first add");
		Assertions.assertEquals(list.get(0), "element 1", "Wrong element @0 after first add");

		handler.onExternalChange(DataChange.createListChangeAdd("test.list", (Object) "element 2"));
		Assertions.assertEquals(2, list.size(), "Wrong list size after second add");
		Assertions.assertEquals(list.get(0), "element 1", "Wrong element @0 after second add");
		Assertions.assertEquals(list.get(1), "element 2", "Wrong element @1 after second add");

		handler.onExternalChange(DataChange.createListChangeRemove("test.list", (Object) "element 1"));
		Assertions.assertEquals(1, list.size(), "Wrong list size after remove");
		Assertions.assertEquals(list.get(0), "element 2", "Wrong element @0 after remove");

	}




	@Test
	void simpleExternalChangeSyncedElements() {

		DataHandler handler = new DataHandler(null);

		SyncedList<SyncedProperty<String>> list = new SyncedList<>("test.list", null, handler);
		SyncedProperty<String> property1 = new SyncedProperty<>("property1", list.getNode(), handler);
		SyncedProperty<String> property2 = new SyncedProperty<>("property2", list.getNode(), handler);

		handler.onExternalChange(DataChange.createListChangeAdd("test.list", property1));
		Assertions.assertEquals(1, list.size(), "Wrong list size after first add");
		Assertions.assertEquals(list.get(0), property1, "Wrong element @0 after first add");

		handler.onExternalChange(DataChange.createListChangeAdd("test.list", property2));
		Assertions.assertEquals(2, list.size(), "Wrong list size after second add");
		Assertions.assertEquals(list.get(0), property1, "Wrong element @0 after second add");
		Assertions.assertEquals(list.get(1), property2, "Wrong element @1 after second add");

		handler.onExternalChange(DataChange.createListChangeRemove("test.list", "property1"));
		Assertions.assertEquals(1, list.size(), "Wrong list size after remove");
		Assertions.assertEquals(list.get(0), property2, "Wrong element @0 after remove");
	}




	@Test
	void nestedExternalChange() {

		DataHandler handler = new DataHandler(null);

		SyncedList<SyncedProperty<String>> list = new SyncedList<>("test.list", null, handler);
		SyncedProperty<String> property1 = new SyncedProperty<>("property1", list.getNode(), handler);
		SyncedProperty<String> property2 = new SyncedProperty<>("property2", list.getNode(), handler);
		property1.setValue("original value 1");
		property2.setValue("original value 2");
		list.add(property1);
		list.add(property2);

		handler.onExternalChange(DataChange.createValueChange("test.list/property1", "new value 1"));
		handler.onExternalChange(DataChange.createValueChange("test.list/property2", "new value 2"));

		Assertions.assertEquals("new value 1", property1.get());
		Assertions.assertEquals("new value 2", property2.get());

	}

}
