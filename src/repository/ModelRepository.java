package repository;

import database.DatabaseBroker;
import database.DatabaseBrokerResultRow;
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
    
    abstract protected T getNewModelInstance();
    
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
    private void setModelDataFromResultRow(T modelToFill, DatabaseBrokerResultRow row)
    {
        Map<String, ModelAttributeValue> mapFieldNamesToValues = modelToFill.getRepository().mapDatabaseFields();
        
        // Set ID by default
        BigInteger id = (BigInteger) row.getColumnValue(modelToFill.getIdColumName());
        modelToFill.setId(id.longValue());
        modelToFill.setPersisted(true);
        
        for (Map.Entry<String,ModelAttributeValue> entry : mapFieldNamesToValues.entrySet())
        {
            // Get raw value
            Object valueToSet = row.getColumnValue(entry.getKey());
            
            // Auto-convert values to java-friendly types
            if(valueToSet instanceof java.sql.Date sqlDate) {
                valueToSet = new java.util.Date(sqlDate.getTime());
            }
            
            // Call a setter of `ModelAttributeValue`
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
            // Generate columns to be inserted
            columns = columns.concat(entry.getKey() + ",");
            
            // Generate a `?` for each column
            values = values.concat("?,");
            
            // Add value for later binding
            valuesToBind.add(entry.getValue().get());
        }
        
        // Trim the last `,` from columns & values strings
        columns = columns.substring(0, columns.length() - 1).concat(")");
        values = values.substring(0, values.length() - 1).concat(")");
        
        // Generate the query
        String query = "insert into " + this.model.tableName() + columns + " values" + values;
    
        // Pass to broker
        this.broker.executeUpdate(query, valuesToBind);
    }

    @Override
    public T getById(Long id) {
        String query = "select * from " + this.model.tableName() + " where " + this.model.getIdColumName() + " = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        
        DatabaseBrokerResultRow row = this.broker.getFirstResult(query, params);
        
        setModelDataFromResultRow(this.model, row);
        
        return this.model;
    }

    public List<Model> allModels() {
        String query = "select * from " + this.model.tableName();
        List<Object> params = new ArrayList<>();
        List<Model> result = new ArrayList<>();
        
        List<DatabaseBrokerResultRow> all = this.broker.getAllResults(query, params);
        
        for(DatabaseBrokerResultRow row : all) {
            T newModel = this.getNewModelInstance();
            
            setModelDataFromResultRow(newModel, row);
            
            result.add(newModel);
        }
        
        return result;
    }
    
    @Override
    public List<Object> all()
    {
        List<Object> res = new ArrayList<>();
        List<Model> models = this.allModels();
        
        for(Model m : models) {
            res.add(m);
        }
        
        return res;
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteById(Long id) {
        String query = "delete from " + this.model.tableName() + " where " + this.model.getIdColumName() + " = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        
        this.broker.executeUpdate(query, params);
    }
}
