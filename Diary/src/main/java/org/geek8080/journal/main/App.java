package org.geek8080.journal.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.geek8080.journal.entities.Diary;
import org.geek8080.journal.entities.Page;
import org.geek8080.journal.entities.User;
import org.geek8080.journal.main.account.LogInController;
import org.geek8080.journal.main.account.OTPController;
import org.geek8080.journal.services.Authenticator;
import org.geek8080.journal.services.Database;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

	public static User USER;
	public static Database DB;
	public static Authenticator AUTH;
	public static Diary DIARY;
	public static boolean firstUse = false;


	@Override
	public void start(Stage stage) throws Exception {
		Parent root;

		if (firstUse) {
			// launch OTP window
			root = FXMLLoader.load(getClass().getResource("account/OTP.fxml"));
			stage.setTitle("OTP Verification");
			OTPController.pstage = stage;
		}else {
			// Launch log in window
			root = FXMLLoader.load(getClass().getResource("account/LogIn.fxml"));
			stage.setTitle("Log In");
			LogInController.pstage = stage;
		}

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		stage.setOnCloseRequest(e -> {
			System.exit(0);
		});

	}

	@Override
	public void init() throws Exception {
		AUTH = Authenticator.getInstance();
		if (AUTH.getUSER() != null){
			USER = AUTH.getUSER();
		}else{
			firstUse = true;
		}
		HashMap<String, String> tables = new HashMap<>(Map.of("PageView", Page.getSQLGenerationQuery()));
		DB = Database.getInstance("Diary", tables);
		DIARY = new Diary(DB.executeQuery("SELECT * FROM PAGE ORDER BY CREATION_TIME ASC;"));
	}

	public static void main(String[] args) {
		System.setProperty("javafx.preloader", "org.geek8080.journal.main.preloader.PreloaderFX");
		Application.launch(App.class, args);
	}
}
