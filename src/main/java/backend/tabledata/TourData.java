package backend.tabledata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class TourData {
    private final SimpleIntegerProperty tableTourId;
    private final SimpleStringProperty tableName;
    private final SimpleStringProperty tableCountry;
    private final SimpleIntegerProperty tableAvailableTickets;
    private final SimpleIntegerProperty tableTakenTickets;
    private final SimpleDoubleProperty tablePrice;
    private final SimpleObjectProperty<LocalDate> tableDepartureDate;
    private final SimpleObjectProperty<LocalDate> tableArrivalDate;

    public TourData(Integer tableTourId, String tableName,  String tableCountry, Integer tableAvailableTickets, Integer tableTakenTickets, Double tablePrice, LocalDate tableDepartureDate, LocalDate tableArrivalDate) {
        this.tableTourId = new SimpleIntegerProperty(tableTourId) ;
        this.tableName = new SimpleStringProperty(tableName);
        this.tableCountry = new SimpleStringProperty(tableCountry) ;
        this.tableAvailableTickets = new SimpleIntegerProperty(tableAvailableTickets);
        this.tableTakenTickets = new SimpleIntegerProperty(tableTakenTickets);
        this.tablePrice = new SimpleDoubleProperty(tablePrice);
        this.tableDepartureDate = new SimpleObjectProperty<>(tableDepartureDate);
        this.tableArrivalDate = new SimpleObjectProperty<>(tableArrivalDate);
    }

    public int getTableTourId() {
        return tableTourId.get();
    }

    public SimpleIntegerProperty tableTourIdProperty() {
        return tableTourId;
    }

    public void setTableTourId(int tableTourId) {
        this.tableTourId.set(tableTourId);
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

    public String getTableCountry() {
        return tableCountry.get();
    }

    public SimpleStringProperty tableCountryProperty() {
        return tableCountry;
    }

    public void setTableCountry(String tableCountry) {
        this.tableCountry.set(tableCountry);
    }

    public int getTableAvailableTickets() {
        return tableAvailableTickets.get();
    }

    public SimpleIntegerProperty tableAvailableTicketsProperty() {
        return tableAvailableTickets;
    }

    public void setTableAvailableTickets(int tableAvailableTickets) {
        this.tableAvailableTickets.set(tableAvailableTickets);
    }

    public int getTableTakenTickets() {
        return tableTakenTickets.get();
    }

    public SimpleIntegerProperty tableTakenTicketsProperty() {
        return tableTakenTickets;
    }

    public void setTableTakenTickets(int tableTakenTickets) {
        this.tableTakenTickets.set(tableTakenTickets);
    }

    public double getTablePrice() {
        return tablePrice.get();
    }

    public SimpleDoubleProperty tablePriceProperty() {
        return tablePrice;
    }

    public void setTablePrice(double tablePrice) {
        this.tablePrice.set(tablePrice);
    }

    public LocalDate getTableDepartureDate() {
        return tableDepartureDate.get();
    }

    public SimpleObjectProperty<LocalDate> tableDepartureDateProperty() {
        return tableDepartureDate;
    }

    public void setTableDepartureDate(LocalDate tableDepartureDate) {
        this.tableDepartureDate.set(tableDepartureDate);
    }

    public LocalDate getTableArrivalDate() {
        return tableArrivalDate.get();
    }

    public SimpleObjectProperty<LocalDate> tableArrivalDateProperty() {
        return tableArrivalDate;
    }

    public void setTableArrivalDate(LocalDate tableArrivalDate) {
        this.tableArrivalDate.set(tableArrivalDate);
    }
}
