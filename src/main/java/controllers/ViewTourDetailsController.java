package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.ReservationService;
import backend.model.Payment;
import backend.model.Reservation;
import backend.model.ReservationStatus;
import backend.model.Tour;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewTourDetailsController {

    @FXML public Label labelTitle;
    @FXML public Label labelCountry;
    @FXML public Label labelAvailableTickets;
    @FXML public Label labelPrice;
    @FXML public Label labelDepartureDate;
    @FXML public Label labelArrivalDate;
    @FXML public Label labelHotel;
    @FXML public Label labelTransport;
    @FXML public Label labelTourGuide;

    @FXML public Label labelTotalToPay;
    @FXML public Label labelError;

    @FXML public ChoiceBox<Integer> choiceBox;
    @FXML public ImageView imageViewTour;
    @FXML public ListView<String> listViewAttractions;
    @FXML public ListView<String> listViewAdditionalServices;
    @FXML public Button buttonMakeReservation;
    @FXML public Button buttonGoBack;

    private final Tour tour = Main.getActiveTour();
    private final ReservationService reservationService = AgencyServiceGenerator.createService(ReservationService.class);
    private final String PREFIX_IMAGE = "/images/";
    private final String SUFFIX_IMAGE = ".jpg";

    @FXML
    public void initialize() {
        initLabels();
        initChoiceBox();
        initImageView();
        initListViews();
    }

    private void initLabels() {
        labelTitle.setText(tour.getTourName());
        labelCountry.setText(tour.getCountry());
        labelAvailableTickets.setText(String.valueOf(tour.getAvailableTickets()));
        labelPrice.setText(String.valueOf(tour.getPrice()));
        labelDepartureDate.setText(tour.getDepartureDate().toString());
        labelArrivalDate.setText(tour.getArrivalDate().toString());
        labelHotel.setText(tour.getHotel().getHotelName());
        labelTransport.setText(tour.getTransport().getCarrier().getName() + " " + tour.getTransport().getTransportType());
        labelTourGuide.setText(tour.getTourGuide().getName() + " " + tour.getTourGuide().getSurname());

    }

    private void initChoiceBox() {
        List<Integer> possibleNumberOfTicketsToBuy = IntStream.rangeClosed(0, tour.getAvailableTickets())
                .boxed().collect(Collectors.toList());

        choiceBox.getItems().setAll(possibleNumberOfTicketsToBuy);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            double totalPrice = tour.getPrice() * newValue;
            totalPrice =  Math.round(totalPrice * 100.0) / 100.0;

            labelTotalToPay.setText(String.valueOf(totalPrice));
        });
    }

    private void initImageView() {
        String imgPath = PREFIX_IMAGE + tour.getImgName() + SUFFIX_IMAGE;
        Image tourImage = new Image(imgPath);
        imageViewTour.setImage(tourImage);
    }

    private void initListViews() {
        listViewAttractions.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);
        listViewAdditionalServices.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);

        List<String> listOfAttractionsAsString = tour.getAttractions().stream()
                .map(attraction -> attraction.getName() + " - " + attraction.getDescription()).collect(Collectors.toList());
        List<String> listOfAdditionalServicesAsString = tour.getAdditionalServices().stream()
                .map(additionalService -> additionalService.getName() + " - " + additionalService.getDescription()).collect(Collectors.toList());

        listViewAttractions.getItems().addAll(listOfAttractionsAsString);
        listViewAdditionalServices.getItems().addAll(listOfAdditionalServicesAsString);
    }

    @FXML
    public void onMakeReservationClick( ) {
        if (choiceBox.getSelectionModel().getSelectedItem() != 0) {
            labelError.setText("");

            int chosenNumberOfTickets = choiceBox.getSelectionModel().getSelectedItem();
            int newNumberOfAvailableTickets = tour.getAvailableTickets() - chosenNumberOfTickets;
            int newNumberOfTakenTickets = tour.getTakenTickets() + chosenNumberOfTickets;

            tour.setAvailableTickets(newNumberOfAvailableTickets);
            tour.setTakenTickets(newNumberOfTakenTickets);

            Reservation reservation = prepareReservationToSave();

            var saveReservationCall = reservationService.save(reservation);
            saveReservationCall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        System.out.println("Save of Reservation has been successfully executed.");

                        Platform.runLater(() -> {
                            closeWindow();
                        });
                    } else {
                        System.out.println("Response wasn't successful, code = " + response.code());

                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    System.out.println("Error has occurred" + throwable.getMessage());
                    Platform.runLater(() -> {
                        labelError.setText("Błędna odpowiedź z serwera!");
                    });
                }
            });

        } else {
            labelError.setText("Musisz wybrać co najmniej 1 bilet!");
        }
    }

    private Reservation prepareReservationToSave() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(0);

        reservation.setStatus(ReservationStatus.PENDING_FOR_PAYMENT.name());

        int chosenNumberOfTickets = choiceBox.getSelectionModel().getSelectedItem();
        reservation.setNumberOfSeats(chosenNumberOfTickets);
        reservation.setNumberOfTickets(chosenNumberOfTickets);

        double totalPrice = tour.getPrice() * chosenNumberOfTickets;
        totalPrice =  Math.round(totalPrice * 100.0) / 100.0;
        reservation.setTotalPrice(totalPrice);

        reservation.setReservationDate(LocalDate.now());
        reservation.setTour(tour);
        reservation.setUser(Main.getUser());

        Payment payment = new Payment();
        payment.setPaymentId(0);
        payment.setPaid(false);
        payment.setTotalToPay(totalPrice);
        payment.setPaymentType("");
        payment.setPaymentDate(null);
        payment.setDeadlineDate(tour.getDepartureDate());

        reservation.setPayment(payment);

        return reservation;
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
