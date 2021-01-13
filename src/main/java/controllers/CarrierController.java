package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.CarrierService;
import backend.model.Carrier;
import backend.tabledata.CarrierData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static utils.Constants.*;

public class CarrierController {

    @FXML public TableView<CarrierData> tableviewCarriers;
    @FXML public TableColumn<CarrierData, Integer> tableCarrierId;
    @FXML public TableColumn<CarrierData, String> tableName;
    @FXML public TableColumn<CarrierData, String> tablePhoneNumber;
    @FXML public TableColumn<CarrierData, String> tableEmail;
    @FXML public Label clockLabel;
    @FXML public Button buttonDeleteCarrier;
    @FXML public Button buttonEditCarrier;
    @FXML public Button buttonAddCarrier;

    private Clock clk;
    private final CarrierService carrierService = AgencyServiceGenerator.createService(CarrierService.class);
    private List<Carrier> allCarriers;
    private final ObservableList<CarrierData> observableCarriers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        fetchAllCarriers();
        setRowClickListener();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initTableColumns() {
        tableCarrierId.setCellValueFactory(new PropertyValueFactory<>("TableCarrierId"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("TableName"));
        tablePhoneNumber.setCellValueFactory(new PropertyValueFactory<>("TablePhoneNumber"));
        tableEmail.setCellValueFactory(new PropertyValueFactory<>("TableEmail"));
    }

    private void fetchAllCarriers() {
        var fetchCarriersCall = carrierService.getAll();
        fetchCarriersCall.enqueue(new Callback<List<Carrier>>() {
            @Override
            public void onResponse(Call<List<Carrier>> call, Response<List<Carrier>> response) {
                if (response.isSuccessful()) {
                    allCarriers = response.body();

                    if (allCarriers != null) {
                        observableCarriers.clear();
                        observableCarriers.addAll(transformToDataFormat(allCarriers));
                        Platform.runLater(() -> {
                            tableviewCarriers.setItems(observableCarriers);
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

    private List<CarrierData> transformToDataFormat(List<Carrier> allCarriers) {
        return allCarriers.stream()
                .map(carrier -> new CarrierData(carrier.getCarrierId(), carrier.getName(), carrier.getPhoneNumber(), carrier.getEmail()))
                .collect(Collectors.toList());
    }

    private void setRowClickListener() {
        tableviewCarriers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null){
                buttonDeleteCarrier.setDisable(true);
                buttonEditCarrier.setDisable(true);
            } else {
                buttonDeleteCarrier.setDisable(false);
                buttonEditCarrier.setDisable(false);
            }
        });
    }


    @FXML
    public void onLogoutClick( ) {
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
    public void onAddCarrierClick() {
        Pane root;
        String fullPath = "fxml-files/AddCarrierScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllCarriers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditCarrierClick() {
        var selectedCarrierIndex = tableviewCarriers.getSelectionModel().getSelectedIndex();
        if (selectedCarrierIndex == -1) {
            return;
        }

        var selectedCarrierData = tableviewCarriers.getSelectionModel().getSelectedItem();
        Carrier selectedCarrier = allCarriers.stream().filter(carrier -> carrier.getCarrierId() == selectedCarrierData.getTableCarrierId()).findFirst().get();
        Main.setCarrier(selectedCarrier);

        Pane root;
        String fullPath = "fxml-files/EditCarrierScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllCarriers();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void onDeleteCarrierClick() {
        var selectedCarrierIndex = tableviewCarriers.getSelectionModel().getSelectedIndex();
        if (selectedCarrierIndex == -1) {
            return;
        }

        var selectedCarrierData = tableviewCarriers.getSelectionModel().getSelectedItem();
        Carrier selectedCarrier = allCarriers.stream().filter(carrier -> carrier.getCarrierId() == selectedCarrierData.getTableCarrierId()).findFirst().get();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Usunięcie przewoźnika");
        alert.setContentText("Usunięcie przewoźnika może spowodować usunięcie powiązanego transportu. Czy na pewno chcesz usunąć przewoźnika?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                carrierService.delete(selectedCarrier).execute();
                fetchAllCarriers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void shutdown(){
        clk.terminate();
    }
}
