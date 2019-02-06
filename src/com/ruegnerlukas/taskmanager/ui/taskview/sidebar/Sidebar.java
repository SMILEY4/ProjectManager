package com.ruegnerlukas.taskmanager.ui.taskview.sidebar;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.DescriptionAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.IDAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item.SidebarItem;
import com.ruegnerlukas.taskmanager.ui.taskview.taskcard.TaskCard;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sidebar extends AnchorPane {


	public TaskCard currentCard = null;

	@FXML private VBox boxContent;
	@FXML private TextArea fieldDesc;
	@FXML private Label labelID;
	@FXML private ChoiceBox<String> choiceFlag;
	@FXML private VBox boxAttribs;

	private List<SidebarItem> items = new ArrayList<>();



	public Sidebar() {

		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_sidebar.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading Sidebar-FXML: " + e);
		}

		this.setPrefSize(10000, 200);

		create();

	}




	private void create() {

		// description
		fieldDesc.setText("");
//		// update every 10th character
//		fieldDesc.textProperty().addListener((observable, oldValue, newValue) -> {
//			if (currentCard != null) {
//				if (newValue.length() % 10 == 0) {
//					Logic.tasks.setAttributeValue(currentCard.task, DescriptionAttributeData.NAME, new TextValue(fieldDesc.getText()));
//				}
//			}
//		});
		fieldDesc.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (currentCard != null && !newValue) {
				Logic.tasks.setAttributeValue(currentCard.task, DescriptionAttributeData.NAME, new TextValue(fieldDesc.getText()));
			}
		});


		// id
		labelID.setText("-");


		// flag
		Logic.taskFlags.getAllFlags(new Request<TaskFlag[]>(true) {
			@Override
			public void onResponse(Response<TaskFlag[]> response) {
				for (TaskFlag flag : response.getValue()) {
					choiceFlag.getItems().add(flag.name);
				}
			}
		});
		choiceFlag.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Logic.taskFlags.getFlag(newValue, new Request<TaskFlag>(true) {
			@Override
			public void onResponse(Response<TaskFlag> response) {
				Logic.tasks.setAttributeValue(currentCard.task, FlagAttributeData.NAME, new FlagValue(response.getValue()));
			}
		}));

	}




	public void refresh() {
		if (currentCard == null) {
			return;
		}

		// description
		Logic.tasks.getAttributeValue(currentCard.task, DescriptionAttributeData.NAME, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				TextValue value = (TextValue)response.getValue();
				fieldDesc.setText(value.getText());
			}
		});

		// id
		Logic.tasks.getAttributeValue(currentCard.task, IDAttributeData.NAME, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				NumberValue value = (NumberValue)response.getValue();
				labelID.setText("T-" + value.getInt());
			}
		});

		// flag
		Logic.tasks.getAttributeValue(currentCard.task, FlagAttributeData.NAME, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				FlagValue value = (FlagValue)response.getValue();
				choiceFlag.getSelectionModel().select(value.getFlag().name);
			}
		});

		// task attributes
		for(SidebarItem item : items) {
			item.dispose();
		}
		boxAttribs.getChildren().clear();
		items.clear();
		Logic.attribute.getAttributes(new Request<List<TaskAttribute>>(true) {
			@Override
			public void onResponse(Response<List<TaskAttribute>> response) {
				List<TaskAttribute> attributes = response.getValue();
				for(int i=0; i<attributes.size(); i++) {
					TaskAttribute attribute = attributes.get(i);
					SidebarItem item = SidebarItem.create(currentCard.task, attribute);
					if(item != null) {
						items.add(item);
						boxAttribs.getChildren().add(item);
					}
				}
			}
		});
	}




	public void showTaskCard(TaskCard card) {
		if (card == null) {
			this.currentCard = null;
			this.setVisible(false);
		} else {
			this.currentCard = card;
			this.setVisible(true);
			refresh();
		}
	}


}
