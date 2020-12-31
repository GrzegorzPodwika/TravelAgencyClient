package backend.tabledata;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class HotelData {
    private final SimpleIntegerProperty tableHotelId;
    private final SimpleStringProperty tableName;
    private final SimpleIntegerProperty tableNumberOfStars;
    private final SimpleStringProperty tableAddress;
    private final SimpleStringProperty tableZipcode;
    private final SimpleStringProperty tableCity;
    private final SimpleStringProperty tableCountry;

    public HotelData(Integer tableHotelId, String tableName, Integer tableNumberOfStars, String tableAddress, String tableZipcode, String tableCity, String tableCountry) {
        this.tableHotelId = new SimpleIntegerProperty(tableHotelId);
        this.tableName = new SimpleStringProperty(tableName) ;
        this.tableNumberOfStars = new SimpleIntegerProperty(tableNumberOfStars);
        this.tableAddress = new SimpleStringProperty(tableAddress);
        this.tableZipcode = new SimpleStringProperty(tableZipcode);
        this.tableCity = new SimpleStringProperty(tableCity);
        this.tableCountry = new SimpleStringProperty(tableCountry);
    }

    public int getTableHotelId() {
        return tableHotelId.get();
    }

    public SimpleIntegerProperty tableHotelIdProperty() {
        return tableHotelId;
    }

    public void setTableHotelId(int tableHotelId) {
        this.tableHotelId.set(tableHotelId);
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

    public int getTableNumberOfStars() {
        return tableNumberOfStars.get();
    }

    public SimpleIntegerProperty tableNumberOfStarsProperty() {
        return tableNumberOfStars;
    }

    public void setTableNumberOfStars(int tableNumberOfStars) {
        this.tableNumberOfStars.set(tableNumberOfStars);
    }

    public String getTableAddress() {
        return tableAddress.get();
    }

    public SimpleStringProperty tableAddressProperty() {
        return tableAddress;
    }

    public void setTableAddress(String tableAddress) {
        this.tableAddress.set(tableAddress);
    }

    public String getTableZipcode() {
        return tableZipcode.get();
    }

    public SimpleStringProperty tableZipcodeProperty() {
        return tableZipcode;
    }

    public void setTableZipcode(String tableZipcode) {
        this.tableZipcode.set(tableZipcode);
    }

    public String getTableCity() {
        return tableCity.get();
    }

    public SimpleStringProperty tableCityProperty() {
        return tableCity;
    }

    public void setTableCity(String tableCity) {
        this.tableCity.set(tableCity);
    }

    public String getTableCountry() {
        return tableCountry.get();
    }

    public SimpleStringProperty tableCountryProperty() {
        return tableCountry;
    }

    public void setTableCountry(String tableCountry) {
        this.tableCountry.set(tableCountry);
    }
}
