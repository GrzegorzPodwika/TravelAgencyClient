package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.AttractionService;
import backend.model.Attraction;
import backend.tabledata.AttractionData;
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

public class AttractionController {
    @FXML public Label clockLabel;
    @FXML public TableView<AttractionData> tableviewAttractions;
    @FXML public TableColumn<AttractionData, Integer> tableAttractionId;
    @FXML public TableColumn<AttractionData, String> tableName;
    @FXML public TableColumn<AttractionData, String> tableDescription;
    @FXML public Button buttonDeleteAttraction;
    @FXML public Button buttonEditAttraction;
    @FXML public Button buttonAddAttraction;

    private final AttractionService attractionService = AgencyServiceGenerator.createService(AttractionService.class);
    private Clock clk;
    private List<Attraction> allAttractions;
    private final ObservableList<AttractionData> observableAttractions = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        fetchAllAttractions();
        setRowClickListener();
    }

    private void initTableColumns() {
        tableAttractionId.setCellValueFactory(new PropertyValueFactory<>("TableAttractionId"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("TableName"));
        tableDescription.setCellValueFactory(new PropertyValueFactory<>("TableDescription"));
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void fetchAllAttractions() {
        var fetchAllAttractionsCall = attractionService.getAll();
        fetchAllAttractionsCall.enqueue(new Callback<List<Attraction>>() {
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                if (response.isSuccessful()) {
                    allAttractions = response.body();

                    if (allAttractions != null) {
                        observableAttractions.clear();
                        observableAttractions.addAll(transformToDataFormat(allAttractions));
                        Platform.runLater(() -> {
                            tableviewAttractions.setItems(observableAttractions);
                        });
                    } else {
                        System.out.println("List of attractions == null");

                    }
                } else {
                    System.out.println("Response was not successful, code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Attraction>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private List<AttractionData> transformToDataFormat(List<Attraction> attractions) {
        return attractions.stream()
                .map(attraction -> new AttractionData(attraction.getAttractionId(), attraction.getName(), attraction.getDescription()))
                .collect(Collectors.toList());
    }

    private void setRowClickListener() {
        tableviewAttractions.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                buttonDeleteAttraction.setDisable(true);
                buttonEditAttraction.setDisable(true);
            } else {
                buttonDeleteAttraction.setDisable(false);
                buttonEditAttraction.setDisable(false);
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
    public void onAddAttractionClick() {
        Pane root;
        String fullPath = "fxml-files/AddAttractionScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllAttractions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditAttractionClick() {
        int selectedAttractionIndex = tableviewAttractions.getSelectionModel().getSelectedIndex();
        if (selectedAttractionIndex == -1)
            return;

        var selectedAttractionData = tableviewAttractions.getSelectionModel().getSelectedItem();
        Attraction selectedAttraction = allAttractions.stream().filter(attraction -> attraction.getAttractionId() == selectedAttractionData.getTableAttractionId()).findFirst().get();
        Main.setAttraction(selectedAttraction);

        Pane root;
        String fullPath = "fxml-files/EditAttractionScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllAttractions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteAttractionClick() {
        int selectedAttractionIndex = tableviewAttractions.getSelectionModel().getSelectedIndex();
        if (selectedAttractionIndex == -1)
            return;

        var selectedAttractionData = tableviewAttractions.getSelectionModel().getSelectedItem();
        Attraction selectedAttraction = allAttractions.stream().filter(attraction -> attraction.getAttractionId() == selectedAttractionData.getTableAttractionId()).findFirst().get();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Usunięcie atrakcji");
        alert.setContentText("Usunięcie atrakcji spowoduje usunięcie wszystkich informacji o tej atrakcji powiązanej z wieloma wycieczkami." +
                " Czy na pewno chcesz usunąć atrakcje?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                attractionService.delete(selectedAttraction).execute();
                fetchAllAttractions();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void shutdown() {
        clk.terminate();
    }
}
