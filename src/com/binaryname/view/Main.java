package com.binaryname.view;

import java.util.Iterator;

import com.binaryname.controller.ConversionController;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import com.sun.javafx.scene.control.skin.FXVK;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * The Main class is use to show Javafx gui and virtual keyboard for user interaction
 */
public class Main extends Application {

	private ConversionController conversionController = new ConversionController();

	private PopupWindow keyboard;

	private final Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	private final Rectangle2D bounds = Screen.getPrimary().getBounds();
	private final double taskbarHeight = bounds.getHeight() - visualBounds.getHeight();

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Binary Name");

		Label helloLbl = new Label("Hello");
		helloLbl.setAlignment(Pos.CENTER);
		helloLbl.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 68));
		helloLbl.setStyle("-fx-background-color: red;padding: 20px;");
		helloLbl.setTextFill(Color.web("#ffffff"));

		Label myNameLbl = new Label("my name is");
		myNameLbl.setAlignment(Pos.CENTER);
		myNameLbl.setFont(Font.font("Comic Sans MS", 48));
		myNameLbl.setStyle("-fx-background-color: red;padding: 20px;");
		myNameLbl.setTextFill(Color.web("#ffffff"));

		TextArea nameTxtArea = new TextArea();
		nameTxtArea.setWrapText(Boolean.TRUE);
		nameTxtArea.getStyleClass().add("center-text-area");
		nameTxtArea.setFont(Font.font("Comic Sans MS", 28));
		nameTxtArea.setStyle("padding: 20px;");

		Label dummy = new Label(" ");
		dummy.setStyle("-fx-background-color: red;");
		dummy.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 10));

		Button printBtn = new Button("PRINT");
		printBtn.setId("ipad-grey");
		printBtn.setDisable(Boolean.TRUE);

		Button convertBtn = new Button("Convert Now!");
		convertBtn.setId("ipad-grey");
		convertBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				nameTxtArea.setText(conversionController.getBinaryName(nameTxtArea.getText()));
				convertBtn.setDisable(Boolean.TRUE);
				printBtn.setDisable(Boolean.FALSE);
				nameTxtArea.requestFocus();
			}
		});

		HBox hBox = new HBox(100);
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(convertBtn, printBtn);

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.TOP_CENTER);
		vBox.getChildren().addAll(helloLbl, myNameLbl, nameTxtArea, dummy, hBox);
		vBox.setStyle("-fx-background-color: red;margin: 20px;");

		printBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Start printing
				print(vBox, nameTxtArea.getText());
				convertBtn.setDisable(Boolean.FALSE);
				printBtn.setDisable(Boolean.TRUE);
				nameTxtArea.setText("");
				nameTxtArea.requestFocus();
			}
		});

		Scene scene = new Scene(vBox);
		scene.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setX(visualBounds.getMinX());
		primaryStage.setY(visualBounds.getMinY());
		primaryStage.setWidth(visualBounds.getWidth());
		primaryStage.setHeight(visualBounds.getHeight());

		adjustTextAreaLayout(nameTxtArea);

		primaryStage.show();

		// attach keyboard to first node on scene:
		Node first = scene.getRoot().getChildrenUnmodifiable().get(0);
		if (first != null) {
			FXVK.init(first);
			FXVK.attach(first);
			keyboard = getPopupWindow();
		}
		
		// Attach the on focus listener
		nameTxtArea.focusedProperty().addListener((ob, b, b1) -> {
			if (keyboard == null) {
				keyboard = getPopupWindow();
			}

			keyboard.setHideOnEscape(Boolean.FALSE);
			keyboard.setAutoHide(Boolean.FALSE);
			keyboard.centerOnScreen();
			keyboard.requestFocus();

			Double y = bounds.getHeight() - taskbarHeight - keyboard.getY();
			nameTxtArea.setMaxHeight((bounds.getHeight() - y));
			nameTxtArea.setMinHeight((bounds.getHeight() - y));
			nameTxtArea.setPrefHeight((bounds.getHeight() - y));

			/*keyboard.yProperty().addListener(obs -> {

				Platform.runLater(() -> {
				});
			});*/

		});

	}
	
	/**
	 * The main() method is use to launch the Javafx gui.
	 * @param args not used
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * The print() method is use to print the text with given dimension
	 * 
	 * @param node1
	 *            to be printed
	 * @param text
	 *            to be written
	 */
	private void print(Node node1, String text) {
		// Create a printer job for the default printer

		Printer printer = Printer.getDefaultPrinter();
		Paper label = PrintHelper.createPaper("2.5x3.5", 2.5, 3.5, Units.INCH);
		PageLayout pageLayout = printer.createPageLayout(label, PageOrientation.LANDSCAPE, Printer.MarginType.EQUAL);

		PrinterJob job = PrinterJob.createPrinterJob();

		Node node = createFullNode(text);

		double scaleX = pageLayout.getPrintableWidth() / node1.getBoundsInParent().getWidth();
		double scaleY = pageLayout.getPrintableHeight() / node1.getBoundsInParent().getHeight();
		node.getTransforms().add(new Scale(scaleX, scaleY));

		if (job != null) {
			// Print the node
			boolean printed = job.printPage(node);

			if (printed) {
				// End the printer job
				job.endJob();
			} else {
				// Write Error Message
				System.out.println("Printing failed.");
			}
		} else {
			// Write Error Message
			System.out.println("Could not create a printer job.");
		}

		node.getTransforms().remove(node.getTransforms().size() - 1);
	}

	/**
	 * The getPopupWindow() method is use to show keyboard on demand
	 * 
	 * @return the keyboard instance
	 */
	private PopupWindow getPopupWindow() {

		@SuppressWarnings("deprecation")
		final Iterator<Window> windows = Window.impl_getWindows();

		while (windows.hasNext()) {
			final Window window = windows.next();
			if (window instanceof PopupWindow) {
				if (window.getScene() != null && window.getScene().getRoot() != null) {
					Parent root = window.getScene().getRoot();
					if (root.getChildrenUnmodifiable().size() > 0) {
						Node popup = root.getChildrenUnmodifiable().get(0);
						if (popup.lookup(".fxvk") != null) {
							FXVK vk = (FXVK) popup.lookup(".fxvk");
							// hide the keyboard-hide key
							vk.lookup(".hide").setVisible(false);
							return (PopupWindow) window;
						}
					}
				}
				return null;
			}
		}
		return null;
	}

	/**
	 * The createFullNode() method is use to create an imaginary node that will
	 * be used for printing
	 * 
	 * @param text
	 *            that will be printed
	 * @return the node to be printed
	 */
	private Node createFullNode(String text) {

		Label helloLbl = new Label("Hello");
		helloLbl.setAlignment(Pos.CENTER);
		helloLbl.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 68));
		helloLbl.setStyle("-fx-background-color: red;padding: 20px;");
		helloLbl.setTextFill(Color.web("#ffffff"));

		Label myNameLbl = new Label("my name is");
		myNameLbl.setAlignment(Pos.CENTER);
		myNameLbl.setFont(Font.font("Comic Sans MS", 48));
		myNameLbl.setStyle("-fx-background-color: red;padding: 20px;");
		myNameLbl.setTextFill(Color.web("#ffffff"));

		TextArea nameTxtArea = new TextArea();
		nameTxtArea.setWrapText(Boolean.TRUE);
		nameTxtArea.setFont(Font.font("Comic Sans MS", 28));
		nameTxtArea.setStyle("padding: 20px;");
		nameTxtArea.setText(text);
		nameTxtArea.getStyleClass().add("center-text-area");

		Label dummy = new Label(" ");
		dummy.setStyle("-fx-background-color: red;");
		dummy.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(helloLbl, myNameLbl, nameTxtArea, dummy);
		vBox.setStyle("-fx-background-color: red;margin: 20px;");

		vBox.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());

		return vBox;
	}

	/**
	 * The adjustTextAreaLayout() method is use to adjust the textarea layout.
	 * It will attach the listener when text is changed
	 * 
	 * @param textArea
	 */
	private void adjustTextAreaLayout(TextArea textArea) {
		textArea.applyCss();
		textArea.layout();

		ScrollPane textAreaScroller = (ScrollPane) textArea.lookup(".scroll-pane");
		Text text = (Text) textArea.lookup(".text");

		ChangeListener<? super Bounds> listener = (obs, oldBounds, newBounds) -> centerTextIfNecessary(textAreaScroller, text);
		textAreaScroller.viewportBoundsProperty().addListener(listener);
		text.boundsInLocalProperty().addListener(listener);

	}

	/**
	 * The centerTextIfNecessary() method is called when each text written. This
	 * method then calculate the vertical alignment
	 * 
	 * @param textAreaScroller
	 * @param text
	 */
	private void centerTextIfNecessary(ScrollPane textAreaScroller, Text text) {
		double textHeight = text.getBoundsInLocal().getHeight();
		double viewportHeight = textAreaScroller.getViewportBounds().getHeight();
		double offset = Math.max(0, (viewportHeight - textHeight) / 2);
		text.setTranslateY(offset);
		Parent content = (Parent) textAreaScroller.getContent();
		for (Node n : content.getChildrenUnmodifiable()) {
			if (n instanceof Path) { // caret
				n.setTranslateY(offset);
			}
		}
	}
}
