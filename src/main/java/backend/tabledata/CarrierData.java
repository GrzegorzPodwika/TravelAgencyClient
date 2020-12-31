package backend.tabledata;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CarrierData {
    private final SimpleIntegerProperty tableCarrierId;
    private final SimpleStringProperty tableName;
    private final SimpleStringProperty tablePhoneNumber;
    private final SimpleStringProperty tableEmail;

    public CarrierData(Integer carrierId, String name, String phoneNumber, String email) {
        this.tableCarrierId = new SimpleIntegerProperty(carrierId);
        this.tableName = new SimpleStringProperty(name);
        this.tablePhoneNumber = new SimpleStringProperty(phoneNumber);
        this.tableEmail = new SimpleStringProperty(email);
    }

    public int getTableCarrierId() {
        return tableCarrierId.get();
    }

    public SimpleIntegerProperty tableCarrierIdProperty() {
        return tableCarrierId;
    }

    public void setTableCarrierId(int tableCarrierId) {
        this.tableCarrierId.set(tableCarrierId);
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

    public String getTablePhoneNumber() {
        return tablePhoneNumber.get();
    }

    public SimpleStringProperty tablePhoneNumberProperty() {
        return tablePhoneNumber;
    }

    public void setTablePhoneNumber(String tablePhoneNumber) {
        this.tablePhoneNumber.set(tablePhoneNumber);
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
