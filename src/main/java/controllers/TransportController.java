package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.TransportService;
import backend.model.Transport;
import backend.tabledata.TransportData;
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

public class TransportController {

    @FXML public TableView<TransportData> tableviewTransports;
    @FXML public TableColumn<TransportData, Integer> tableTransportId;
    @FXML public TableColumn<TransportData, String> tableTransportType;
    @FXML public TableColumn<TransportData, Double> tableJourneyTime;
    @FXML public TableColumn<TransportData, String> tableJourneyDescription;
    @FXML public TableColumn<TransportData, String> tableCarrierName;
    @FXML public Label clockLabel;
    @FXML public Button buttonDeleteTransport;
    @FXML public Button buttonEditTransport;
    @FXML public Button buttonAddTransport;

    private Clock clk;
    private final TransportService transportService = AgencyServiceGenerator.createService(TransportService.class);
    private List<Transport> allTransports;
    private final ObservableList<TransportData> observableTransports = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        fetchAllTransports();
        setRowClickListener();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initTableColumns() {
        tableTransportId.setCellValueFactory(new PropertyValueFactory<>("TableTransportId"));
        tableTransportType.setCellValueFactory(new PropertyValueFactory<>("TableTransportType"));
        tableJourneyTime.setCellValueFactory(new PropertyValueFactory<>("TableJourneyTime"));
        tableJourneyDescription.setCellValueFactory(new PropertyValueFactory<>("TableJourneyDescription"));
        tableCarrierName.setCellValueFactory(new PropertyValueFactory<>("TableCarrierName"));
    }

    private void fetchAllTransports() {
        var fetchTransportsCall = transportService.getAll();
        fetchTransportsCall.enqueue(new Callback<List<Transport>>() {
            @Override
            public void onResponse(Call<List<Transport>> call, Response<List<Transport>> response) {
                if (response.isSuccessful()) {
                    allTransports = response.body();

                    if (allTransports != null) {
                        observableTransports.clear();
                        observableTransports.addAll(transformToDataFormat(allTransports));
                        Platform.runLater(() -> {
                            tableviewTransports.setItems(observableTransports);
                        });
                    } else {
                        System.out.println("List of transports == null");
                    }
                } else {
                    System.out.println("Response was not successful, code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Transport>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());

            }
        });
    }

    private List<TransportData> transformToDataFormat(List<Transport> transports) {
        return transports.stream()
                .map(transport -> new TransportData(transport.getTransportId(), transport.getTransportType(),
                        transport.getJourneyTime(), transport.getJourneyDescription(), transport.getCarrier().getName()))
                .collect(Collectors.toList());
    }

    private void setRowClickListener() {
        tableviewTransports.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null){
                buttonDeleteTransport.setDisable(true);
                buttonEditTransport.setDisable(true);
            } else {
                buttonDeleteTransport.setDisable(false);
                buttonEditTransport.setDisable(false);
            }
        });
    }

    @FXML
    public void onAddTransportClick() {
        Pane root;
        String fullPath = "fxml-files/AddTransportScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllTransports();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditTransportClick() {
        if (tableviewTransports.getSelectionModel().getSelectedIndex() == -1)
            return;

        var selectedTransportData = tableviewTransports.getSelectionModel().getSelectedItem();
        Transport selectedTransport = allTransports.stream()
                .filter(transport -> transport.getTransportId() == selectedTransportData.getTableTransportId()).findFirst().get();

        Main.setTransport(selectedTransport);
        Pane root;
        String fullPath = "fxml-files/EditTransportScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllTransports();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteTransportClick() {
        if (tableviewTransports.getSelectionModel().getSelectedIndex() == -1)
            return;

        var selectedTransportData = tableviewTransports.getSelectionModel().getSelectedItem();
        Transport selectedTransport = allTransports.stream()
                .filter(transport -> transport.getTransportId() == selectedTransportData.getTableTransportId()).findFirst().get();

        try {
            transportService.delete(selectedTransport).execute();
            fetchAllTransports();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onGoBackClick( ) {
        SceneCreator.launchScene(EMPLOYEE_SCENE);
        shutdown();
    }

    @FXML
    public void onLogoutClick() {
        Main.setEmployee(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    private void shutdown(){
        clk.terminate();
    }
}
