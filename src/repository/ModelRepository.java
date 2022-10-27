package repository;

import database.DatabaseBroker;
import database.Model;
import database.ModelAttributeValue;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class ModelRepository<T extends Model> implements IRepository {
    private final T model;
    private final DatabaseBroker broker;
    
    abstract public Map<String, ModelAttributeValue> mapDatabaseFields();
    
    public ModelRepository(T model)
    {
        this.model = model;
        this.broker = new DatabaseBroker();
    }
    
    /**
     * Loops through map, where keys are model data keys,
     * and values are values to be set
     * 
     * @param row 
     */
    private void setModelDataFromMap(Map<String, Object> row)
    {
        Map<String, ModelAttributeValue> mapFieldNamesToValues = this.mapDatabaseFields();
        
        this.model.setId(((BigInteger) row.get(this.model.getIdColumName())).longValue());
        for (Map.Entry<String,ModelAttributeValue> entry : mapFieldNamesToValues.entrySet())
        {
            Object valueToSet = row.get(entry.getKey());
            
            // Auto-convert values to java-friendly types
            if(valueToSet instanceof java.sql.Date sqlDate) {
                valueToSet = new java.util.Date(sqlDate.getTime());
            }
            
            entry.getValue().set(valueToSet);          
        }
    }

    @Override
    public void create() {
        String columns = "(";
        String values = "(";
        
        Map<String, ModelAttributeValue> mapFieldNamesToValues = this.mapDatabaseFields();
        List<Object> valuesToBind = new ArrayList<>();
        
        for (Map.Entry<String,ModelAttributeValue> entry : mapFieldNamesToValues.entrySet())
        {
            columns = columns.concat(entry.getKey() + ",");
            values = values.concat("?,");
            valuesToBind.add(entry.getValue().get());
        }
        
        columns = columns.substring(0, columns.length() - 1).concat(")");
        values = values.substring(0, values.length() - 1).concat(")");
        
        String query = "insert into " + this.model.tableName() + columns + " values" + values;
    
        this.broker.insert(query, valuesToBind);
    }

    @Override
    public T getById(Long id) {   
        String query = "select * from " + this.model.tableName() + " where " + this.model.getIdColumName() + " = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        
        Map<String, Object> row = this.broker.getFirstResult(query, params);
        
        setModelDataFromMap(row);
        
        return this.model;
    }

    @Override
    public List<Object> all() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
