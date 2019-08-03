package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.popupconfirmchange;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.logic.utils.SetAttributeValueEffect;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupConfirmChanges extends PopupBase {


	@FXML private HBox boxHeader;

	@FXML private VBox boxContent;
	@FXML private VBox boxList;

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

		// sort effects into buckets
		Map<AttributeValueType, List<SetAttributeValueEffect>> effectsMap = new HashMap<>();
		for (SetAttributeValueEffect effect : effects) {
			List<SetAttributeValueEffect> list = effectsMap.computeIfAbsent(effect.prevAttValue.getType(), k -> new ArrayList<>());
			list.add(effect);
		}

		// add items
		for (AttributeValueType type : effectsMap.keySet()) {
			SetAttributeValueEffect sampleEffect = effectsMap.get(type).get(0);
			boxList.getChildren().add(new ConfirmChangeHeader(type, sampleEffect.prevAttValue, sampleEffect.nextAttValue));
			for (SetAttributeValueEffect effect : effectsMap.get(type)) {
				boxList.getChildren().add(new ConfirmChangeItem(effect));
			}
		}


		// button cancel
		btnCancel.setOnAction(event -> {
			onCancel();
		});


		// button accept
		btnAccept.setOnAction(event -> {
			onAccept();
		});

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
