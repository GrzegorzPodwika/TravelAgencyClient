package backend.tabledata;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AttractionData {
    private final SimpleIntegerProperty tableAttractionId;
    private final SimpleStringProperty tableName;
    private final SimpleStringProperty tableDescription;

    public AttractionData(Integer tableAttractionId, String tableName, String tableDescription) {
        this.tableAttractionId = new SimpleIntegerProperty(tableAttractionId);
        this.tableName = new SimpleStringProperty(tableName);
        this.tableDescription = new SimpleStringProperty(tableDescription);
    }

    public int getTableAttractionId() {
        return tableAttractionId.get();
    }

    public SimpleIntegerProperty tableAttractionIdProperty() {
        return tableAttractionId;
    }

    public void setTableAttractionId(int tableAttractionId) {
        this.tableAttractionId.set(tableAttractionId);
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

    public String getTableDescription() {
        return tableDescription.get();
    }

    public SimpleStringProperty tableDescriptionProperty() {
        return tableDescription;
    }

    public void setTableDescription(String tableDescription) {
        this.tableDescription.set(tableDescription);
    }
}
