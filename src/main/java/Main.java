package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.backend.model.Tour;
import main.java.backend.model.User;

public class Main extends Application {
    static Parent root;
    static Stage primaryStage;
    static User activeUser;
    static Tour activeTour;
    static Main main;

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("../resources/fxml-files/LogInScene.fxml"));
        activeUser = new User();
        activeTour = new Tour();
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Booking Application");
        Scene welcomeScene = new Scene(root, 1024, 768);
        primaryStage.setResizable(false);
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        main = new Main();
        launch(args);
    }

    public static void setRoot(Parent root) {
        Main.root = root;
    }

    public static Parent getRoot() {
        return root;
    }

    public static Stage getStage() {

        return primaryStage;
    }

    public static void setStage(Stage stage) {

        Main.primaryStage = stage;
    }

    public static void setUser(User user) {
        Main.activeUser = user;
    }

    public static User getUser() {
        return Main.activeUser;
    }

    public static Tour getTour() {
        return Main.activeTour;
    }
}
