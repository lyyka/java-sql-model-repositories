package database;

import java.util.HashMap;
import java.util.Map;

public class DatabaseBrokerResultRow {
    Map<String, Object> row;
    
    public DatabaseBrokerResultRow() {
        this.row = new HashMap<>();
    }
    
    public Object getColumnValue(String columnName) {
        return this.row.get(columnName);
    }
    
    public void put(String columnName, Object columnValue) {
        this.row.put(columnName, columnValue);
    }
}
