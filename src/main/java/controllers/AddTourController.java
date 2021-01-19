package controllers;

import backend.api.*;
import backend.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Constants.*;
import static utils.TravelUtils.*;

public class AddTourController {

    @FXML public TextField inputName;
    @FXML public ComboBox<String> comboBoxCountry;
    @FXML public TextField inputNumOfAvailableTickets;
    @FXML public TextField inputPrice;
    @FXML public DatePicker datePickerDepartureDate;
    @FXML public DatePicker datePickerArrivalDate;
    @FXML public ComboBox<Hotel> comboBoxHotel;
    @FXML public ComboBox<Transport> comboBoxTransport;
    @FXML public ComboBox<TourGuide> comboBoxTourGuide;
    @FXML public CheckComboBox<AdditionalService> checkComboBoxService;
    @FXML public CheckComboBox<Attraction> checkComboBoxAttraction;
    @FXML public ListView<String> listViewImages;

    @FXML public Button buttonCancel;
    @FXML public Button buttonConfirm;
    @FXML public Label labelError;

    private final HotelService hotelService = AgencyServiceGenerator.createService(HotelService.class);
    private final TransportService transportService = AgencyServiceGenerator.createService(TransportService.class);
    private final TourGuideService tourGuideService = AgencyServiceGenerator.createService(TourGuideService.class);
    private final AdditionalServiceService additionalServiceService = AgencyServiceGenerator.createService(AdditionalServiceService.class);
    private final AttractionService attractionService = AgencyServiceGenerator.createService(AttractionService.class);
    private final TourService tourService = AgencyServiceGenerator.createService(TourService.class);

    private List<Hotel> allHotels = new ArrayList<>();
    private final ObservableList<Transport> observableTransports = FXCollections.observableArrayList();
    private final ObservableList<TourGuide> observableTourGuides = FXCollections.observableArrayList();
    private final ObservableList<AdditionalService> observableAdditionalServices = FXCollections.observableArrayList();
    private final ObservableList<Attraction> observableAttractions = FXCollections.observableArrayList();

    private final String BALI = "Bali";
    private final String CHILE = "Chile";
    private final String CYPR = "Cypr";
    private final String DOMINIKANA = "Dominikana";
    private final String FRANCJA = "Francja";
    private final String HISZPANIA = "Hiszpania";
    private final String MADAGASKAR = "Madagaskar";
    private final String NIEMCY = "Niemcy";
    private final String OAHU = "Oahu";
    private final String PORTUGALIA = "Portugalia";
    private final String SZWAJCARIA = "Szwajcaria";
    private final String WIELKABRYTANIA = "Wielka_Brytania";
    private final String WIETNAM = "Wietnam";
    private final String WLOCHY = "Wlochy";

    private final String[] imageNamesArray = {BALI, CHILE, CYPR, DOMINIKANA, FRANCJA, HISZPANIA, MADAGASKAR,
            NIEMCY, OAHU, PORTUGALIA, SZWAJCARIA, WIELKABRYTANIA, WIETNAM, WLOCHY};
    private final ObservableList<String> observableImages = FXCollections.observableArrayList(imageNamesArray);


    @FXML
    public void initialize() {
        initCountryComboBox();
        setObjectConvertersInComboBoxes();
        fetchAllNecessaryData();
        setImagesIntoListView();
        setTextFieldsListeners();
    }

    private void initCountryComboBox() {
        comboBoxCountry.getItems().setAll(imageNamesArray);
        comboBoxCountry.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                comboBoxHotel.getItems().clear();
            } else {
                List<Hotel> filteredHotels = allHotels.stream().filter(hot -> hot.getCountry().equals(newValue)).collect(Collectors.toList());
                comboBoxHotel.getItems().setAll(filteredHotels);
            }
        });

        datePickerDepartureDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });

        datePickerArrivalDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
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
                    allHotels = response.body();
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

    private void setImagesIntoListView() {
        listViewImages.setItems(observableImages);

        listViewImages.setCellFactory(param -> new ListCell<String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setFitWidth(128.0);
                    imageView.setFitHeight(128.0);

                    switch (name) {
                        case BALI -> imageView.setImage(new Image("/images/Bali.jpg"));
                        case CHILE -> imageView.setImage(new Image("/images/Chile.jpg"));
                        case CYPR -> imageView.setImage(new Image("/images/Cypr.jpg"));
                        case DOMINIKANA -> imageView.setImage(new Image("/images/Dominikana.jpg"));
                        case FRANCJA -> imageView.setImage(new Image("/images/Francja.jpg"));
                        case HISZPANIA -> imageView.setImage(new Image("/images/Hiszpania.jpg"));
                        case MADAGASKAR -> imageView.setImage(new Image("/images/Madagaskar.jpg"));
                        case NIEMCY -> imageView.setImage(new Image("/images/Niemcy.jpg"));
                        case OAHU -> imageView.setImage(new Image("/images/Oahu.jpg"));
                        case PORTUGALIA -> imageView.setImage(new Image("/images/Portugalia.jpg"));
                        case SZWAJCARIA -> imageView.setImage(new Image("/images/Szwajcaria.jpg"));
                        case WIELKABRYTANIA -> imageView.setImage(new Image("/images/Wielka_Brytania.jpg"));
                        case WIETNAM -> imageView.setImage(new Image("/images/Wietnam.jpg"));
                        case WLOCHY -> imageView.setImage(new Image("/images/Wlochy.jpg"));
                    }

                    setText(name);
                    setGraphic(imageView);
                    setFont(Font.font(20.0));
                }
            }
        });
    }

    private void setTextFieldsListeners() {
        inputNumOfAvailableTickets.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (inputNumOfAvailableTickets.getText().length() > 3) {
                    String sub = inputNumOfAvailableTickets.getText().substring(0, 3);
                    inputNumOfAvailableTickets.setText(sub);
                } else {
                    try {
                        Integer.parseInt(newValue);
                        labelError.setText("");
                    } catch (NumberFormatException e) {
                        labelError.setText(ERROR_NOT_A_NUMBER);
                    }
                }

            } else {
                labelError.setText("");
            }
        });

        inputPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (inputPrice.getText().length() > 9) {
                    String sub = inputPrice.getText().substring(0, 9);
                    inputPrice.setText(sub);
                } else {
                    try {
                        Double.parseDouble(newValue);
                        labelError.setText("");
                    } catch (NumberFormatException e) {
                        labelError.setText(ERROR_NOT_A_NUMBER);
                    }
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
        if (viewsAreNotEmpty() && viewsAreCorrect()) {
            if (secondDateIsLaterThanFirstOne()) {
                Tour newTour = collectValuesFromViewsAndCreateNewTour();

                var saveTourCall = tourService.save(newTour);
                saveTourCall.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful()) {
                            Platform.runLater(() -> {
                                closeWindow();
                            });
                        } else  {
                            Platform.runLater(() -> {
                                labelError.setText("Problem z serwerem, kod = " + response.code());
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable throwable) {
                        labelError.setText("Error has occurred " +throwable.getMessage());
                    }
                });
            } else {
                labelError.setText(ERROR_WRONG_DATES);
            }


        } else {
            labelError.setText(ERROR_EMPTY_VIEW);
        }
    }

    private boolean viewsAreNotEmpty() {
        return inputName.getText() != null && !inputName.getText().isEmpty()
                && comboBoxCountry.getSelectionModel().getSelectedItem() != null && !comboBoxCountry.getSelectionModel().isEmpty()
                && inputPrice.getText() != null && !inputPrice.getText().isEmpty()
                && inputNumOfAvailableTickets.getText() != null && !inputNumOfAvailableTickets.getText().isEmpty()
                && datePickerDepartureDate.getValue() != null && datePickerArrivalDate.getValue() != null
                && !comboBoxHotel.getSelectionModel().isEmpty() && !comboBoxTransport.getSelectionModel().isEmpty()
                && !comboBoxTourGuide.getSelectionModel().isEmpty() && !checkComboBoxService.getCheckModel().isEmpty()
                && !checkComboBoxAttraction.getCheckModel().isEmpty() && !listViewImages.getSelectionModel().isEmpty();
    }

    private boolean viewsAreCorrect() {
        return isItANumber(inputNumOfAvailableTickets) && isItAFloatNumber(inputPrice);
    }

    private boolean isItANumber(TextField textField) {
        try {
            Integer.parseInt(textField.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isItAFloatNumber(TextField textField) {
        try {
            Double.parseDouble(textField.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean secondDateIsLaterThanFirstOne() {
        var depDate = datePickerDepartureDate.getValue();
        var arrDate = datePickerArrivalDate.getValue();

        return depDate.isBefore(arrDate) && !depDate.equals(arrDate);

    }

    private Tour collectValuesFromViewsAndCreateNewTour() {
        Tour tour = new Tour();

        tour.setTourId(0);
        tour.setTourName(inputName.getText());
        tour.setCountry(comboBoxCountry.getSelectionModel().getSelectedItem());
        tour.setAvailableTickets(Integer.parseInt(inputNumOfAvailableTickets.getText()));
        tour.setTakenTickets(0);
        tour.setPrice(roundOffStrToDouble(inputPrice.getText()));
        tour.setDepartureDate(datePickerDepartureDate.getValue());
        tour.setArrivalDate(datePickerArrivalDate.getValue());
        tour.setImgName(listViewImages.getSelectionModel().getSelectedItem());
        tour.setEmployee(Main.getEmployee());
        tour.setHotel(comboBoxHotel.getSelectionModel().getSelectedItem());
        tour.setTransport(comboBoxTransport.getSelectionModel().getSelectedItem());
        tour.setTourGuide(comboBoxTourGuide.getSelectionModel().getSelectedItem());

        var selectedAdditionalServices = checkComboBoxService.getCheckModel().getCheckedItems();
        var selectedAttractions = checkComboBoxAttraction.getCheckModel().getCheckedItems();

        tour.setAdditionalServices(new HashSet<>(selectedAdditionalServices));
        tour.setAttractions(new HashSet<>(selectedAttractions));

        return tour;
    }

    private void closeWindow() {
        Stage currentStage = (Stage) buttonConfirm.getScene().getWindow();
        currentStage.close();
    }
}
