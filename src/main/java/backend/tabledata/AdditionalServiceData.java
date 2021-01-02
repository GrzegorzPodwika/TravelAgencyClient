package backend.tabledata;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AdditionalServiceData {
    private final SimpleIntegerProperty tableAdditionalServiceId;
    private final SimpleStringProperty tableName;
    private final SimpleStringProperty tableDescription;

    public AdditionalServiceData(Integer tableAdditionalServiceId, String tableName, String tableDescription) {
        this.tableAdditionalServiceId = new SimpleIntegerProperty(tableAdditionalServiceId);
        this.tableName = new SimpleStringProperty(tableName);
        this.tableDescription = new SimpleStringProperty(tableDescription);
    }

    public int getTableAdditionalServiceId() {
        return tableAdditionalServiceId.get();
    }

    public SimpleIntegerProperty tableAdditionalServiceIdProperty() {
        return tableAdditionalServiceId;
    }

    public void setTableAdditionalServiceId(int tableAdditionalServiceId) {
        this.tableAdditionalServiceId.set(tableAdditionalServiceId);
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
