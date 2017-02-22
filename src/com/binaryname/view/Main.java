package com.binaryname.view;

import com.binaryname.controller.ConversionController;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
	
	private ConversionController conversionController = new ConversionController();
	
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
		
		Button convertBtn = new Button("Convert Now!");
		convertBtn.setFont(Font.font("Arial Rounded MT Bold", 30));
		convertBtn.setStyle("-fx-background-color: #ffcccc; padding: 20px;margin: 10px;-fx-text-fill: #006666;");
		convertBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				nameTxtArea.setText(conversionController.getBinaryName(nameTxtArea.getText()));
			}
		});
		
		Button printBtn = new Button("PRINT");
		printBtn.setFont(Font.font("Arial Rounded MT Bold", 30));
		printBtn.setStyle("-fx-background-color: #ffcccc; padding: 20px;margin: 10px;-fx-text-fill: #006666;");
		
		HBox hBox = new HBox(100);
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(convertBtn, printBtn);
		
		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(helloLbl, myNameLbl, nameTxtArea, hBox);
		vBox.setStyle("-fx-background-color: red;margin: 20px;");
		
		printBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// Start printing 
				print(vBox);
			}
		});
		
		Scene scene = new Scene(vBox);
		scene.getStylesheets().add
		 (Main.class.getResource("/style.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void print(Node node) 
	{
		// Define the Job Status Message
//		jobStatus.textProperty().unbind();
//		jobStatus.setText("Creating a printer job...");
		
		// Create a printer job for the default printer
		PrinterJob job = PrinterJob.createPrinterJob();
		
		if (job != null) 
		{
			// Show the printer job status
//			jobStatus.textProperty().bind(job.jobStatusProperty().asString());
			
			// Print the node
			boolean printed = job.printPage(node);

			if (printed) 
			{
				// End the printer job
				job.endJob();
			} 
			else 
			{
				// Write Error Message
//				jobStatus.textProperty().unbind();
				System.out.println("Printing failed.");
			}
		} 
		else 
		{
			// Write Error Message
			System.out.println("Could not create a printer job.");
		}
	}	

}
