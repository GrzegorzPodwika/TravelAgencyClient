package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.ReservationService;
import backend.model.Reservation;
import backend.model.ReservationStatus;
import backend.model.Tour;
import backend.tabledata.ReservationData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import static javafx.scene.control.Alert.*;
import static utils.Constants.LOGIN_SCENE;
import static utils.Constants.USER_SCENE;

public class ManageReservationsController {
    @FXML public Label clockLabel;
    @FXML public TableView<ReservationData> tableviewReservations;
    @FXML public TableColumn<ReservationData, Integer> tableReservationId;
    @FXML public TableColumn<ReservationData, String> tableStatus;
    @FXML public TableColumn<ReservationData, Integer> tableNumberOfTickets;
    @FXML public TableColumn<ReservationData, Double> tableTotalPrice;
    @FXML public TableColumn<ReservationData, LocalDate> tableReservationDate;
    @FXML public TableColumn<ReservationData, LocalDate> tableDeadlineDate;
    @FXML public Button buttonDeleteReservation;
    @FXML public Button buttonPayReservation;

    private Clock clk;
    private final ReservationService reservationService = AgencyServiceGenerator.createService(ReservationService.class);
    private List<Reservation> allReservationsFilteredByUser;
    private final ObservableList<ReservationData> observableReservations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        setRowClickListener();
        fetchAllReservationsFilteredByUser();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initTableColumns() {
        tableReservationId.setCellValueFactory(new PropertyValueFactory<>("TableReservationId"));
        tableStatus.setCellValueFactory(new PropertyValueFactory<>("TableStatus"));
        tableNumberOfTickets.setCellValueFactory(new PropertyValueFactory<>("TableNumberOfTickets"));
        tableTotalPrice.setCellValueFactory(new PropertyValueFactory<>("TableTotalPrice"));
        tableReservationDate.setCellValueFactory(new PropertyValueFactory<>("TableReservationDate"));
        tableDeadlineDate.setCellValueFactory(new PropertyValueFactory<>("TableDeadlineDate"));
    }

    private void setRowClickListener() {
        tableviewReservations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                buttonDeleteReservation.setDisable(true);
                buttonPayReservation.setDisable(true);
            } else {
                if (newSelection.getTableStatus().equals(ReservationStatus.PENDING_FOR_PAYMENT.name())) {
                    buttonDeleteReservation.setDisable(false);
                    buttonPayReservation.setDisable(false);
                } else {
                    buttonDeleteReservation.setDisable(true);
                    buttonPayReservation.setDisable(true);
                }
            }
        });
    }

    private void fetchAllReservationsFilteredByUser() {
        var fetchAReservationsByUser = reservationService.getAllByUser(Main.getUser());
        fetchAReservationsByUser.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    allReservationsFilteredByUser = response.body();

                    if (allReservationsFilteredByUser != null) {
                        observableReservations.clear();
                        observableReservations.addAll(transformToDataFormat(allReservationsFilteredByUser));
                        Platform.runLater(() -> {
                            tableviewReservations.setItems(observableReservations);
                        });
                    }
                } else {
                    System.out.println("List of reservations == null");
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private List<ReservationData> transformToDataFormat(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> new ReservationData(reservation.getReservationId(), reservation.getStatus(),
                        reservation.getNumberOfTickets(), reservation.getTotalPrice(), reservation.getReservationDate(),
                        reservation.getTour().getDepartureDate())).collect(Collectors.toList());
    }

    @FXML
    public void onLogoutClick() {
        Main.setUser(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void onGoBackClick() {
        SceneCreator.launchScene(USER_SCENE);
        shutdown();
    }

    @FXML
    public void onDeleteReservationClick() {
        int selectedReservationIndex = tableviewReservations.getSelectionModel().getSelectedIndex();

        if (selectedReservationIndex == -1)
            return;

        Reservation selectedReservation = allReservationsFilteredByUser.get(selectedReservationIndex);

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("Rezygnacja z rezerwacji");
        alert.setContentText("Czy na pewno chcesz odwołać rezerwację?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Tour tour = selectedReservation.getTour();
            int newNumberOfAvailableTickets = tour.getAvailableTickets() +  selectedReservation.getNumberOfTickets();
            int newNumberOfTakenTickets = tour.getTakenTickets() -  selectedReservation.getNumberOfTickets();

            tour.setAvailableTickets(newNumberOfAvailableTickets);
            tour.setTakenTickets(newNumberOfTakenTickets);

            selectedReservation.setTour(tour);

            var cancelReservationCall = reservationService.delete(selectedReservation);
            cancelReservationCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        System.out.println("Delete of Reservation has been successfully executed.");

                        Platform.runLater(() -> {
                            showConfirmationDialog();
                        });
                    } else {
                        System.out.println("Response wasn't successful, code = " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    System.out.println("Error has occurred" + throwable.getMessage());
                }
            });
        } else {
            // do nothing
        }

    }

    private void showConfirmationDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Anulowanie rezerwacji");
        alert.setHeaderText("Anulowanie rezerwacji przebiegło pomyślnie");
        alert.setContentText("Pieniądze zostaną zwrócone na konto z którego była przeprowadzona płatność!");

        alert.showAndWait();
    }

    @FXML
    public void onPayReservationClick() {
        var selectedReservationIndex = tableviewReservations.getSelectionModel().getSelectedIndex();

        if (selectedReservationIndex == -1) {
            return;
        }

        Reservation selectedReservation = allReservationsFilteredByUser.get(selectedReservationIndex);
        Main.setReservation(selectedReservation);

        Pane root;
        String fullPath = "fxml-files/MakePaymentScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllReservationsFilteredByUser();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void shutdown(){
        clk.terminate();
    }

}
