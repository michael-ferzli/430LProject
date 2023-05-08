package com.mdf00.exchange;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new
                FXMLLoader(Main.class.getResource("parent.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 750);
        stage.setTitle("Currency Exchange");
        stage.setScene(scene);
        stage.show();
        //Runing the main window
    }

    public static void main(String[] args) {
        launch();
    }
}