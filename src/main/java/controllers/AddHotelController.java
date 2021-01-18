package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.HotelService;
import backend.model.Hotel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static utils.Constants.*;

public class AddHotelController {
    @FXML public TextField inputName;
    @FXML public ChoiceBox<Integer> choiceBoxNumbOfStars;
    @FXML public TextField inputAddress;
    @FXML public TextField inputZipcode;
    @FXML public TextField inputCity;
    public ComboBox<String> comboBoxCountry;
    @FXML public Label labelError;
    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final HotelService hotelService = AgencyServiceGenerator.createService(HotelService.class);
    private final List<Integer> stars = Arrays.asList(2, 3, 4, 5, 6, 7);

    @FXML
    public void initialize() {
        initChoiceBox();
    }

    private void initChoiceBox() {
        choiceBoxNumbOfStars.getItems().setAll(stars);
        comboBoxCountry.getItems().setAll(COUNTRIES);
    }

    @FXML
    public void onCancelClick() {
        closeWindow();
    }

    @FXML
    public void onConfirmClick() {
        if (viewsAreNotEmpty()) {
            var name = inputName.getText();
            var numbOfStars = choiceBoxNumbOfStars.getValue();
            var address = inputAddress.getText();
            var zipcode = inputZipcode.getText();
            var city = inputCity.getText();
            var country = comboBoxCountry.getSelectionModel().getSelectedItem();

            Hotel newHotel = new Hotel(0, name, numbOfStars, address, zipcode, city, country);

            try {
                hotelService.save(newHotel).execute();
                closeWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            labelError.setText(ERROR_EMPTY_VIEW);
        }
    }

    private boolean viewsAreNotEmpty() {
        return inputName.getText() != null && !inputName.getText().isEmpty()
                && choiceBoxNumbOfStars.getSelectionModel().getSelectedItem() != null
                && inputAddress.getText() != null && !inputAddress.getText().isEmpty()
                && inputZipcode.getText() != null && !inputZipcode.getText().isEmpty()
                && inputCity.getText() != null && !inputCity.getText().isEmpty()
                && comboBoxCountry.getSelectionModel().getSelectedItem() != null;
    }


    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }
}
