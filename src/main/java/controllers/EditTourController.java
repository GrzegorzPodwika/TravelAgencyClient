package controllers;

import backend.api.*;
import backend.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Constants.ERROR_NOT_A_NUMBER;

public class EditTourController {

    @FXML public Label labelTourName;
    @FXML public Label labelCountry;
    @FXML public Label labelAvailableTickets;
    @FXML public Label labelPrice;
    @FXML public Label labelDepartureDate;
    @FXML public Label labelArrivalDate;
    @FXML public Label labelHotel;
    @FXML public Label labelTransport;
    @FXML public Label labelTourGuide;
    @FXML public ListView<String> listViewTourServices;
    @FXML public ListView<String> listViewAttractions;

    @FXML public TextField inputName;
    @FXML public TextField inputCountry;
    @FXML public TextField inputNumOfAvailableTickets;
    @FXML public TextField inputPrice;
    @FXML public DatePicker datePickerDepartureDate;
    @FXML public DatePicker datePickerArrivalDate;
    @FXML public ComboBox<Hotel> comboBoxHotel;
    @FXML public ComboBox<Transport> comboBoxTransport;
    @FXML public ComboBox<TourGuide> comboBoxTourGuide;
    @FXML public CheckComboBox<AdditionalService> checkComboBoxService;
    @FXML public CheckComboBox<Attraction> checkComboBoxAttraction;

    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;
    @FXML public Label labelError;

    private final Tour activeTour = Main.getActiveTour();
    private final HotelService hotelService = AgencyServiceGenerator.createService(HotelService.class);
    private final TransportService transportService = AgencyServiceGenerator.createService(TransportService.class);
    private final TourGuideService tourGuideService = AgencyServiceGenerator.createService(TourGuideService.class);
    private final AdditionalServiceService additionalServiceService = AgencyServiceGenerator.createService(AdditionalServiceService.class);
    private final AttractionService attractionService = AgencyServiceGenerator.createService(AttractionService.class);
    private final TourService tourService = AgencyServiceGenerator.createService(TourService.class);

    private final ObservableList<Hotel> observableHotels = FXCollections.observableArrayList();
    private final ObservableList<Transport> observableTransports = FXCollections.observableArrayList();
    private final ObservableList<TourGuide> observableTourGuides = FXCollections.observableArrayList();
    private final ObservableList<AdditionalService> observableAdditionalServices = FXCollections.observableArrayList();
    private final ObservableList<Attraction> observableAttractions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        fillUpLabelsWithData();
        setObjectConvertersInComboBoxes();
        fetchAllNecessaryData();
        setTextFieldsListeners();
    }

    private void fillUpLabelsWithData() {
        labelTourName.setText(activeTour.getTourName());
        labelCountry.setText(activeTour.getCountry());
        labelAvailableTickets.setText(String.valueOf(activeTour.getAvailableTickets()));
        labelPrice.setText(String.valueOf(activeTour.getPrice()));
        labelDepartureDate.setText(activeTour.getDepartureDate().toString());
        labelArrivalDate.setText(activeTour.getArrivalDate().toString());
        labelHotel.setText(activeTour.getHotel().getHotelName());
        labelTransport.setText(activeTour.getTransport().getCarrier().getName() + " " + activeTour.getTransport().getTransportType());
        labelTourGuide.setText(activeTour.getTourGuide().getName() + " " + activeTour.getTourGuide().getSurname());

        var listOfAttractionsAsString = activeTour.getAttractions().stream()
                .map(Attraction::getName).collect(Collectors.toList());
        var listOfServicesAsString = activeTour.getAdditionalServices().stream()
                .map(AdditionalService::getName).collect(Collectors.toList());

        listViewAttractions.getItems().setAll(listOfAttractionsAsString);
        listViewTourServices.getItems().setAll(listOfServicesAsString);

        listViewAttractions.setCellFactory(cell -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean b) {
                super.updateItem(item, b);

                if (item != null) {
                    setText(item);
                    setFont(Font.font(14));
                }
            }
        });
        listViewTourServices.setCellFactory(cell -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean b) {
                super.updateItem(item, b);

                if (item != null) {
                    setText(item);
                    setFont(Font.font(14));
                }
            }
        });
    }


    private void setObjectConvertersInComboBoxes() {
        comboBoxHotel.setConverter(new StringConverter<Hotel>() {
            @Override
            public String toString(Hotel hotel) {
                return hotel.getHotelName();
            }

            @Override
            public Hotel fromString(String s) {
                return comboBoxHotel.getItems().stream()
                        .filter(hotel -> hotel.getHotelName().equals(s)).findFirst().orElse(null);
            }
        });

        comboBoxTransport.setConverter(new StringConverter<Transport>() {
            @Override
            public String toString(Transport transport) {
                return transport.getCarrier().getName();
            }

            @Override
            public Transport fromString(String s) {
                return comboBoxTransport.getItems().stream()
                        .filter(transport -> transport.getCarrier().getName().equals(s)).findFirst().orElse(null);
            }
        });

        comboBoxTourGuide.setConverter(new StringConverter<TourGuide>() {
            @Override
            public String toString(TourGuide tourGuide) {
                return tourGuide.getName() + " " + tourGuide.getSurname();
            }

            @Override
            public TourGuide fromString(String s) {
                return comboBoxTourGuide.getItems().stream()
                        .filter(guide -> (guide.getName() + " " + guide.getSurname()).equals(s)).findFirst().orElse(null);
            }
        });

        checkComboBoxService.setConverter(new StringConverter<AdditionalService>() {
            @Override
            public String toString(AdditionalService additionalService) {
                return additionalService.getName();
            }

            @Override
            public AdditionalService fromString(String s) {
                return checkComboBoxService.getItems().stream()
                        .filter(serv -> serv.getName().equals(s)).findFirst().orElse(null);
            }
        });

        checkComboBoxAttraction.setConverter(new StringConverter<Attraction>() {
            @Override
            public String toString(Attraction attraction) {
                return attraction.getName();
            }

            @Override
            public Attraction fromString(String s) {
                return checkComboBoxAttraction.getItems().stream()
                        .filter(attraction -> attraction.getName().equals(s)).findFirst().orElse(null);
            }
        });
    }

    private void fetchAllNecessaryData() {
        var fetchHotelsCall = hotelService.getAll();
        fetchHotelsCall.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful()) {
                    var hotels = response.body();

                    if (hotels != null) {
                        observableHotels.addAll(hotels);
                        Platform.runLater(() -> {
                            comboBoxHotel.setItems(observableHotels);
                        });
                    } else {
                        System.out.println("List of hotels == null");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                System.out.println("Error has occurred " + throwable.getMessage());
            }
        });

        var fetchTransportsCall = transportService.getAll();
        fetchTransportsCall.enqueue(new Callback<List<Transport>>() {
            @Override
            public void onResponse(Call<List<Transport>> call, Response<List<Transport>> response) {
                if (response.isSuccessful()) {
                    var transportList = response.body();

                    if (transportList != null) {
                        observableTransports.addAll(transportList);
                        Platform.runLater(() -> {
                            comboBoxTransport.setItems(observableTransports);
                        });
                    } else {
                        System.out.println("List of transport == null");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Transport>> call, Throwable throwable) {
                System.out.println("Error has occurred " + throwable.getMessage());

            }
        });

        var fetchTourGuidesCall = tourGuideService.getAll();
        fetchTourGuidesCall.enqueue(new Callback<List<TourGuide>>() {
            @Override
            public void onResponse(Call<List<TourGuide>> call, Response<List<TourGuide>> response) {
                if (response.isSuccessful()) {
                    var tourGuideList = response.body();

                    if (tourGuideList != null) {
                        observableTourGuides.addAll(tourGuideList);
                        Platform.runLater(() -> {
                            comboBoxTourGuide.setItems(observableTourGuides);
                        });
                    } else {
                        System.out.println("List of tour guides == null");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TourGuide>> call, Throwable throwable) {
                System.out.println("Error has occurred " + throwable.getMessage());
            }
        });

        var fetchAdditionalServices = additionalServiceService.getAll();
        fetchAdditionalServices.enqueue(new Callback<List<AdditionalService>>() {
            @Override
            public void onResponse(Call<List<AdditionalService>> call, Response<List<AdditionalService>> response) {
                if (response.isSuccessful()) {
                    var additionalServiceList = response.body();

                    if (additionalServiceList != null) {
                        observableAdditionalServices.addAll(additionalServiceList);
                        Platform.runLater(() -> {
                            checkComboBoxService.getItems().addAll(observableAdditionalServices);
                        });
                    } else {
                        System.out.println("List of additionalservices == null");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalService>> call, Throwable throwable) {
                System.out.println("Error has occurred " + throwable.getMessage());
            }
        });

        var fetchAttractionsCall = attractionService.getAll();
        fetchAttractionsCall.enqueue(new Callback<List<Attraction>>() {
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                if (response.isSuccessful()) {
                    var attractionList = response.body();

                    if (attractionList != null) {
                        observableAttractions.addAll(attractionList);
                        Platform.runLater(() -> {
                            checkComboBoxAttraction.getItems().addAll(observableAttractions);
                        });
                    } else {
                        System.out.println("List of attractions == null");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Attraction>> call, Throwable throwable) {
                System.out.println("Error has occurred " + throwable.getMessage());

            }
        });
    }

    private void setTextFieldsListeners() {
        inputNumOfAvailableTickets.textProperty().addListener((observable, oldValue, newValue) -> {
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

        inputPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    var isNumber = Double.parseDouble(newValue);
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
        Tour tourToUpdate = generateProperTour();

        try {
            tourService.update(tourToUpdate).execute();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tour generateProperTour() {
        Tour tourToUpdate = new Tour();
        tourToUpdate.setTourId(activeTour.getTourId());

        if (inputName.getText().isEmpty())
            tourToUpdate.setTourName(activeTour.getTourName());
        else
            tourToUpdate.setTourName(inputName.getText());

        if (inputCountry.getText().isEmpty())
            tourToUpdate.setCountry(activeTour.getCountry());
        else
            tourToUpdate.setCountry(inputCountry.getText());

        if(inputPrice.getText().isEmpty() || !isItAFloatNumber(inputPrice))
            tourToUpdate.setPrice(activeTour.getPrice());
        else
            tourToUpdate.setPrice(Double.parseDouble(inputPrice.getText()));

        if (inputNumOfAvailableTickets.getText().isEmpty() || !isItANumber(inputNumOfAvailableTickets))
            tourToUpdate.setAvailableTickets(activeTour.getAvailableTickets());
        else
            tourToUpdate.setAvailableTickets(Integer.parseInt(inputNumOfAvailableTickets.getText()));

        tourToUpdate.setTakenTickets(activeTour.getTakenTickets());

        if (datePickerDepartureDate.getValue() == null)
            tourToUpdate.setDepartureDate(activeTour.getDepartureDate());
        else
            tourToUpdate.setDepartureDate(datePickerDepartureDate.getValue());

        if (datePickerArrivalDate.getValue() == null)
            tourToUpdate.setArrivalDate(activeTour.getArrivalDate());
        else
            tourToUpdate.setArrivalDate(datePickerArrivalDate.getValue());

        tourToUpdate.setImgName(activeTour.getImgName());
        tourToUpdate.setEmployee(activeTour.getEmployee());

        if (comboBoxHotel.getSelectionModel().isEmpty())
            tourToUpdate.setHotel(activeTour.getHotel());
        else
            tourToUpdate.setHotel(comboBoxHotel.getSelectionModel().getSelectedItem());

        if (comboBoxTransport.getSelectionModel().isEmpty())
            tourToUpdate.setTransport(activeTour.getTransport());
        else
            tourToUpdate.setTransport(comboBoxTransport.getSelectionModel().getSelectedItem());

        if (comboBoxTourGuide.getSelectionModel().isEmpty())
            tourToUpdate.setTourGuide(activeTour.getTourGuide());
        else
            tourToUpdate.setTourGuide(comboBoxTourGuide.getSelectionModel().getSelectedItem());

        if (checkComboBoxAttraction.getCheckModel().isEmpty())
            tourToUpdate.setAttractions(activeTour.getAttractions());
        else
            tourToUpdate.setAttractions(new HashSet<>(checkComboBoxAttraction.getCheckModel().getCheckedItems()));

        if (checkComboBoxService.getCheckModel().isEmpty())
            tourToUpdate.setAdditionalServices(activeTour.getAdditionalServices());
        else
            tourToUpdate.setAdditionalServices(new HashSet<>(checkComboBoxService.getCheckModel().getCheckedItems()));


        return tourToUpdate;
    }

    private boolean isItANumber(TextField textField) {
        try {
            var isNumber = Integer.parseInt(textField.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isItAFloatNumber(TextField textField) {
        try {
            var isNumber = Double.parseDouble(textField.getText());
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
