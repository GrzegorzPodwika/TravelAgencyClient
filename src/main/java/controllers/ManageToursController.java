package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.TourService;
import backend.model.Reservation;
import backend.model.Tour;
import backend.tabledata.TourData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Clock;
import utils.SceneCreator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static utils.Constants.*;

public class ManageToursController {
    @FXML public Label clockLabel;
    public TableView<TourData> tableviewTours;
    public TableColumn<TourData, Integer> tableTourId;
    public TableColumn<TourData, String> tableName;
    public TableColumn<TourData, String> tableCountry;
    public TableColumn<TourData, Integer> tableAvailableTickets;
    public TableColumn<TourData, Integer> tableTakenTickets;
    public TableColumn<TourData, Double> tablePrice;
    public TableColumn<TourData, LocalDate> tableDepartureDate;
    public TableColumn<TourData, LocalDate> tableArrivalDate;
    public Button buttonDeleteTour;
    public Button buttonEditTour;
    public Button buttonAddTour;
    public Button buttonShowReservations;

    private Clock clk;
    private final TourService tourService = AgencyServiceGenerator.createService(TourService.class);
    private List<Tour> allTours;
    private final ObservableList<TourData> observableTours = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        fetchAllTours();
        setRowClickListener();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initTableColumns() {
        tableTourId.setCellValueFactory(new PropertyValueFactory<>("TableTourId"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("TableName"));
        tableCountry.setCellValueFactory(new PropertyValueFactory<>("TableCountry"));
        tableAvailableTickets.setCellValueFactory(new PropertyValueFactory<>("TableAvailableTickets"));
        tableTakenTickets.setCellValueFactory(new PropertyValueFactory<>("TableTakenTickets"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("TablePrice"));
        tableDepartureDate.setCellValueFactory(new PropertyValueFactory<>("TableDepartureDate"));
        tableArrivalDate.setCellValueFactory(new PropertyValueFactory<>("TableArrivalDate"));
    }

    private void fetchAllTours() {
        var fetchToursCall = tourService.getAll();
        fetchToursCall.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                if (response.isSuccessful()) {
                    allTours = response.body();

                    if(allTours != null) {
                        observableTours.clear();
                        observableTours.addAll(transformToDataFormat(allTours));
                        Platform.runLater(() -> {
                            tableviewTours.setItems(observableTours);
                        });
                    } else
                        System.out.println("Fetched list of tours == null!");
                } else {
                    System.out.println("Response was not successful, code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private List<TourData> transformToDataFormat(List<Tour> tours) {
        return tours.stream()
                .map(tour -> new TourData(tour.getTourId(), tour.getTourName(), tour.getCountry(), tour.getAvailableTickets(),
                        tour.getTakenTickets(), tour.getPrice(), tour.getDepartureDate(), tour.getArrivalDate()))
                .collect(Collectors.toList());
    }

    private void setRowClickListener() {
        tableviewTours.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null){
                buttonDeleteTour.setDisable(true);
                buttonEditTour.setDisable(true);
                buttonShowReservations.setDisable(true);
            } else {
                buttonDeleteTour.setDisable(false);
                buttonEditTour.setDisable(false);
                buttonShowReservations.setDisable(false);
            }
        });
    }

    @FXML
    public void onLogoutClick() {
        Main.setEmployee(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void onGoBackClick() {
        SceneCreator.launchScene(EMPLOYEE_SCENE);
        shutdown();
    }

    @FXML
    public void onDeleteTourClick() {
        var selectedTourIndex = tableviewTours.getSelectionModel().getSelectedIndex();
        if (selectedTourIndex == -1)
            return;

        var selectedTourData = tableviewTours.getSelectionModel().getSelectedItem();
        Tour selectedTour = allTours.stream()
                .filter(tour -> tour.getTourId() == selectedTourData.getTableTourId()).findFirst().get();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Usunięcie wycieczki");
        alert.setContentText("Usunięcie wycieczki spowoduje usunięcie powiązanych rezerwacji klienta!. Czy na pewno chcesz usunąć wycieczkę?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                tourService.delete(selectedTour).execute();
                fetchAllTours();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void onEditTourClick() {
        var selectedTourIndex = tableviewTours.getSelectionModel().getSelectedIndex();
        if (selectedTourIndex == -1)
            return;

        var selectedTourData = tableviewTours.getSelectionModel().getSelectedItem();
        Tour selectedTour = allTours.stream()
                .filter(tour -> tour.getTourId() == selectedTourData.getTableTourId()).findFirst().get();
        Main.setActiveTour(selectedTour);

        Pane root;
        String fullPath = "fxml-files/EditTourScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllTours();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddTourClick() {
        Pane root;
        String fullPath = "fxml-files/AddTourScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllTours();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onShowReservationsClick( ) {
        var selectedTourIndex = tableviewTours.getSelectionModel().getSelectedIndex();
        if (selectedTourIndex == -1)
            return;

        var selectedTourData = tableviewTours.getSelectionModel().getSelectedItem();
        Tour selectedTour = allTours.stream().filter(tour -> tour.getTourId() == selectedTourData.getTableTourId()).findFirst().get();

        Main.setActiveTour(selectedTour);

        Pane root;
        String fullPath = "fxml-files/ViewTourReservationsScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutdown(){
        clk.terminate();
    }
}
