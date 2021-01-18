package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.HotelService;
import backend.model.Hotel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static utils.Constants.ERROR_NOT_A_NUMBER;

public class EditHotelController {

    @FXML public Label labelName;
    @FXML public Label labelNumbOfStars;
    @FXML public Label labelAddress;
    @FXML public Label labelZipcode;
    @FXML public Label labelCity;
    @FXML public Label labelCountry;
    @FXML public Label labelError;

    @FXML public TextField inputName;
    @FXML public ChoiceBox<Integer> choiceBoxNumbOfStars;
    @FXML public TextField inputAddress;
    @FXML public TextField inputZipcode;
    @FXML public TextField inputCity;
    @FXML public TextField inputCountry;

    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;

    private final HotelService hotelService = AgencyServiceGenerator.createService(HotelService.class);
    private final Hotel fetchedHotel = Main.getHotel();
    private final List<Integer> stars = Arrays.asList(2, 3, 4, 5, 6, 7);


    @FXML
    public void initialize() {
        initLabels();
        initChoiceBox();
    }

    private void initLabels() {
        labelName.setText(labelName.getText() + fetchedHotel.getHotelName());
        labelNumbOfStars.setText(labelNumbOfStars.getText() + fetchedHotel.getNumberOfStars());
        labelAddress.setText(labelAddress.getText() + fetchedHotel.getAddress());
        labelZipcode.setText(labelZipcode.getText() + fetchedHotel.getZipcode());
        labelCity.setText(labelCity.getText() + fetchedHotel.getCity());
        labelCountry.setText(labelCountry.getText() + fetchedHotel.getCountry());
    }

    private void initChoiceBox() {
            choiceBoxNumbOfStars.getItems().setAll(stars);
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

        if (inputName.getText() == null || inputName.getText().isEmpty())
            hotelToUpdate.setHotelName(fetchedHotel.getHotelName());
        else
            hotelToUpdate.setHotelName(inputName.getText());

        if (choiceBoxNumbOfStars.getValue() == null)
            hotelToUpdate.setNumberOfStars(fetchedHotel.getNumberOfStars());
        else
            hotelToUpdate.setNumberOfStars(choiceBoxNumbOfStars.getValue());

        if (inputAddress.getText() == null || inputAddress.getText().isEmpty())
            hotelToUpdate.setAddress(fetchedHotel.getAddress());
        else
            hotelToUpdate.setAddress(inputAddress.getText());

        if (inputZipcode.getText() == null || inputZipcode.getText().isEmpty())
            hotelToUpdate.setZipcode(fetchedHotel.getZipcode());
        else
            hotelToUpdate.setZipcode(inputZipcode.getText());

        if (inputCity.getText() == null || inputCity.getText().isEmpty())
            hotelToUpdate.setCity(fetchedHotel.getCity());
        else
            hotelToUpdate.setCity(inputCity.getText());

        if (inputCountry.getText() == null || inputCountry.getText().isEmpty())
            hotelToUpdate.setCountry(fetchedHotel.getCountry());
        else
            hotelToUpdate.setCountry(inputCountry.getText());


        return hotelToUpdate;
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }

}
