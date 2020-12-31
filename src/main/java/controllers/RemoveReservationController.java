package controllers;

import backend.model.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import utils.Clock;
import utils.SceneCreator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

import static java.util.Objects.isNull;


public class RemoveReservationController {
    @FXML
    Label clockLabel;
    @FXML
    ListView<Reservation> removeList;
    ObservableList<Reservation> list = FXCollections.observableArrayList();
    Clock clk;
    Thread th;
    int toBeDeletedId;

    @FXML
    public void initialize() throws IOException {
        clk = new Clock(clockLabel);
        th = new Thread(clk);
        th.start();
    }
    @FXML
    public void logOutButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("LogInScene.fxml");
        shutdown();
    }
    @FXML
    public void goBackButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("UserScene.fxml");
        shutdown();
    }

    @FXML
    public void confirmPopup() throws IOException {

        if(!isNull(this.removeList.getSelectionModel().getSelectedItem())) {
            Reservation res = this.removeList.getSelectionModel().getSelectedItem();
            toBeDeletedId = res.getReservationId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie");
            alert.setHeaderText("Usuwanie rezerwacji");
            alert.setContentText("Czy jesteś pewny/a że chcesz anulować rezerwację o id: " + toBeDeletedId + "?");
            alert.setX(750);
            alert.setY(384);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                //giveBackToTour();
                //deleteReservation();
            }
        }
    }

    public void shutdown(){
        clk.terminate();
    }



}
