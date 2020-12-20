package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import main.java.Main;
import main.java.backend.model.Reservation;
import main.java.utils.Clock;
import main.java.utils.ManageCell;
import main.java.utils.SceneCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManageToursController {
    @FXML
    Label clockLabel;

    @FXML
    ListView<Reservation> manageList;
    ObservableList<Reservation> list = FXCollections.observableArrayList();
    Clock clk;
    Thread th;
    int toBeDeletedId;

    @FXML
    public void initialize() throws IOException {
        clk = new Clock(clockLabel);
        th = new Thread(clk);
        th.start();
        getAllReservations();

    }
    public void goBackButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("UserScene.fxml", Main.getUser());
        shutdown();
    }
    public void logOutButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("LogInScene.fxml",Main.getUser());
        shutdown();
    }
    public void shutdown(){
        clk.terminate();
    }

    public void getAllReservations() throws IOException{
        String result = "getAllReservations " + Main.getUser().getId();
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        String[] all = str.split("#");
        for(String reservation: all){
            String[] one = reservation.split("@");
            Reservation listReservation = new Reservation(Integer.parseInt(one[0]),one[1],Integer.parseInt(one[2]),one[3],one[4]);
            list.add(listReservation);

        }
        this.manageList.setItems(list);
        manageList.setCellFactory(param -> new ManageCell("Przejrzyj", "Opłać"));
    }

}
