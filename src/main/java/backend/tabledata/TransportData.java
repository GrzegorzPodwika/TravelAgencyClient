package backend.tabledata;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TransportData {
    private final SimpleIntegerProperty tableTransportId;
    private final SimpleStringProperty tableTransportType;
    private final SimpleDoubleProperty tableJourneyTime;
    private final SimpleStringProperty tableJourneyDescription;
    private final SimpleStringProperty tableCarrierName;

    public TransportData(Integer tableTransportId, String tableTransportType, Double tableJourneyTime, String tableJourneyDescription, String tableCarrierName) {
        this.tableTransportId = new SimpleIntegerProperty(tableTransportId);
        this.tableTransportType = new SimpleStringProperty(tableTransportType);
        this.tableJourneyTime = new SimpleDoubleProperty(tableJourneyTime);
        this.tableJourneyDescription = new SimpleStringProperty(tableJourneyDescription);
        this.tableCarrierName = new SimpleStringProperty(tableCarrierName);
    }


    public int getTableTransportId() {
        return tableTransportId.get();
    }

    public SimpleIntegerProperty tableTransportIdProperty() {
        return tableTransportId;
    }

    public void setTableTransportId(int tableTransportId) {
        this.tableTransportId.set(tableTransportId);
    }

    public String getTableTransportType() {
        return tableTransportType.get();
    }

    public SimpleStringProperty tableTransportTypeProperty() {
        return tableTransportType;
    }

    public void setTableTransportType(String tableTransportType) {
        this.tableTransportType.set(tableTransportType);
    }

    public double getTableJourneyTime() {
        return tableJourneyTime.get();
    }

    public SimpleDoubleProperty tableJourneyTimeProperty() {
        return tableJourneyTime;
    }

    public void setTableJourneyTime(double tableJourneyTime) {
        this.tableJourneyTime.set(tableJourneyTime);
    }

    public String getTableJourneyDescription() {
        return tableJourneyDescription.get();
    }

    public SimpleStringProperty tableJourneyDescriptionProperty() {
        return tableJourneyDescription;
    }

    public void setTableJourneyDescription(String tableJourneyDescription) {
        this.tableJourneyDescription.set(tableJourneyDescription);
    }

    public String getTableCarrierName() {
        return tableCarrierName.get();
    }

    public SimpleStringProperty tableCarrierNameProperty() {
        return tableCarrierName;
    }

    public void setTableCarrierName(String tableCarrierName) {
        this.tableCarrierName.set(tableCarrierName);
    }
}
