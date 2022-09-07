package com.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 * 
 *  Made by @lolmaxz
 *  On September 2nd, 2022
 * 
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        /*scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();*/
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("examen_1_interface.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 433, 535);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Calculator Exam #1 JAVAFX");
            primaryStage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}