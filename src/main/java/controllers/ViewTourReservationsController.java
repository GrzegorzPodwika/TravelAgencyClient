package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.ReservationService;
import backend.model.Reservation;
import backend.tabledata.ReservationData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ViewTourReservationsController {

    @FXML public TableView<ReservationData> tableviewReservations;
    @FXML public TableColumn<ReservationData, Integer> tableReservationId;
    @FXML public TableColumn<ReservationData, String> tableStatus;
    @FXML public TableColumn<ReservationData, Integer> tableNumberOfTickets;
    @FXML public TableColumn<ReservationData, Double> tableTotalPrice;
    @FXML public TableColumn<ReservationData, LocalDate> tableReservationDate;
    @FXML public TableColumn<ReservationData, LocalDate> tableDeadlineDate;
    @FXML public Button buttonGoBack;

    private final ReservationService reservationService = AgencyServiceGenerator.createService(ReservationService.class);
    private List<Reservation> allReservationsFilteredByTour;
    private final ObservableList<ReservationData> observableReservations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initTableColumns();
        fetchAllReservationsFilteredByTour();
    }

    private void initTableColumns() {
        tableReservationId.setCellValueFactory(new PropertyValueFactory<>("TableReservationId"));
        tableStatus.setCellValueFactory(new PropertyValueFactory<>("TableStatus"));
        tableNumberOfTickets.setCellValueFactory(new PropertyValueFactory<>("TableNumberOfTickets"));
        tableTotalPrice.setCellValueFactory(new PropertyValueFactory<>("TableTotalPrice"));
        tableReservationDate.setCellValueFactory(new PropertyValueFactory<>("TableReservationDate"));
        tableDeadlineDate.setCellValueFactory(new PropertyValueFactory<>("TableDeadlineDate"));
    }

    private void fetchAllReservationsFilteredByTour() {
        var fetchAllReservationByTour = reservationService.getAllByTour(Main.getActiveTour());
        fetchAllReservationByTour.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    allReservationsFilteredByTour = response.body();

                    if (allReservationsFilteredByTour != null) {
                        observableReservations.clear();
                        observableReservations.addAll(transformToDataFormat(allReservationsFilteredByTour));
                        Platform.runLater(() -> {
                            tableviewReservations.setItems(observableReservations);
                        });
                    } else  {
                        System.out.println("List of reservations == null");
                    }
                } else {
                    System.out.println("Something went wrong. " + response.code());
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
    public void onGoBackClick() {
        closeWindow();
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonGoBack.getScene().getWindow();
        currentStage.close();
    }


}
