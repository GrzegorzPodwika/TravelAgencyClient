package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.HotelService;
import backend.model.Hotel;
import backend.tabledata.HotelData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Clock;
import utils.SceneCreator;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Constants.EMPLOYEE_SCENE;
import static utils.Constants.LOGIN_SCENE;

public class HotelController {

    @FXML public TableView<HotelData> tableviewHotel;
    @FXML public TableColumn<HotelData, Integer> tableHotelId;
    @FXML public TableColumn<HotelData, String> tableName;
    @FXML public TableColumn<HotelData, Integer> tableNumberOfStars;
    @FXML public TableColumn<HotelData, String> tableAddress;
    @FXML public TableColumn<HotelData, String> tableZipcode;
    @FXML public TableColumn<HotelData, String> tableCity;
    @FXML public TableColumn<HotelData, String> tableCountry;
    @FXML public Button buttonDeleteHotel;
    @FXML public Button buttonEditHotel;
    @FXML public Button buttonAddHotel;
    @FXML public Label clockLabel;

    private Clock clk;
    private final HotelService hotelService = AgencyServiceGenerator.createService(HotelService.class);
    private List<Hotel> allHotels;
    private final ObservableList<HotelData> observableHotels = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        fetchAllHotels();
        setRowClickListener();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initTableColumns() {
        tableHotelId.setCellValueFactory(new PropertyValueFactory<>("TableHotelId"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("TableName"));
        tableNumberOfStars.setCellValueFactory(new PropertyValueFactory<>("TableNumberOfStars"));
        tableAddress.setCellValueFactory(new PropertyValueFactory<>("TableAddress"));
        tableZipcode.setCellValueFactory(new PropertyValueFactory<>("TableZipcode"));
        tableCity.setCellValueFactory(new PropertyValueFactory<>("TableCity"));
        tableCountry.setCellValueFactory(new PropertyValueFactory<>("TableCountry"));
    }

    private void fetchAllHotels() {
        var fetchHotelsCall = hotelService.getAll();
        fetchHotelsCall.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (response.isSuccessful()) {
                    allHotels = response.body();

                    if (allHotels != null) {
                        observableHotels.clear();
                        observableHotels.addAll(transformToDataFormat(allHotels));
                        Platform.runLater(() -> {
                            tableviewHotel.setItems(observableHotels);
                        });
                    }
                } else {
                    System.out.println("List of carriers == null");
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private List<HotelData> transformToDataFormat(List<Hotel> hotels) {
        return hotels.stream()
                .map(hotel -> new HotelData(hotel.getHotelId(), hotel.getHotelName(), hotel.getNumberOfStars(),
                        hotel.getAddress(), hotel.getZipcode(), hotel.getCity(), hotel.getCountry()))
                .collect(Collectors.toList());
    }

    private void setRowClickListener() {
        tableviewHotel.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null){
                buttonDeleteHotel.setDisable(true);
                buttonEditHotel.setDisable(true);
            } else {
                buttonDeleteHotel.setDisable(false);
                buttonEditHotel.setDisable(false);
            }
        });
    }

    @FXML
    public void onLogoutClick() {
        Main.setEmployee(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void onGoBackClick() {
        SceneCreator.launchScene(EMPLOYEE_SCENE);
        shutdown();
    }

    @FXML
    public void onDeleteHotelClick() {
        int selectedIndex = tableviewHotel.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1)
            return;

        var selectedHotelData = tableviewHotel.getSelectionModel().getSelectedItem();
        Hotel selectedHotel = allHotels.stream().filter(hotel -> hotel.getHotelId() == selectedHotelData.getTableHotelId()).findFirst().get();

        try {
            hotelService.delete(selectedHotel).execute();
            fetchAllHotels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditHotelClick() {
        int selectedIndex = tableviewHotel.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1)
            return;

        var selectedHotelData = tableviewHotel.getSelectionModel().getSelectedItem();
        Hotel selectedHotel = allHotels.stream().filter(hotel -> hotel.getHotelId() == selectedHotelData.getTableHotelId()).findFirst().get();
        Main.setHotel(selectedHotel);

        Pane root;
        String fullPath = "fxml-files/EditHotelScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllHotels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddHotelClick() {
        Pane root;
        String fullPath = "fxml-files/AddHotelScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllHotels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        clk.terminate();
    }

}
