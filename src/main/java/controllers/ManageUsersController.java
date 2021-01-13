package controllers;

import backend.api.AgencyServiceGenerator;
import backend.api.UserService;
import backend.model.User;
import backend.tabledata.UserData;
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

import static utils.Constants.EMPLOYEE_SCENE;
import static utils.Constants.LOGIN_SCENE;

public class ManageUsersController {
    @FXML public TableView<UserData> tableviewUsers;
    @FXML public TableColumn<UserData, Integer> tableUserId;
    @FXML public TableColumn<UserData, String> tableName;
    @FXML public TableColumn<UserData, String> tableSurname;
    @FXML public TableColumn<UserData, Integer> tableAge;
    @FXML public TableColumn<UserData, String> tablePhone;
    @FXML public TableColumn<UserData, String> tableEmail;
    @FXML public Button buttonDeleteUser;
    @FXML public Button buttonEditUser;
    @FXML public Label clockLabel;

    private Clock clk;
    private final UserService userService = AgencyServiceGenerator.createService(UserService.class);
    private List<User> allUsers;
    private final ObservableList<UserData> observableUsers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initClock();
        initTableColumns();
        fetchAllUsers();
        setRowClickListener();
    }

    private void initClock() {
        clk = new Clock(clockLabel);
        Thread th = new Thread(clk);
        th.start();
    }

    private void initTableColumns() {
        tableUserId.setCellValueFactory(new PropertyValueFactory<>("TableUserId"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("TableName"));
        tableSurname.setCellValueFactory(new PropertyValueFactory<>("TableSurname"));
        tableAge.setCellValueFactory(new PropertyValueFactory<>("TableAge"));
        tablePhone.setCellValueFactory(new PropertyValueFactory<>("TablePhone"));
        tableEmail.setCellValueFactory(new PropertyValueFactory<>("TableEmail"));
    }

    private void fetchAllUsers() {
        var fetchUsersCall = userService.getAll();
        fetchUsersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    allUsers = response.body();

                    if (allUsers != null) {
                        observableUsers.clear();
                        observableUsers.addAll(transformToDataFormat(allUsers));
                        Platform.runLater(() -> {
                            tableviewUsers.setItems(observableUsers);
                        });
                    }
                } else {
                    System.out.println("List of carriers == null");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable throwable) {
                System.out.println("Error has occurred" + throwable.getMessage());
            }
        });
    }

    private List<UserData> transformToDataFormat(List<User> allUsers) {
        return allUsers.stream()
                .map(user -> new UserData(user.getPersonId(), user.getName(), user.getSurname(), user.getAge(), user.getPhoneNumber(), user.getEmail()))
                .collect(Collectors.toList());
    }

    private void setRowClickListener() {
        tableviewUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null){
                buttonDeleteUser.setDisable(true);
                buttonEditUser.setDisable(true);
            } else {
                buttonDeleteUser.setDisable(false);
                buttonEditUser.setDisable(false);
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
    public void onDeleteUserClick() {
        if (tableviewUsers.getSelectionModel().getSelectedIndex() == -1)
            return;

        var selectedUserData = tableviewUsers.getSelectionModel().getSelectedItem();
        User selectedUser = allUsers.stream().filter(user -> user.getPersonId() == selectedUserData.getTableUserId()).findFirst().get();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Usunięcie klienta");
        alert.setContentText("Usunięcie klienta spowoduje usunięcie wszystkich informacji powiązanych z klientem, jak rezerwacje i  płatności." +
                " Czy na pewno chcesz usunąć klienta?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                userService.delete(selectedUser).execute();
                fetchAllUsers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void onEditUserClick() {
        if (tableviewUsers.getSelectionModel().getSelectedIndex() == -1)
            return;

        var selectedUserData = tableviewUsers.getSelectionModel().getSelectedItem();
        User selectedUser = allUsers.stream().filter(user -> user.getPersonId() == selectedUserData.getTableUserId()).findFirst().get();
        Main.setUser(selectedUser);

        Pane root;
        String fullPath = "fxml-files/EditUserAsEmployeeScene.fxml";

        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fullPath));
            root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();

            fetchAllUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shutdown(){
        clk.terminate();
    }

}
