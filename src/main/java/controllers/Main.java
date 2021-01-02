package controllers;

import backend.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static Parent root;
    static Stage primaryStage;
    static Main main;

    static User activeUser;
    static Tour activeTour;
    static Employee employee;
    static Carrier carrier;
    static TourGuide tourGuide;
    static Hotel hotel;
    static Reservation reservation;
    static AdditionalService additionalService;
    static Attraction attraction;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml-files/LogInScene.fxml"));
        root = loader.load();
        activeUser = new User();
        employee = new Employee();
        Main.primaryStage = primaryStage;

        primaryStage.setTitle("Booking Application");
        Scene welcomeScene = new Scene(root);
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

    public static Employee getEmployee() {
        return employee;
    }

    public static void setEmployee(Employee employee) {
        Main.employee = employee;
    }

    public static Carrier getCarrier() {
        return carrier;
    }

    public static void setCarrier(Carrier carrier) {
        Main.carrier = carrier;
    }

    public static TourGuide getTourGuide() {
        return tourGuide;
    }

    public static void setTourGuide(TourGuide tourGuide) {
        Main.tourGuide = tourGuide;
    }

    public static Hotel getHotel() {
        return hotel;
    }

    public static void setHotel(Hotel hotel) {
        Main.hotel = hotel;
    }

    public static Tour getActiveTour() {
        return activeTour;
    }

    public static void setActiveTour(Tour activeTour) {
        Main.activeTour = activeTour;
    }

    public static Reservation getReservation() {
        return reservation;
    }

    public static void setReservation(Reservation reservation) {
        Main.reservation = reservation;
    }

    public static AdditionalService getAdditionalService() {
        return additionalService;
    }

    public static void setAdditionalService(AdditionalService additionalService) {
        Main.additionalService = additionalService;
    }

    public static Attraction getAttraction() {
        return attraction;
    }

    public static void setAttraction(Attraction attraction) {
        Main.attraction = attraction;
    }
}
