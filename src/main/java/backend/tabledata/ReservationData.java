package backend.tabledata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class ReservationData {
    private final SimpleIntegerProperty tableReservationId;
    private final SimpleStringProperty tableStatus;
    private final SimpleIntegerProperty tableNumberOfTickets;
    private final SimpleDoubleProperty tableTotalPrice;
    private final SimpleObjectProperty<LocalDate>  tableReservationDate;
    private final SimpleObjectProperty<LocalDate> tableDeadlineDate;

    public ReservationData(Integer tableReservationId, String tableStatus, Integer tableNumberOfTickets, Double tableTotalPrice, LocalDate tableReservationDate, LocalDate tableDeadlineDate) {
        this.tableReservationId = new SimpleIntegerProperty(tableReservationId);
        this.tableStatus = new SimpleStringProperty(tableStatus);
        this.tableNumberOfTickets = new SimpleIntegerProperty(tableNumberOfTickets);
        this.tableTotalPrice = new SimpleDoubleProperty(tableTotalPrice);
        this.tableReservationDate = new SimpleObjectProperty<>(tableReservationDate);
        this.tableDeadlineDate = new SimpleObjectProperty<>(tableDeadlineDate);
    }

    public int getTableReservationId() {
        return tableReservationId.get();
    }

    public SimpleIntegerProperty tableReservationIdProperty() {
        return tableReservationId;
    }

    public void setTableReservationId(int tableReservationId) {
        this.tableReservationId.set(tableReservationId);
    }

    public String getTableStatus() {
        return tableStatus.get();
    }

    public SimpleStringProperty tableStatusProperty() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus.set(tableStatus);
    }

    public int getTableNumberOfTickets() {
        return tableNumberOfTickets.get();
    }

    public SimpleIntegerProperty tableNumberOfTicketsProperty() {
        return tableNumberOfTickets;
    }

    public void setTableNumberOfTickets(int tableNumberOfTickets) {
        this.tableNumberOfTickets.set(tableNumberOfTickets);
    }

    public double getTableTotalPrice() {
        return tableTotalPrice.get();
    }

    public SimpleDoubleProperty tableTotalPriceProperty() {
        return tableTotalPrice;
    }

    public void setTableTotalPrice(double tableTotalPrice) {
        this.tableTotalPrice.set(tableTotalPrice);
    }

    public LocalDate getTableReservationDate() {
        return tableReservationDate.get();
    }

    public SimpleObjectProperty<LocalDate> tableReservationDateProperty() {
        return tableReservationDate;
    }

    public void setTableReservationDate(LocalDate tableReservationDate) {
        this.tableReservationDate.set(tableReservationDate);
    }

    public LocalDate getTableDeadlineDate() {
        return tableDeadlineDate.get();
    }

    public SimpleObjectProperty<LocalDate> tableDeadlineDateProperty() {
        return tableDeadlineDate;
    }

    public void setTableDeadlineDate(LocalDate tableDeadlineDate) {
        this.tableDeadlineDate.set(tableDeadlineDate);
    }
}
