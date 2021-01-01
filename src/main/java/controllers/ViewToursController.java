package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.TourService;
import backend.model.Tour;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
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

import static utils.Constants.LOGIN_SCENE;
import static utils.Constants.USER_SCENE;

public class ViewToursController {

    @FXML public ListView<String> listViewTours;
    @FXML public Button buttonShowTourDetails;
    @FXML public Label clockLabel;
    @FXML public Label labelError;

    private Clock clk;
    private final TourService tourService = AgencyServiceGenerator.createService(TourService.class);
    private List<Tour> allTours;
    private final ObservableList<String> observableImagesNames = FXCollections.observableArrayList();

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

    @FXML
    public void initialize() {
        initClock();
        initListView();
        fetchTours();
        setImagesIntoListView();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initListView() {
        listViewTours.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void fetchTours() {
        var fetchToursCall = tourService.getAll();
        fetchToursCall.enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                if (response.isSuccessful()) {
                    allTours = response.body();

                    if(allTours != null) {
                        List<String> fetchedImagesNames = new ArrayList<>();

                        for (Tour tour: allTours) {
                            fetchedImagesNames.add(tour.getImgName() + ", " + tour.getPrice() + " PLN " + tour.getDepartureDate().toString() + "-" + tour.getArrivalDate().toString());
                        }

                        Platform.runLater(() -> {
                            observableImagesNames.clear();
                            observableImagesNames.addAll(fetchedImagesNames);
                            listViewTours.setItems(observableImagesNames);
                        });
                    } else
                        System.out.println("Fetched list of tours == null!");
                } else {
                    System.out.println("Response was not successful, code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private void setImagesIntoListView() {
        listViewTours.setCellFactory(param -> new ListCell<String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setFitWidth(256.0);
                    imageView.setFitHeight(144.0);

                    //TODO czesc substringa przed przecinkiem
                    //String imgName = name.
                    int indexOfComma = name.indexOf(',');
                    String imgName = name.substring(0, indexOfComma);

                    switch (imgName) {
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

    @FXML
    public void onLogoutClick() {
        Main.setUser(null);
        SceneCreator.launchScene(LOGIN_SCENE);
        shutdown();
    }

    @FXML
    public void onGoBackClick() {
        SceneCreator.launchScene(USER_SCENE);
        shutdown();
    }

    @FXML
    public void onShowTourDetailClick() {
        if (listViewTours.getSelectionModel().getSelectedIndex() != -1) {
            labelError.setText("");
            var selectedTourIndex = listViewTours.getSelectionModel().getSelectedIndex();
            Main.setActiveTour(allTours.get(selectedTourIndex));

            Pane root;
            String fullPath = "fxml-files/ViewTourDetailsScene.fxml";

            try {
                Stage newStage = new Stage();
                newStage.setResizable(false);
                FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
                root = loader.load();
                newStage.setScene(new Scene(root));
                newStage.showAndWait();

                fetchTours();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            labelError.setText("Musisz wybrać wycieczkę!");
        }

    }

    private void shutdown(){
        clk.terminate();
    }

}