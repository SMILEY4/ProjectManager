package com.ruegnerlukas.tests;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings ("Duplicates")
public class Test_SyncedProperty {


	@Test
	void simpleLocalChange() {

		List<DataChange> changes = new ArrayList<>();
		DataHandler handler = new DataHandler(null) {
			@Override
			public void onLocalChange(DataChange change) {
				changes.add(change);
			}
		};

		SyncedProperty<String> property = new SyncedProperty<>("test.property", null, handler);
		property.set("new value 1");
		property.set(null);
		property.set("new value 2");
		property.dispose();
		property.set("new value 3");

		Assertions.assertEquals(3, changes.size(), "Wrong amount of changes.");
		if (changes.size() == 3) {
			String status1 = TestUtils.checkValueChange(changes.get(0), "test.property", "new value 1");
			if (!status1.equals("OK")) {
				Assertions.fail("Change 1: " + status1);
			}
			String status2 = TestUtils.checkValueChange(changes.get(1), "test.property", null);
			if (!status2.equals("OK")) {
				Assertions.fail("Change 2: " + status2);
			}
			String status3 = TestUtils.checkValueChange(changes.get(2), "test.property", "new value 2");
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

		SyncedProperty<String> propertyParent = new SyncedProperty<>("parent", null, handler);
		SyncedProperty<String> property = new SyncedProperty<>("property", propertyParent.getNode(), handler);

		property.set("new value 1");
		property.dispose();
		property.set("new value 3");

		Assertions.assertEquals(1, changes.size(), "Wrong amount of changes.");
		if (changes.size() == 3) {

			DataChange changeNested = changes.get(0);
			String status1 = TestUtils.checkNestedChange(changeNested, "parent");
			if (!status1.equals("OK")) {
				Assertions.fail("Nested: " + status1);

			} else {
				DataChange changeValue = ((NestedChange) changeNested).getNext();
				String status2 = TestUtils.checkValueChange(changeValue, "property", "new value 1");
				if (!status2.equals("OK")) {
					Assertions.fail("Value: " + status2);
				}
			}

		}

	}




	@Test
	void simpleExternalChange() {

		DataHandler handler = new DataHandler(null);

		SyncedProperty<String> property = new SyncedProperty<>("test.property", null, handler);

		property.set("original value");

		handler.onExternalChange(DataChange.createValueChange("test.property", "new value"));
		Assertions.assertEquals("new value", property.get());
	}


}