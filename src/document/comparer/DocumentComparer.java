package document.comparer;

/**
 *
 * @author Kellen
 */

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class DocumentComparer extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override	
	public void start(Stage primaryStage) {

		primaryStage.setTitle("VSpaceDetector v. 1.0");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		Text scenetitle = new Text("Select Files");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label file1LA = new Label("File 1:");
		grid.add(file1LA, 0, 1);

		TextField file1TF = new TextField();
		file1TF.setPrefWidth(300);
		grid.add(file1TF, 1, 1);
		
		Button brows1 = new Button("Browse");
		HBox hbBtn1 = new HBox(20);
		hbBtn1.setAlignment(Pos.CENTER);
		hbBtn1.getChildren().add(brows1);
		grid.add(hbBtn1, 2, 1);
		brows1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			
				FileChooser fileChooser = new FileChooser();
				
				String currentDir = System.getProperty("user.dir") + File.separator;
				
				fileChooser.setInitialDirectory(new File(currentDir));
				
				fileChooser.setTitle("Select File");

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*", "*");

				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(null);

				if(file!=null)

				file1TF.setText(file.getPath());
			}
		});
		
		
		
		Label file2LA = new Label("File 2:");
		grid.add(file2LA, 0, 2);

		TextField file2TF = new TextField();
		grid.add(file2TF, 1, 2);
		
		Button brows2 = new Button("Browse");
		HBox hbBtn2 = new HBox(20);
		hbBtn2.setAlignment(Pos.CENTER);
		hbBtn2.getChildren().add(brows2);
		grid.add(hbBtn2, 2, 2);
		brows2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				FileChooser fileChooser = new FileChooser();
				
				String currentDir = System.getProperty("user.dir") + File.separator;
				
				fileChooser.setInitialDirectory(new File(currentDir));
				
				fileChooser.setTitle("Select File");

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*", "*");

				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(null);

				if(file!=null)

				file2TF.setText(file.getPath());
			}
		});
		
		
		
		Button comp = new Button("Compare");
		HBox hbBtn3 = new HBox(10);
		hbBtn3.setAlignment(Pos.CENTER);
		hbBtn3.getChildren().add(comp);
		grid.add(hbBtn3, 1, 4);
		
		final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 5);
		
		comp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
			
				actiontarget.setText("");
				
				File f1 = new File(file1TF.getText());
				
				File f2 = new File(file2TF.getText());
				
				if ( f1.canRead() && f2.canRead() ) {
					
					
					GridPane grid2 = new GridPane();
					grid2.setAlignment(Pos.CENTER);
					grid2.setHgap(20);
					grid2.setVgap(20);
					grid2.setPadding(new Insets(25, 25, 25, 25));
					
					Label secondLabel = new Label("Comparing Files");
					secondLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
					grid2.add(secondLabel, 0, 0, 2, 1);
					
					Label secondLabel2 = new Label("(Please Wait)");
					secondLabel2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
					grid2.add(secondLabel2, 0, 0, 2, 3);
					
					Label secondLabel3 = new Label("");
					secondLabel2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
					grid2.add(secondLabel3, 0, 0, 2, 4);

					Scene secondScene = new Scene(grid2, 500, 300);

					Stage secondStage = new Stage();
					secondStage.setTitle("VSpaceDetector v. 1.0");
					secondStage.setScene(secondScene);

					secondStage.show();
					
					primaryStage.close();
					
					//-------------------------------------------------------------
					//  The Magic!
					//-------------------------------------------------------------	
					HashMap<String, Integer> wordCountVector1;
					HashMap<String, Integer> wordCountVector2;
					HashMap<String, Integer> wordOrderVector1;
					HashMap<String, Integer> wordOrderVector2;
					HashMap<String, Integer> semanticVector1;
					HashMap<String, Integer> semanticVector2;
					
					
					try {
						//Word Count Vectors
						wordCountVector1 = Vectors.makeWordVector(new Scanner(f1));
						wordCountVector2 = Vectors.makeWordVector(new Scanner(f2));
						Vectors.sameMapKeys(wordCountVector1, wordCountVector2);
						
						//Word Order Vectors
						wordOrderVector1 = Vectors.makeOrderVector(new Scanner(f1));
						wordOrderVector2 = Vectors.makeOrderVector(new Scanner(f2));
						Vectors.sameMapKeys(wordOrderVector1, wordOrderVector2);
						
						//Semantic Vectors
						//semanticVector1 = Vectors.makeSemanticVector(wordCountVector1);
						//semanticVector2 = Vectors.makeSemanticVector(wordCountVector2);
						//Vectors.sameMapKeys(semanticVector1, semanticVector2);	

						GridPane grid3= new GridPane();
						grid3.setAlignment(Pos.TOP_LEFT);
						grid3.setHgap(20);
						grid3.setVgap(20);
						grid3.setPadding(new Insets(25, 25, 25, 25));

						Scene thirdScene = new Scene(grid3, 500, 300);

						Stage thirdStage = new Stage();
						thirdStage.setTitle("VSpaceDetector v. 1.0");
						thirdStage.setScene(thirdScene);
						
						Label thirdLabel = new Label("Results");
						thirdLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
						thirdLabel.setAlignment(Pos.TOP_CENTER);
						grid3.add(thirdLabel, 5, 0, 1, 1);
						
						Button rev1 = new Button("Word Count Similarity " + (int)(Vectors.compareVectors(wordCountVector1, wordCountVector2) * 100) + "%" );
						HBox hbBtn4 = new HBox(20);
						hbBtn4.setAlignment(Pos.TOP_LEFT);
						hbBtn4.getChildren().add(rev1);
						grid3.add(hbBtn4, 0, 1);
						rev1.setOnAction(new EventHandler<ActionEvent>() {
						
							@Override
							public void handle(ActionEvent event) {

								final CategoryAxis xAxis = new CategoryAxis();
								final NumberAxis yAxis = new NumberAxis();
								
								xAxis.setTickLabelsVisible(false);
								yAxis.setTickLabelsVisible(false);
							
								final ScatterChart<String,Number> sc = new ScatterChart<String,Number>(xAxis,yAxis);
								sc.setTitle("Word Count Summary");
								xAxis.setLabel("Words");       
								yAxis.setLabel("Occurrences");
						 
								XYChart.Series series1 = new XYChart.Series();
								series1.setName("Document 1");
								
								Set<String> set = wordCountVector1.keySet();
								
								for (String s : set) {
									series1.getData().add(new XYChart.Data(s, wordCountVector1.get(s)));   
								}
								
								XYChart.Series series2 = new XYChart.Series();
								series2.setName("Document 2");
								
								Set<String> set2 = wordCountVector2.keySet();
								
								for (String s : set2) {
									series2.getData().add(new XYChart.Data(s, wordCountVector2.get(s)));
								}
		 
		 
								Stage fourthStage = new Stage();
								Scene scene  = new Scene(sc,800,600);
								sc.getData().addAll(series1, series2);
								fourthStage.setScene(scene);
								
								fourthStage.show();
								
							}
						});
						
						Button rev2 = new Button("Word Order Similarity " + (int)(Vectors.compareVectors(wordOrderVector1, wordOrderVector2) * 100) + "%" );
						HBox hbBtn5 = new HBox(20);
						hbBtn5.setAlignment(Pos.TOP_LEFT);
						hbBtn5.getChildren().add(rev2);
						grid3.add(hbBtn5, 0, 2);
						rev2.setOnAction(new EventHandler<ActionEvent>() {
						
							@Override
							public void handle(ActionEvent event) {

								final CategoryAxis xAxis = new CategoryAxis();
								final NumberAxis yAxis = new NumberAxis();
								
								xAxis.setTickLabelsVisible(false);
								yAxis.setTickLabelsVisible(false);
							
								final ScatterChart<String,Number> sc = new ScatterChart<String,Number>(xAxis,yAxis);
								sc.setTitle("Word Order");
								xAxis.setLabel("Word Pairs");       
								yAxis.setLabel("Occurrences");
						 
								XYChart.Series series1 = new XYChart.Series();
								series1.setName("Document 1");
								
								Set<String> set = wordOrderVector1.keySet();
								
								for (String s : set) {
									series1.getData().add(new XYChart.Data(s, wordOrderVector1.get(s)));   
								}
								
								XYChart.Series series2 = new XYChart.Series();
								series2.setName("Document 2");
								
								Set<String> set2 = wordOrderVector2.keySet();
								
								for (String s : set2) {
									series2.getData().add(new XYChart.Data(s, wordOrderVector2.get(s)));
								}
		 
		 
								Stage fourthStage = new Stage();
								Scene scene  = new Scene(sc,800,600);
								sc.getData().addAll(series1, series2);
								fourthStage.setScene(scene);
								
								fourthStage.show();
								
							}
						});
						
						/*
						Button rev3 = new Button("Semantic Similarity " + (int)(Vectors.compareVectors(semanticVector1, semanticVector2) * 100) + "%" );
						HBox hbBtn6 = new HBox(20);
						hbBtn6.setAlignment(Pos.TOP_LEFT);
						hbBtn6.getChildren().add(rev3);
						grid3.add(hbBtn6, 0, 2);
						rev3.setOnAction(new EventHandler<ActionEvent>() {
						
							@Override
							public void handle(ActionEvent event) {

								final CategoryAxis xAxis = new CategoryAxis();
								final NumberAxis yAxis = new NumberAxis();
								
								//xAxis.setTickLabelsVisible(false);
								//yAxis.setTickLabelsVisible(false);
							
								final ScatterChart<String,Number> sc = new ScatterChart<String,Number>(xAxis,yAxis);
								sc.setTitle("Country Summary");
								xAxis.setLabel("Meanings");       
								yAxis.setLabel("Occurrences");
						 
								XYChart.Series series1 = new XYChart.Series();
								series1.setName("Document 1");
								
								Set<String> set = semanticVector1.keySet();
								
								for (String s : set) {
									series1.getData().add(new XYChart.Data(s, semanticVector1.get(s)));   
								}
								
								XYChart.Series series2 = new XYChart.Series();
								series2.setName("Document 2");
								
								Set<String> set2 = semanticVector2.keySet();
								
								for (String s : set2) {
									series2.getData().add(new XYChart.Data(s, semanticVector2.get(s)));
								}
		 
		 
								Stage fourthStage = new Stage();
								Scene scene  = new Scene(sc,800,600);
								sc.getData().addAll(series1, series2);
								fourthStage.setScene(scene);
								
								fourthStage.show();
								
							}
						});
						*/

						thirdStage.show();
						secondStage.close();
						
						
					} catch(Exception evt) {
						evt.printStackTrace();
						JOptionPane.showMessageDialog(null, "Resources unavailable. Unable to continue.", "Alert", JOptionPane.ERROR_MESSAGE);
						secondStage.close();					
					}					
						
					//-------------------------------------------------------------
					//  End The Magic!
					//-------------------------------------------------------------
					
					
				}
				else {
					actiontarget.setFill(Color.FIREBRICK);
					actiontarget.setText("Could not find files");
				}
			}
		});

		Scene scene = new Scene(grid, 500, 300);
		primaryStage.setScene(scene);
		
		
		primaryStage.show();
	}
}
