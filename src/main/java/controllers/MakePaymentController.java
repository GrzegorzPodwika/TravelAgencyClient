package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.ReservationService;
import backend.model.Payment;
import backend.model.PaymentType;
import backend.model.Reservation;
import backend.model.ReservationStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MakePaymentController {
    @FXML
    public Label labelTotalToPay;
    @FXML
    public Label labelDeadlineDate;
    @FXML
    public Label labelError;
    @FXML
    public ComboBox<String> comboBoxPaymentType;
    @FXML
    public Button buttonCancel;
    @FXML
    public Button buttonConfirm;

    private final ReservationService reservationService = AgencyServiceGenerator.createService(ReservationService.class);
    private final Reservation reservation = Main.getReservation();
    private final List<String> paymentsTypes = Arrays.stream(PaymentType.values())
            .map(Enum::name).collect(Collectors.toList());


    @FXML
    public void initialize() {
        initLabels();
        initComboBox();
    }

    private void initLabels() {
        labelTotalToPay.setText(String.valueOf(reservation.getTotalPrice()));
        labelDeadlineDate.setText(String.valueOf(reservation.getPayment().getDeadlineDate()));
    }

    private void initComboBox() {
        comboBoxPaymentType.getItems().addAll(paymentsTypes);
    }

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        int selectedPaymentTypeIndex = comboBoxPaymentType.getSelectionModel().getSelectedIndex();
        if (selectedPaymentTypeIndex != -1) {
            Payment updatedPayment = reservation.getPayment();

            updatedPayment.setPaymentDate(LocalDate.now());
            updatedPayment.setPaymentType(paymentsTypes.get(selectedPaymentTypeIndex));
            updatedPayment.setPaid(true);

            reservation.setPayment(updatedPayment);
            reservation.setStatus(ReservationStatus.PAYMENT_PAID.name());

            try {
                reservationService.update(reservation).execute();
                closeWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            labelError.setText("Wybierz metodę płatności!");
        }
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }
}
