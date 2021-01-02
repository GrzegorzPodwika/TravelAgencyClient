package controllers;

import backend.api.AdditionalServiceService;
import backend.api.AgencyServiceGenerator;
import backend.model.AdditionalService;
import backend.model.Carrier;
import backend.tabledata.AdditionalServiceData;
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
import java.util.stream.Collectors;

import static utils.Constants.*;

public class AdditionalServiceController {
    @FXML public Label clockLabel;
    @FXML public TableView<AdditionalServiceData> tableviewAdditionalServices;
    @FXML public TableColumn<AdditionalServiceData, Integer> tableAdditionalServiceId;
    @FXML public TableColumn<AdditionalServiceData, String> tableName;
    @FXML public TableColumn<AdditionalServiceData, String> tableDescription;
    @FXML public Button buttonDeleteAdditionalService;
    @FXML public Button buttonEditAdditionalService;
    @FXML public Button buttonAddAdditionalService;

    private final AdditionalServiceService additionalServiceService = AgencyServiceGenerator.createService(AdditionalServiceService.class);
    private Clock clk;
    private List<AdditionalService> allAdditionalServices;
    private final ObservableList<AdditionalServiceData> observableAdditionalServices = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        fetchAllServices();
        setRowClickListener();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initTableColumns() {
        tableAdditionalServiceId.setCellValueFactory(new PropertyValueFactory<>("TableAdditionalServiceId"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("TableName"));
        tableDescription.setCellValueFactory(new PropertyValueFactory<>("TableDescription"));
    }

    private void fetchAllServices() {
        var fetchAllServicesCall = additionalServiceService.getAll();
        fetchAllServicesCall.enqueue(new Callback<List<AdditionalService>>() {
            @Override
            public void onResponse(Call<List<AdditionalService>> call, Response<List<AdditionalService>> response) {
                if (response.isSuccessful()) {
                    allAdditionalServices = response.body();

                    if (allAdditionalServices != null) {
                        observableAdditionalServices.clear();
                        observableAdditionalServices.addAll(transformToDataFormat(allAdditionalServices));
                        Platform.runLater(() -> {
                            tableviewAdditionalServices.setItems(observableAdditionalServices);
                        });
                    } else {
                        System.out.println("List of additional services == null");

                    }
                } else {
                    System.out.println("Response was not successful, code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AdditionalService>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private List<AdditionalServiceData> transformToDataFormat(List<AdditionalService> services) {
        return services.stream()
                .map(service -> new AdditionalServiceData(service.getAdditionalServiceId(), service.getName(), service.getDescription()))
                .collect(Collectors.toList());
    }

    private void setRowClickListener() {
        tableviewAdditionalServices.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                buttonDeleteAdditionalService.setDisable(true);
                buttonEditAdditionalService.setDisable(true);
            } else {
                buttonDeleteAdditionalService.setDisable(false);
                buttonEditAdditionalService.setDisable(false);
            }
        });
    }

    @FXML
    public void onLogoutClick() {
        Main.setEmployee(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    public void onGoBackClick() {
        SceneCreator.launchScene(EMPLOYEE_SCENE);
        shutdown();
    }

    @FXML
    public void onAddServiceClick() {
        Pane root;
        String fullPath = "fxml-files/AddAdditionalServiceScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllServices();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditServiceClick() {
        int selectedServiceIndex = tableviewAdditionalServices.getSelectionModel().getSelectedIndex();
        if (selectedServiceIndex == -1)
            return;

        var selectedServiceData = tableviewAdditionalServices.getSelectionModel().getSelectedItem();
        AdditionalService selectedService = allAdditionalServices.stream().filter(service -> service.getAdditionalServiceId() == selectedServiceData.getTableAdditionalServiceId()).findFirst().get();
        Main.setAdditionalService(selectedService);

        Pane root;
        String fullPath = "fxml-files/EditAdditionalServiceScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllServices();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteServiceClick() {
        int selectedServiceIndex = tableviewAdditionalServices.getSelectionModel().getSelectedIndex();
        if (selectedServiceIndex == -1)
            return;

        var selectedServiceData = tableviewAdditionalServices.getSelectionModel().getSelectedItem();
        AdditionalService selectedService = allAdditionalServices.stream().filter(service -> service.getAdditionalServiceId() == selectedServiceData.getTableAdditionalServiceId()).findFirst().get();

        try {
            additionalServiceService.delete(selectedService).execute();
            fetchAllServices();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutdown() {
        clk.terminate();
    }
}
