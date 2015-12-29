package org.systemexception.lunarlander.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author leo
 * @date 29/12/15 12:01
 */
public class MainJavaFx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("TestLander.fxml"));
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
	}

	public static void main(String args[]){
		launch (args);
	}
}
