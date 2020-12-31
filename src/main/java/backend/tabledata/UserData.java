package backend.tabledata;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserData {
    private final SimpleIntegerProperty tableUserId;
    private final SimpleStringProperty tableName;
    private final SimpleStringProperty tableSurname;
    private final SimpleIntegerProperty tableAge;
    private final SimpleStringProperty tablePhone;
    private final SimpleStringProperty tableEmail;

    public UserData(Integer tableUserId, String tableName, String tableSurname, Integer tableAge, String tablePhone, String tableEmail) {
        this.tableUserId = new SimpleIntegerProperty(tableUserId);
        this.tableName = new SimpleStringProperty(tableName);
        this.tableSurname = new SimpleStringProperty(tableSurname);
        this.tableAge = new SimpleIntegerProperty(tableAge);
        this.tablePhone = new SimpleStringProperty(tablePhone);
        this.tableEmail = new SimpleStringProperty(tableEmail);
    }

    public int getTableUserId() {
        return tableUserId.get();
    }

    public SimpleIntegerProperty tableUserIdProperty() {
        return tableUserId;
    }

    public void setTableUserId(int tableUserId) {
        this.tableUserId.set(tableUserId);
    }

    public String getTableName() {
        return tableName.get();
    }

    public SimpleStringProperty tableNameProperty() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName.set(tableName);
    }

    public String getTableSurname() {
        return tableSurname.get();
    }

    public SimpleStringProperty tableSurnameProperty() {
        return tableSurname;
    }

    public void setTableSurname(String tableSurname) {
        this.tableSurname.set(tableSurname);
    }

    public int getTableAge() {
        return tableAge.get();
    }

    public SimpleIntegerProperty tableAgeProperty() {
        return tableAge;
    }

    public void setTableAge(int tableAge) {
        this.tableAge.set(tableAge);
    }

    public String getTablePhone() {
        return tablePhone.get();
    }

    public SimpleStringProperty tablePhoneProperty() {
        return tablePhone;
    }

    public void setTablePhone(String tablePhone) {
        this.tablePhone.set(tablePhone);
    }

    public String getTableEmail() {
        return tableEmail.get();
    }

    public SimpleStringProperty tableEmailProperty() {
        return tableEmail;
    }

    public void setTableEmail(String tableEmail) {
        this.tableEmail.set(tableEmail);
    }
}
