package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.HotelService;
import backend.model.Hotel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static utils.Constants.ERROR_NOT_A_NUMBER;

public class EditHotelController {
    @FXML public TextField inputName;
    @FXML public TextField inputNumbOfStars;
    @FXML public TextField inputAddress;
    @FXML public TextField inputZipcode;
    @FXML public TextField inputCity;
    @FXML public TextField inputCountry;
    @FXML public Label labelName;
    @FXML public Label labelNumbOfStars;
    @FXML public Label labelAddress;
    @FXML public Label labelZipcode;
    @FXML public Label labelCity;
    @FXML public Label labelCountry;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final HotelService hotelService = AgencyServiceGenerator.createService(HotelService.class);
    private final Hotel fetchedHotel = Main.getHotel();

    @FXML
    public void initialize() {
        initLabels();
        setTextFieldsListeners();
    }

    private void initLabels() {
        labelName.setText(labelName.getText() + fetchedHotel.getHotelName());
        labelNumbOfStars.setText(labelNumbOfStars.getText() + fetchedHotel.getNumberOfStars());
        labelAddress.setText(labelAddress.getText() + fetchedHotel.getAddress());
        labelZipcode.setText(labelZipcode.getText() + fetchedHotel.getZipcode());
        labelCity.setText(labelCity.getText() + fetchedHotel.getCity());
        labelCountry.setText(labelCountry.getText() + fetchedHotel.getCountry());
    }

    private void setTextFieldsListeners() {
        inputNumbOfStars.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    var isNumber = Integer.parseInt(newValue);
                    labelError.setText("");
                } catch (NumberFormatException e) {
                    labelError.setText(ERROR_NOT_A_NUMBER);
                }
            } else {
                labelError.setText("");
            }
        });
    }

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        Hotel hotelToUpdate = generateProperHotel();

        try {
            hotelService.update(hotelToUpdate).execute();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Hotel generateProperHotel() {
        Hotel hotelToUpdate = new Hotel();
        hotelToUpdate.setHotelId(fetchedHotel.getHotelId());

        if (inputName.getText().isEmpty())
            hotelToUpdate.setHotelName(fetchedHotel.getHotelName());
        else
            hotelToUpdate.setHotelName(inputName.getText());

        if (inputNumbOfStars.getText().isEmpty() || !isNumbOfStarsIsNumber())
            hotelToUpdate.setNumberOfStars(fetchedHotel.getNumberOfStars());
        else
            hotelToUpdate.setNumberOfStars(Integer.parseInt(inputNumbOfStars.getText()));

        if (inputAddress.getText().isEmpty())
            hotelToUpdate.setAddress(fetchedHotel.getAddress());
        else
            hotelToUpdate.setAddress(inputAddress.getText());

        if (inputZipcode.getText().isEmpty())
            hotelToUpdate.setZipcode(fetchedHotel.getZipcode());
        else
            hotelToUpdate.setZipcode(inputZipcode.getText());

        if (inputCity.getText().isEmpty())
            hotelToUpdate.setCity(fetchedHotel.getCity());
        else
            hotelToUpdate.setCity(inputCity.getText());

        if (inputCountry.getText().isEmpty())
            hotelToUpdate.setCountry(fetchedHotel.getCountry());
        else
            hotelToUpdate.setCountry(inputCountry.getText());


        return hotelToUpdate;
    }

    private boolean isNumbOfStarsIsNumber() {
        try {
            var isNumber = Integer.parseInt(inputNumbOfStars.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }

}
