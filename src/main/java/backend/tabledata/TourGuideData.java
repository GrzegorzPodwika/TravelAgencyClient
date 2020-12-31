package backend.tabledata;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TourGuideData {
    private final SimpleIntegerProperty tableTourGuideId;
    private final SimpleStringProperty tableName;
    private final SimpleStringProperty tableSurname;
    private final SimpleStringProperty tablePhoneNumber;

    public TourGuideData(Integer tableTourGuideId, String tableName, String tableSurname, String tablePhoneNumber) {
        this.tableTourGuideId = new SimpleIntegerProperty(tableTourGuideId);
        this.tableName = new SimpleStringProperty(tableName);
        this.tableSurname = new SimpleStringProperty(tableSurname);
        this.tablePhoneNumber = new SimpleStringProperty(tablePhoneNumber);
    }

    public int getTableTourGuideId() {
        return tableTourGuideId.get();
    }

    public SimpleIntegerProperty tableTourGuideIdProperty() {
        return tableTourGuideId;
    }

    public void setTableTourGuideId(int tableTourGuideId) {
        this.tableTourGuideId.set(tableTourGuideId);
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

    public String getTablePhoneNumber() {
        return tablePhoneNumber.get();
    }

    public SimpleStringProperty tablePhoneNumberProperty() {
        return tablePhoneNumber;
    }

    public void setTablePhoneNumber(String tablePhoneNumber) {
        this.tablePhoneNumber.set(tablePhoneNumber);
    }
}
