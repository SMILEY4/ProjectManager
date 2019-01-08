package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import java.io.IOException;
import java.util.ArrayList;

import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Type;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomAttribute.Variable;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomBoolAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomChoiceAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomDateAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomNumberAttributeData;
import com.ruegnerlukas.taskmanager.logic.data.taskattribs.CustomTextAttributeData;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeChangedNameEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeChangedTypeEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.CustomAttributeChangedVariableEvent;
import com.ruegnerlukas.taskmanager.logic.services.DataService;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class CustomAttributeNode extends AnchorPane {

	@FXML private Button btnDelete;
	@FXML private TextField attribName;
	@FXML private ChoiceBox<String> attribType;
	
	@FXML private HBox boxText;
	@FXML private Spinner<Integer> textCharLimit;
	@FXML private CheckBox textUseDefault;
	@FXML private TextField textDefault;

	@FXML private HBox boxNumber;
	@FXML private Spinner<Integer> numberDecPlaces;
	@FXML private Spinner<Double> numberMin;
	@FXML private Spinner<Double> numberMax;
	@FXML private CheckBox numberUseDefault;
	@FXML private Spinner<Double> numberDefault;
	
	@FXML private HBox boxBoolean;
	@FXML private CheckBox boolUseDefault;
	@FXML private ChoiceBox<String> boolDefault;
	
	@FXML private HBox boxDate;
	@FXML private CheckBox dateUseDefault;
	@FXML private DatePicker dateDefault;
	
	@FXML private HBox boxChoice;
	@FXML private TextField choiceValues;
	@FXML private CheckBox choiceUseDefault;
	@FXML private ChoiceBox<String> choiceDefault;

	public final CustomAttribute attribute;
	
	
	
	public CustomAttributeNode(CustomAttribute attribute) {
		this.attribute = attribute;
		try {
			loadFromFXML();
			createCustomElements();
			setupListeners();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private void loadFromFXML() throws IOException {
		final String PATH = "taskAttribute.fxml";
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
		loader.setController(this);
		HBox  root = (HBox) loader.load();
		root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
		root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
		root.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.R) {
					root.getStylesheets().clear();
					root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
					root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
				}
			}
		});

		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);
	}
	
	
	
	
	private void createCustomElements() {
	
		// btn delete
		ButtonUtils.makeIconButton(btnDelete, SVGIcons.getCross(), 40, 40, "black");
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.deleteCustomAttribute(attribute);
			}			
		});
		
		// attrib name
		attribName.setText(attribute.name);
		attribName.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.renameCustomAttribute(attribute, attribName.getText());
			}
		});
		attribName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue == false) {
					DataService.customAttributes.renameCustomAttribute(attribute, attribName.getText());
				}
			}
		});
	
		// attrib type
		for(Type type : Type.values()) {
			attribType.getItems().add(type.toString());
		}
		attribType.getSelectionModel().select(attribute.data.getType().toString());
		attribType.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				for(Type type : Type.values()) {
					if(type.toString().equals(attribType.getValue())) {
						DataService.customAttributes.setCustomAttributeType(attribute, type);
					}
				}
			}
		});
		
		
		
		// TEXT
		CustomTextAttributeData defaultTextAttrib = new CustomTextAttributeData();
		
		SpinnerUtils.initSpinner(textCharLimit, defaultTextAttrib.charLimit, -9999, 9999, 1, 0, true, new ChangeListener<Integer>() {
			@Override public void changed(ObservableValue<? extends Integer> observablse, Integer oldValue, Integer newValue) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.TEXT_CHAR_LIMIT, newValue);
			}
		});
		textCharLimit.setEditable(true);
		
		textUseDefault.setSelected(defaultTextAttrib.useDefault);
		textUseDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.TEXT_USE_DEFAULT, textUseDefault.isSelected());
			}
		});
		
		textDefault.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.TEXT_DEFAULT, textDefault.getText());
			}
		});
		textDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.TEXT_DEFAULT, textDefault.getText());
			}
		});
		textDefault.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue == false) {
					DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.TEXT_DEFAULT, textDefault.getText());
				}
			}
		});
		textDefault.setDisable(!defaultTextAttrib.useDefault);
		
		
		// NUMBER
		CustomNumberAttributeData defaultNumberAttrib = new CustomNumberAttributeData();
		
		SpinnerUtils.initSpinner(numberDecPlaces, defaultNumberAttrib.decPlaces, 0, 10, 1, 0, true, new ChangeListener<Integer>() {
			@Override public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_DEC_PLACES, newValue.intValue());
			}
		});
		numberDecPlaces.setEditable(true);

		
		SpinnerUtils.initSpinner(numberMin, defaultNumberAttrib.min, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, 1, true, new ChangeListener<Double>() {
			@Override public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_MIN, newValue.doubleValue());
			}
		});
		numberMin.setEditable(true);

		
		SpinnerUtils.initSpinner(numberMax, defaultNumberAttrib.max, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, 1, true, new ChangeListener<Double>() {
			@Override public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_MAX, newValue.doubleValue());
			}
		});
		numberMax.setEditable(true);

		
		numberUseDefault.setSelected(defaultNumberAttrib.useDefault);
		numberUseDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_USE_DEFAULT, numberUseDefault.isSelected());
			}
		});
		
		SpinnerUtils.initSpinner(numberDefault, defaultNumberAttrib.defaultValue, -9990, +9999, 1, 1, true, new ChangeListener<Double>() {
			@Override public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_DEFAULT, newValue.doubleValue());
			}
		});
		numberDefault.setEditable(true);
		numberDefault.setDisable(!numberUseDefault.isSelected());
		
		
		// BOOLEAN
		CustomBoolAttributeData defaultBoolAttrib = new CustomBoolAttributeData();
		
		boolUseDefault.setSelected(defaultBoolAttrib.useDefault);
		boolUseDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.BOOLEAN_USE_DEFAULT, boolUseDefault.isSelected());
			}
		});
		
		boolDefault.getItems().addAll("True", "False");
		boolDefault.getSelectionModel().select(defaultBoolAttrib.defaultValue ? "True" : "False");
		boolDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				boolean value = boolDefault.getSelectionModel().getSelectedItem().equals("True");
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.BOOLEAN_DEFAULT, value);
			}
		});
		boolDefault.setDisable(!boolUseDefault.isSelected());
		
		
		// DATE
		CustomDateAttributeData defaultDateAttrib = new CustomDateAttributeData();
		
		dateUseDefault.setSelected(defaultDateAttrib.useDefault);
		dateUseDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.DATE_USE_DEFAULT, dateUseDefault.isSelected());
			}
		});
		
		dateDefault.setValue(defaultDateAttrib.defaultValue);
		dateDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.DATE_DEFAULT, dateDefault.getValue());
			}
		});
		dateDefault.setDisable(!dateUseDefault.isSelected());

		
		// CHOICE
		CustomChoiceAttributeData defaultChoiceAttrib = new CustomChoiceAttributeData();

		choiceValues.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				ArrayList<String> values = new ArrayList<String>();
				for(String str : choiceValues.getText().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
					values.add(str.trim());
				}
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.CHOICE_VALUES, values);
			}
		});
		choiceValues.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				ArrayList<String> values = new ArrayList<String>();
				for(String str : choiceValues.getText().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
					values.add(str.trim());
				}
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.CHOICE_VALUES, values);
			}
		});
		choiceValues.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue == false) {
					ArrayList<String> values = new ArrayList<String>();
					for(String str : choiceValues.getText().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
						values.add(str.trim());
					}
					DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.CHOICE_VALUES, values);
				}
			}
		});
		StringBuilder builderChoiceValues = new StringBuilder();
		for(String value : defaultChoiceAttrib.values) {
			builderChoiceValues.append(value);
			if(defaultChoiceAttrib.values.indexOf(value) != defaultChoiceAttrib.values.size()-1) {
				builderChoiceValues.append(", ");
			}
		}
		choiceValues.setText(builderChoiceValues.toString());
		
		
		choiceUseDefault.setSelected(defaultChoiceAttrib.useDefault);
		choiceUseDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.CHOICE_USE_DEFAULT, choiceUseDefault.isSelected());
			}
		});
		
		choiceDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.CHOICE_DEFAULT, choiceDefault.getSelectionModel().getSelectedItem());
			}
		});
		choiceDefault.setDisable(!choiceUseDefault.isSelected());
		
		updateType();
	}

	
	
	
	private void updateType() {
		
		boxText.setVisible(false);
		boxNumber.setVisible(false);
		boxBoolean.setVisible(false);
		boxDate.setVisible(false);
		boxChoice.setVisible(false);
		
		boxText.setDisable(true);
		boxNumber.setDisable(true);
		boxBoolean.setDisable(true);
		boxDate.setDisable(true);
		boxChoice.setDisable(true);
		
		if(attribute.data.getType() == Type.TEXT) {
			CustomTextAttributeData textAttrib = (CustomTextAttributeData)attribute.data;
			
			boxText.setVisible(true);
			boxText.setDisable(false);
			
			SpinnerUtils.initSpinner(textCharLimit, textAttrib.charLimit, -9999, 9999, 1, 0, true, null);
			
			textUseDefault.setSelected(textAttrib.useDefault);
			textDefault.setDisable(!textAttrib.useDefault);
			
			textDefault.setText(textAttrib.defaultValue);
		}
		
		if(attribute.data.getType() == Type.NUMBER) {
			CustomNumberAttributeData numberAttrib = (CustomNumberAttributeData)attribute.data;
			
			boxNumber.setVisible(true);
			boxNumber.setDisable(false);
			
			SpinnerUtils.initSpinner(numberDecPlaces, numberAttrib.decPlaces, 0, 9999, 1, 0, true, null);
			SpinnerUtils.initSpinner(numberMin, numberAttrib.min, Integer.MIN_VALUE, Integer.MAX_VALUE, 1.0/Math.pow(10, numberAttrib.decPlaces), Math.max(1, numberAttrib.decPlaces), true, null);
			SpinnerUtils.initSpinner(numberMax, numberAttrib.max, Integer.MIN_VALUE, Integer.MAX_VALUE, 1.0/Math.pow(10, numberAttrib.decPlaces), Math.max(1, numberAttrib.decPlaces), true, null);

			
			numberUseDefault.setSelected(numberAttrib.useDefault);
			numberDefault.setDisable(!numberAttrib.useDefault);
			
			SpinnerUtils.initSpinner(numberDefault, numberAttrib.defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, 1.0/Math.pow(10, numberAttrib.decPlaces), Math.max(1, numberAttrib.decPlaces), true, null);
		}
		
		if(attribute.data.getType() == Type.BOOLEAN) {
			CustomBoolAttributeData boolAttrib = (CustomBoolAttributeData)attribute.data;
			
			boxBoolean.setVisible(true);
			boxBoolean.setDisable(false);
			
			boolUseDefault.setSelected(boolAttrib.useDefault);
			boolDefault.setDisable(!boolAttrib.useDefault);

			boolDefault.getSelectionModel().select(boolAttrib.defaultValue ? "True" : "False");
		}
		
		if(attribute.data.getType() == Type.DATE) {
			CustomDateAttributeData dateAttrib = (CustomDateAttributeData)attribute.data;
			
			boxDate.setVisible(true);
			boxDate.setDisable(false);
			
			dateUseDefault.setSelected(dateAttrib.useDefault);
			dateDefault.setDisable(!dateAttrib.useDefault);
			
			dateDefault.setValue(dateAttrib.defaultValue);
		}
		
		if(attribute.data.getType() == Type.CHOICE) {
			CustomChoiceAttributeData choiceAttrib = (CustomChoiceAttributeData)attribute.data;
			
			boxChoice.setVisible(true);
			boxChoice.setDisable(false);
			
			StringBuilder builderChoiceValues = new StringBuilder();
			for(String value : choiceAttrib.values) {
				builderChoiceValues.append(value);
				if(choiceAttrib.values.indexOf(value) != choiceAttrib.values.size()-1) {
					builderChoiceValues.append(", ");
				}
			}
			choiceValues.setText(builderChoiceValues.toString());
			
			choiceUseDefault.setSelected(choiceAttrib.useDefault);
			choiceDefault.setDisable(!choiceAttrib.useDefault);

			choiceDefault.getItems().setAll(choiceAttrib.values);
			choiceDefault.getSelectionModel().select(choiceAttrib.defaultValue);
		}
		
	}
	
	
	
	
	private void setupListeners() {
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				CustomAttributeChangedNameEvent event = (CustomAttributeChangedNameEvent)e;
				if(event.getAttribute() == attribute) {
					attribName.setText(event.getNewName());
				}
			}
		}, CustomAttributeChangedNameEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				CustomAttributeChangedTypeEvent event = (CustomAttributeChangedTypeEvent)e;
				if(event.getAttribute() == attribute) {
					attribType.getSelectionModel().select(event.getNewType().toString());
					updateType();
				}
			}
		}, CustomAttributeChangedTypeEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				CustomAttributeChangedVariableEvent event = (CustomAttributeChangedVariableEvent)e;
				if(event.getAttribute() == attribute) {
					
					if(attribute.data.getType() == Type.TEXT) {
						CustomTextAttributeData data = (CustomTextAttributeData)attribute.data;
						if(event.getVariable() == Variable.TEXT_CHAR_LIMIT) {
							SpinnerUtils.initSpinner(textCharLimit, data.charLimit, -9999, 9999, 1, 0, true, null);
						}
						if(event.getVariable() == Variable.TEXT_USE_DEFAULT) {
							textUseDefault.setSelected(data.useDefault);
							textDefault.setDisable(!data.useDefault);
						}
						if(event.getVariable() == Variable.TEXT_DEFAULT) {
							textDefault.setText(data.defaultValue);
						}
					}
					
					if(attribute.data.getType() == Type.NUMBER) {
						CustomNumberAttributeData data = (CustomNumberAttributeData)attribute.data;
						if(event.getVariable() == Variable.NUMBER_DEC_PLACES) {
							SpinnerUtils.initSpinner(numberDecPlaces, data.decPlaces, 0, 9999, 1, 0, true, null);
							SpinnerUtils.initSpinner(numberMin, data.min, Integer.MIN_VALUE, data.max, 1.0/Math.pow(10, data.decPlaces), Math.max(1, data.decPlaces), true, null);
							SpinnerUtils.initSpinner(numberMax, data.max, data.min, Integer.MAX_VALUE, 1.0/Math.pow(10, data.decPlaces), Math.max(1, data.decPlaces), true, null);
							SpinnerUtils.initSpinner(numberDefault, data.defaultValue, data.min, data.max, 1.0/Math.pow(10, data.decPlaces), Math.max(1, data.decPlaces), true, null);
							if( (Integer)event.getOldValue() > (Integer)event.getNewValue() ) {
								int shift = (Integer)event.getNewValue();
								double newMin = (double)((int)(data.min * Math.pow(10, shift))) / Math.pow(10, shift);
								double newMax = (double)((int)(data.max * Math.pow(10, shift))) / Math.pow(10, shift);
								double newDefault = (double)((int)(data.defaultValue * Math.pow(10, shift))) / Math.pow(10, shift);
								DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_MIN, newMin);
								DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_MAX, newMax);
								DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_DEFAULT, newDefault);
							}
						}
						if(event.getVariable() == Variable.NUMBER_MIN) {
							SpinnerUtils.initSpinner(numberMin, data.min, Integer.MIN_VALUE, data.max, 1.0/Math.pow(10, data.decPlaces), Math.max(1, data.decPlaces), true, null);
							SpinnerUtils.initSpinner(numberMax, data.max, data.min, Integer.MAX_VALUE, 1.0/Math.pow(10, data.decPlaces), Math.max(1, data.decPlaces), true, null);
							if(data.defaultValue < data.min) {
								DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_DEFAULT, data.min);
							}
						}
						if(event.getVariable() == Variable.NUMBER_MAX) {
							SpinnerUtils.initSpinner(numberMax, data.max, data.min, Integer.MAX_VALUE, 1.0/Math.pow(10, data.decPlaces), Math.max(1, data.decPlaces), true, null);
							SpinnerUtils.initSpinner(numberMin, data.min, Integer.MIN_VALUE, data.max, 1.0/Math.pow(10, data.decPlaces), Math.max(1, data.decPlaces), true, null);
							if(data.defaultValue > data.max) {
								DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.NUMBER_DEFAULT, data.max);
							}
						}
						if(event.getVariable() == Variable.NUMBER_USE_DEFAULT) {
							numberUseDefault.setSelected(data.useDefault);
							numberDefault.setDisable(!data.useDefault);
						}
						if(event.getVariable() == Variable.NUMBER_DEFAULT) {
							SpinnerUtils.initSpinner(numberDefault, data.defaultValue, data.min, data.max, 1.0/Math.pow(10, data.decPlaces), Math.max(1, data.decPlaces), true, null);
						}
					}
					
					if(attribute.data.getType() == Type.BOOLEAN) {
						CustomBoolAttributeData data = (CustomBoolAttributeData)attribute.data;
						if(event.getVariable() == Variable.BOOLEAN_USE_DEFAULT) {
							boolUseDefault.setSelected(data.useDefault);
							boolDefault.setDisable(!data.useDefault);
						}
						if(event.getVariable() == Variable.BOOLEAN_DEFAULT) {
							boolDefault.getSelectionModel().select(data.defaultValue ? "True" : "False");
						}
					}
					
					if(attribute.data.getType() == Type.DATE) {
						CustomDateAttributeData data = (CustomDateAttributeData)attribute.data;
						if(event.getVariable() == Variable.DATE_USE_DEFAULT) {
							dateUseDefault.setSelected(data.useDefault);
							dateDefault.setDisable(!data.useDefault);
						}
						if(event.getVariable() == Variable.DATE_DEFAULT) {
							dateDefault.setValue(data.defaultValue);
						}
					}
					
					if(attribute.data.getType() == Type.CHOICE) {
						CustomChoiceAttributeData data = (CustomChoiceAttributeData)attribute.data;
						if(event.getVariable() == Variable.CHOICE_VALUES) {
							choiceDefault.getItems().setAll(data.values);
							choiceDefault.getSelectionModel().select(data.defaultValue);
							if(!data.values.contains(data.defaultValue)) {
								if(data.values.isEmpty()) {
									DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.CHOICE_DEFAULT, "");
									DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.CHOICE_USE_DEFAULT, false);
								} else {
									DataService.customAttributes.setCustomAttributeVariable(attribute, Variable.CHOICE_DEFAULT, data.values.get(0));
								}
							}
						}
						if(event.getVariable() == Variable.CHOICE_USE_DEFAULT) {
							choiceUseDefault.setSelected(data.useDefault);
							choiceDefault.setDisable(!data.useDefault);
						}
						if(event.getVariable() == Variable.CHOICE_DEFAULT) {
							choiceDefault.getSelectionModel().select(data.defaultValue);
						}
					}
					
				}
			}
		}, CustomAttributeChangedVariableEvent.class);
		
	}
	
	
}











