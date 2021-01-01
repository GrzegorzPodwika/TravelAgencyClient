package utils;

import backend.model.Reservation;
import controllers.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import utils.SceneCreator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class ManageCell extends ListCell<Reservation> {
    HBox hbox = new HBox();
    Label label = new Label("");
    Pane pane = new Pane();
    Button button = new Button();
    Button payButton = new Button();
    ObservableList<Reservation> list = FXCollections.observableArrayList();
    int tourId;
    public ManageCell(String name, String name2) {
        super();
        button.setMinSize(100,35);
        button.setMaxSize(100,35);
        button.setStyle("-fx-background-color: #62d813; -fx-opacity: 80%; -fx-text-fill: white;");
        button.setText(name);

        payButton.setMinSize(100,35);
        payButton.setMaxSize(100,35);
        payButton.setStyle("-fx-background-color: #0066ff; -fx-opacity: 80%; -fx-text-fill: white;");
        payButton.setText(name2);

        hbox.setSpacing(8);
        hbox.getChildren().addAll(label, pane, button, payButton);
        HBox.setHgrow(pane, Priority.ALWAYS);

        button.setOnAction(event -> {
            try {
                getTourId(getItem().getReservationId());
                getTourFromServer(tourId);
                SceneCreator.launchScene("ViewTourDetailsScene.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        payButton.setOnAction(event -> {
            try {
                if(!getItem().getStatus().equals("oplacone")) confirmPopup();
                changeToPayed(getItem().getReservationId());
                //getAllReservations();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    protected void updateItem(Reservation item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        if (item != null && !empty) {
            label.setTextFill(Color.WHITE);
            label.setText(item.toString());
            setGraphic(hbox);
        }
    }

    public void getTourFromServer(int id) throws IOException {
        String query = "getTour " + id;
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(query);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        String[] data = str.split("\\s+");
    }
    public void getTourId(int id) throws IOException {
        String query = "getTourId " + id;
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(query);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        tourId = Integer.parseInt(str);
    }
    public void changeToPayed(int id) throws IOException {
        String query = "changeToPayed " + id;
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(query);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
    }

    public void confirmPopup() throws IOException {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Potwierdzenie");
        alert.setHeaderText("Opłata");
        alert.setContentText("Dziękujemy za opłacenie rezerwacji o id: " + getItem().getReservationId() + "!");
        alert.setX(750);
        alert.setY(384);
        Optional<ButtonType> result = alert.showAndWait();
    }
}
