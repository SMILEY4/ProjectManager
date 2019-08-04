package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.popupconfirmchange;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterOperation;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.TerminalFilterCriteria;
import com.ruegnerlukas.taskmanager.logic.PresetLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.utils.SetAttributeValueEffect;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopupConfirmChanges extends PopupBase {


	@FXML private HBox boxHeader;

	@FXML private VBox boxContent;
	@FXML private VBox boxList;

	@FXML private TextField fieldFilterName;
	@FXML private Button btnSaveFilter;

	@FXML private Button btnCancel;
	@FXML private Button btnAccept;

	private List<SetAttributeValueEffect> effects;

	private boolean result = false;




	public PopupConfirmChanges(List<SetAttributeValueEffect> effects) {
		super(900, 500);
		this.effects = effects;
	}




	@Override
	public void create() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_CONFIRM_CHANGES, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading PopupConfirmChanges-FXML: " + e);
		}

		// add items
		for (SetAttributeValueEffect effect : effects) {
			boxList.getChildren().add(new ConfirmChangeItem(effect));
		}

		// input filter name
		SetAttributeValueEffect sampleEffect = effects.get(0);
		String presetName = "change " + sampleEffect.attribute.name.get() + " - " + Integer.toHexString(new Random().hashCode());
		fieldFilterName.setText(presetName);
		fieldFilterName.textProperty().addListener(((observable, oldValue, newValue) -> {
			final String name = fieldFilterName.getText().trim();
			boolean validName = true;
			if (name.isEmpty() || Data.projectProperty.get().data.presetsFilter.containsKey(name)) {
				validName = false;
			}
			btnSaveFilter.setDisable(!validName);
		}));

		// button save filter
		btnSaveFilter.setOnAction(event -> {
			onSaveFilter();
		});

		// button cancel
		btnCancel.setOnAction(event -> {
			onCancel();
		});


		// button accept
		btnAccept.setOnAction(event -> {
			onAccept();
		});

	}




	private void onSaveFilter() {
		final String strName = fieldFilterName.getText().trim();
		final boolean saved = PresetLogic.saveFilterPreset(Data.projectProperty.get(), strName, buildFilter(effects));
		if (saved) {
			fieldFilterName.setText(strName);
			fieldFilterName.setDisable(true);
			btnSaveFilter.setDisable(true);
		}
	}




	private FilterCriteria buildFilter(List<SetAttributeValueEffect> effects) {
		TaskAttribute idAttribute = AttributeLogic.findAttributeByType(Data.projectProperty.get(), AttributeType.ID);
		List<FilterCriteria> criteriaList = new ArrayList<>();
		for (SetAttributeValueEffect effect : effects) {
			TerminalFilterCriteria terminal = new TerminalFilterCriteria(idAttribute, FilterOperation.EQUALS, TaskLogic.getTaskID(effect.task));
			criteriaList.add(terminal);
		}
		return new OrFilterCriteria(criteriaList);
	}




	private void onCancel() {
		result = false;
		this.getStage().close();
	}




	private void onAccept() {
		result = true;
		this.getStage().close();
	}




	public boolean getResult() {
		return result;
	}

}
