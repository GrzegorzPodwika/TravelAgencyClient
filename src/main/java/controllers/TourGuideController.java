package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.TourGuideService;
import backend.model.TourGuide;
import backend.tabledata.TourGuideData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Constants.EMPLOYEE_SCENE;
import static utils.Constants.LOGIN_SCENE;

public class TourGuideController {

    @FXML public TableView<TourGuideData> tableviewTourGuides;
    @FXML public TableColumn<TourGuideData, Integer> tableTourGuideId;
    @FXML public TableColumn<TourGuideData, String> tableName;
    @FXML public TableColumn<TourGuideData, String> tableSurname;
    @FXML public TableColumn<TourGuideData, String> tablePhoneNumber;
    @FXML public Label clockLabel;
    @FXML public Button buttonDeleteTourGuide;
    @FXML public Button buttonEditTourGuide;
    @FXML public Button buttonAddTourGuide;

    private Clock clk;
    private final TourGuideService tourGuideService = AgencyServiceGenerator.createService(TourGuideService.class);
    private List<TourGuide> allTourGuides;
    private final ObservableList<TourGuideData> observableTourGuides = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        fetchAllTourGuides();
        setRowClickListener();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initTableColumns() {
        tableTourGuideId.setCellValueFactory(new PropertyValueFactory<>("TableTourGuideId"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("TableName"));
        tableSurname.setCellValueFactory(new PropertyValueFactory<>("TableSurname"));
        tablePhoneNumber.setCellValueFactory(new PropertyValueFactory<>("TablePhoneNumber"));
    }

    private void fetchAllTourGuides() {
        var fetchTourGuidesCall = tourGuideService.getAll();
        fetchTourGuidesCall.enqueue(new Callback<List<TourGuide>>() {
            @Override
            public void onResponse(Call<List<TourGuide>> call, Response<List<TourGuide>> response) {
                if (response.isSuccessful()) {
                    allTourGuides = response.body();

                    if (allTourGuides != null) {
                        observableTourGuides.clear();
                        observableTourGuides.addAll(transformToDataFormat(allTourGuides));
                        Platform.runLater( () -> {
                            tableviewTourGuides.setItems(observableTourGuides);
                        });
                    }
                } else {
                    System.out.println("List of tour guides == null");
                }
            }

            @Override
            public void onFailure(Call<List<TourGuide>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private List<TourGuideData> transformToDataFormat(List<TourGuide> allTourGuides) {
        return allTourGuides.stream()
                .map(guide -> new TourGuideData(guide.getTourGuideId(), guide.getName(), guide.getSurname(), guide.getPhoneNumber()))
                .collect(Collectors.toList());
    }

    private void setRowClickListener() {
        tableviewTourGuides.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null){
                buttonDeleteTourGuide.setDisable(true);
                buttonEditTourGuide.setDisable(true);
            } else {
                buttonDeleteTourGuide.setDisable(false);
                buttonEditTourGuide.setDisable(false);
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
    public void onDeleteTourGuideClick() {
        var selectedTourGuide = tableviewTourGuides.getSelectionModel().getSelectedItem();

        if (selectedTourGuide == null)
            return;

        TourGuide plainTourGuide = transformToPlainFormat(selectedTourGuide);

        try {
            tourGuideService.delete(plainTourGuide).execute();
            fetchAllTourGuides();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TourGuide transformToPlainFormat(TourGuideData selectedTourGuide) {
        return new TourGuide(selectedTourGuide.getTableTourGuideId(), selectedTourGuide.getTableName(),
                selectedTourGuide.getTableSurname(), selectedTourGuide.getTablePhoneNumber());
    }

    @FXML
    public void onEditTourGuideClick() {
        var selectedTourGuide = tableviewTourGuides.getSelectionModel().getSelectedItem();

        if (selectedTourGuide == null)
            return;

        TourGuide plainTourGuide = transformToPlainFormat(selectedTourGuide);
        Main.setTourGuide(plainTourGuide);

        Pane root;
        String fullPath = "fxml-files/EditTourGuideScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllTourGuides();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddTourGuideClick() {
        Pane root;
        String fullPath = "fxml-files/AddTourGuideScene.fxml";

        try {
            Stage newStage = new Stage();
            newStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllTourGuides();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shutdown(){
        clk.terminate();
    }
}
