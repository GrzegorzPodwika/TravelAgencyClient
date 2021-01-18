package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.CarrierService;
import backend.api.TransportService;
import backend.model.Carrier;
import backend.model.Transport;
import backend.model.TransportType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.TravelUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Constants.ERROR_NOT_A_NUMBER;
import static utils.TravelUtils.roundOffStrToDouble;

public class EditTransportController {
    @FXML public TextField inputJourneyTime;
    @FXML public TextField inputJourneyDescription;
    @FXML public ComboBox<String> comboBoxTransportType;
    @FXML public ComboBox<Carrier> comboBoxCarrier;

    @FXML public Label labelTransportType;
    @FXML public Label labelJourneyTime;
    @FXML public Label labelJourneyDescription;
    @FXML public Label labelCarrier;
    @FXML public Label labelError;

    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final Transport fetchedTransport = Main.getTransport();
    private final CarrierService carrierService = AgencyServiceGenerator.createService(CarrierService.class);
    private final TransportService transportService = AgencyServiceGenerator.createService(TransportService.class);
    private final ObservableList<Carrier> observableCarriers = FXCollections.observableArrayList();
    private final List<String> journeyTypes = Arrays.stream(TransportType.values())
            .map(Enum::name).collect(Collectors.toList());

    @FXML
    public void initialize() {
        initLabels();
        setTextFieldsListeners();
        setObjectConverterInComboBox();
        fetchAllCarriers();
        setUpTransportTypeComboBox();
    }

    private void setTextFieldsListeners() {
        inputJourneyTime.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    var parsedNumber = Double.parseDouble(newValue);
                    labelError.setText("");
                } catch (NumberFormatException e) {
                    labelError.setText(ERROR_NOT_A_NUMBER);
                }
            } else {
                labelError.setText("");
            }
        });

    }

    private void setObjectConverterInComboBox() {
        comboBoxCarrier.setConverter(new StringConverter<Carrier>() {
            @Override
            public String toString(Carrier carrier) {
                return carrier.getName();
            }

            @Override
            public Carrier fromString(String s) {
                return comboBoxCarrier.getItems().stream()
                        .filter(carrier -> carrier.getName().equals(s)).findFirst().orElse(null);
            }
        });
    }

    private void fetchAllCarriers() {
        var fetchCarriersCall = carrierService.getAll();
        fetchCarriersCall.enqueue(new Callback<List<Carrier>>() {
            @Override
            public void onResponse(Call<List<Carrier>> call, Response<List<Carrier>> response) {
                if (response.isSuccessful()) {
                    var carriers = response.body();

                    if (carriers != null) {
                        observableCarriers.addAll(carriers);
                        Platform.runLater(() -> {
                            comboBoxCarrier.setItems(observableCarriers);
                        });
                    } else {
                        System.out.println("List of carriers == null");
                    }
                } else {
                    System.out.println("Response was not successful, code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Carrier>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private void setUpTransportTypeComboBox() {
        comboBoxTransportType.getItems().addAll(journeyTypes);
    }


    private void initLabels() {
        labelTransportType.setText(labelTransportType.getText() + fetchedTransport.getTransportType());
        labelJourneyTime.setText(labelJourneyTime.getText() + fetchedTransport.getJourneyTime());
        labelJourneyDescription.setText(labelJourneyDescription.getText() + fetchedTransport.getJourneyDescription());
        labelCarrier.setText(labelCarrier.getText() + fetchedTransport.getCarrier().getName());
    }

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        Transport transportToUpdate = generateTransport();

        try {
            transportService.update(transportToUpdate).execute();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            labelError.setText("Ups.. something gone wrong " + e.getMessage());
        }
    }

    private Transport generateTransport() {
        Transport transportToUpdate = new Transport();
        transportToUpdate.setTransportId(fetchedTransport.getTransportId());

        if (inputJourneyTime.getText() == null || inputJourneyTime.getText().isEmpty() || !isANumber(inputJourneyTime))
            transportToUpdate.setJourneyTime(fetchedTransport.getJourneyTime());
        else
            transportToUpdate.setJourneyTime(roundOffStrToDouble(inputJourneyTime.getText()));

        if (inputJourneyDescription.getText() == null || inputJourneyDescription.getText().isEmpty())
            transportToUpdate.setJourneyDescription(fetchedTransport.getJourneyDescription());
        else
            transportToUpdate.setJourneyDescription(inputJourneyDescription.getText());

        if (comboBoxCarrier.getSelectionModel().getSelectedItem() == null)
            transportToUpdate.setCarrier(fetchedTransport.getCarrier());
        else
            transportToUpdate.setCarrier(comboBoxCarrier.getSelectionModel().getSelectedItem());

        if (comboBoxTransportType.getSelectionModel().getSelectedItem() == null)
            transportToUpdate.setTransportType(fetchedTransport.getTransportType());
        else
            transportToUpdate.setTransportType(comboBoxTransportType.getSelectionModel().getSelectedItem());

        return transportToUpdate;
    }

    private boolean isANumber(TextField textField) {
        try {
            Double.parseDouble(textField.getText());
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }
}
