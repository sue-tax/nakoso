package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

	static final float VERSION = 0.10f;

	static Stage stage;

	static String strText;

	static ConfigProc configProc = null;
	static FileProc fileProc = null;

	@Override
	public void start(Stage stage) {
		try {
    		Parent root = FXMLLoader.load(
	    			getClass().
	    			getResource("nakoso.fxml"));
			Scene scene = new Scene(root);
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(
					getClass().getResource(
					"application.css").toExternalForm());
	    	stage.setTitle(String.format(
	    			"nakoso version %.2f",
	    			Main.VERSION));
			stage.setScene(scene);
			stage.show();
			Main.stage = stage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
