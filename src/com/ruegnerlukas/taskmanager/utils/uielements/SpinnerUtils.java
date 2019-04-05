package com.ruegnerlukas.taskmanager.utils.uielements;

import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

public class SpinnerUtils {


	private static class StringConverterInt extends StringConverter<Integer> {


		private final Spinner<Integer> spinner;




		public StringConverterInt(Spinner<Integer> spinner) {
			this.spinner = spinner;
		}




		@Override
		public String toString(Integer value) {
			if (value == null) {
				return "";
			}
			return "" + value.intValue();
		}




		@Override
		public Integer fromString(String value) {
			boolean successful = true;
			int newValue = 0;
			try {
				if (value == null) {
					successful = false;
				} else {
					value = value.trim();
					if (value.length() < 1) {
						successful = false;
					} else {
						successful = true;
						newValue = Integer.parseInt(value);
					}
				}
			} catch (Exception ex) {
				successful = false;
			}

			if (successful) {
				return newValue;
			} else {
				newValue = spinner.getValue();
				spinner.getEditor().setText("" + newValue);
				commitEditorText(spinner);
				return newValue;
			}

		}

	}






	static class StringConverterDouble extends StringConverter<Double> {


		private final DecimalFormat format;
		private final Spinner<Double> spinner;




		public StringConverterDouble(int decPlaces, Spinner<Double> spinner) {
			this(decPlaces, 0.0, spinner);
		}




		public StringConverterDouble(int decPlaces, double defaultValue, Spinner<Double> spinner) {
			String strDecPlaces = "";
			for (int i = 0; i < decPlaces; i++) {
				strDecPlaces += "#";
			}
			format = new DecimalFormat("#" + (strDecPlaces.isEmpty() ? "" : ".") + strDecPlaces);
			this.spinner = spinner;
		}




		@Override
		public String toString(Double value) {
			if (value == null) {
				return "NaN";
			} else {
				return format.format(value);
			}
		}




		@Override
		public Double fromString(String value) {
			boolean successful = true;
			double newValue = 0;
			try {

				if (value == null) {
					successful = false;
				} else {
					value = value.trim();
					if (value.length() < 1) {
						successful = false;
					} else {
						successful = true;
						newValue = format.parse(value).doubleValue();
					}
				}

			} catch (ParseException e) {
				successful = false;
			}

			if (successful) {
				return newValue;
			} else {
				newValue = spinner.getValue();
				spinner.getEditor().setText("" + newValue);
				commitEditorText(spinner);
				return newValue;
			}

		}

	}




	private static <T> void commitEditorText(Spinner<T> spinner) {
		if (!spinner.isEditable()) return;
		String text = spinner.getEditor().getText();
		SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
		if (valueFactory != null) {
			StringConverter<T> converter = valueFactory.getConverter();
			if (converter != null) {
				T value = converter.fromString(text);
				valueFactory.setValue(value);
			}
		}
	}




	public static void initSpinner(Spinner<?> spinner, double defaultValue, double min, double max, double step, int decPlaces, ChangeListener listener) {
		initSpinner(spinner, defaultValue, min, max, step, decPlaces, false, true, listener);
	}




	public static void initSpinner(Spinner<?> spinner, double defaultValue, double min, double max, double step, int decPlaces, boolean forceDouble, ChangeListener listener) {
		initSpinner(spinner, defaultValue, min, max, step, decPlaces, forceDouble, true, listener);
	}




	@SuppressWarnings ({"rawtypes", "unchecked"})
	public static void initSpinner(Spinner<?> spinner, double defaultValue, double min, double max, double step, int decPlaces, boolean forceDouble, boolean enableMouseWheel, ChangeListener listener) {
		if (decPlaces <= 0 && !forceDouble) {
			SpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory((int) min, (int) max, (int) defaultValue, (int) step);
			valueFactory.setConverter(new StringConverterInt((Spinner<Integer>) spinner));
			spinner.setValueFactory(valueFactory);
		} else {
			SpinnerValueFactory valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, defaultValue, step);
			valueFactory.setConverter(new StringConverterDouble(decPlaces, defaultValue, (Spinner<Double>) spinner));
			spinner.setValueFactory(valueFactory);
		}
		spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				commitEditorText(spinner);
			}
		});
		if (enableMouseWheel) {
			spinner.setOnScroll(event -> {

				// early out if is in scrollable scrollpane
				if (spinner.getParent() != null) {
					Parent parent = spinner;
					while ((parent = parent.getParent()) != null) {
						if (parent instanceof ScrollPane) {
							ScrollPane scrollPane = (ScrollPane) parent;
							if (scrollPane.getContent() != null && scrollPane.getContent() instanceof AnchorPane) {
								double heightScroll = scrollPane.getHeight();
								double heightContent = ((AnchorPane) scrollPane.getContent()).getHeight();
								if (heightScroll < heightContent) {
									return;
								}
							}
						}
					}
				}


				if (event.getDeltaY() > 0) {
					spinner.getValueFactory().increment(1);
				} else {
					spinner.getValueFactory().decrement(1);
				}
				event.consume();
			});
		}
		if (listener != null) {
			spinner.valueProperty().addListener(listener);
		}
	}


}
