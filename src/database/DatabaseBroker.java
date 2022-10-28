/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseBroker {
    private Connection connection;

    /**
     * Establishes a connection
     */
    public void connect() {
        String url = "jdbc:mysql://localhost/prosoft22";
        String username = "root";
        String password = "Root.123";
        
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Konekcija uspesno uspostavljena!");
        } catch (SQLException ex) {
            System.out.println("Greska prilikom uspostavljanja konekcije.");    
        }
    }
    
    /**
     * Checks if a connection is active or not
     * 
     * @return
     */
    private boolean isActive() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            System.out.println("Greska prilikom provere aktivnosti konekcije.");    
        }
        
        return false;
    }
    
    /**
     * Closes the connection, if active
     */
    public void closeConnection() {
        try {
            if(isActive()) {
                connection.close();
                System.out.println("Konekcija uspesno zatvorena!");
            }
        } catch (SQLException ex) {
            System.out.println("Greska prilikom zatvaranja konekcije.");    
        }
    }

    /**
     * Instantiates prepared statement based on the passed in query
     * 
     * @param query
     * @return 
     */
    public PreparedStatement getPreparedStatement(String query) {
        try {
            if(isActive()) {
                return connection.prepareStatement(query);
            }
        } catch (SQLException ex) {
            System.out.println("Greska prilikom kreiranja upita.");    
        }
        
        return null;
    }
    
    /**
     * For each of the parameters, in their order, binds the parameter to the statement
     * 
     * @param statement
     * @param parameters 
     */
    private void bindValuesToStatement(PreparedStatement statement, List<Object> parameters)
    {
        try {
            int i = 1;
            for(Object o : parameters) {
                statement.setObject(i, o);
                i++;
            }
            
            System.out.println("Uspesno vezane vrednosti.");
        } catch(SQLException ex) {
            System.out.println("Neuspesno vezivanje vrednosti.");
        }
    }
    
    /**
     * Taking into account currently active row in the ResultSet,
     * takes all objects from that row and forms a map.
     * 
     * Map represents a row, in which keys (column names) point to values (column values)
     * 
     * @param set
     * @return 
     */
    private DatabaseBrokerResultRow currentResultSetRowToDatabaseBrokerRow(ResultSet set)
    {
        DatabaseBrokerResultRow result = new DatabaseBrokerResultRow();
        
        try {
            // Get row meta data to fetch column names and count
            ResultSetMetaData resultSetMetaData = set.getMetaData();
            final int columnCount = resultSetMetaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                result.put(resultSetMetaData.getColumnName(i), set.getObject(i));
            }
        } catch(SQLException ex) {
            System.out.println("Greska prilikom konvertovanja set-a u listu. " + ex.getMessage());
        }
        
        return result;
    }
    
    /**
     * Performs an insert operation
     * 
     * @param query
     * @param parameters 
     */
    public void executeUpdate(String query, List<Object> parameters) {
        if(!isActive()) {
            connect();
        }
        
        PreparedStatement statement = this.getPreparedStatement(query);
        bindValuesToStatement(statement, parameters);

        try {            
            statement.executeUpdate();
            
            statement.close();
            
            System.out.println("Unos uspesan.");
        } catch(SQLException ex) {
            System.out.println("Greska prilikom unosa. " + ex.getMessage());
        }
        
        
        closeConnection();
    }
    
    /**
     * Gets all results from set
     * 
     * @param query
     * @param parameters
     * @return 
     */
    public List<DatabaseBrokerResultRow> getAllResults(String query, List<Object> parameters) {
        if(!isActive()) {
            connect();
        } 
        
        PreparedStatement statement = this.getPreparedStatement(query);
        bindValuesToStatement(statement, parameters);
        
        List<DatabaseBrokerResultRow> response = new ArrayList<>();
        
        try {            
            ResultSet set = statement.executeQuery();
            
            while(set.next()) {
                response.add(currentResultSetRowToDatabaseBrokerRow(set));
            }
            
            set.close();
            
            statement.close();
            
            System.out.println("getAllResults uspesan.");
        } catch(SQLException ex) {
            System.out.println("Greska prilikom getAllResults. " + ex.getMessage());
        }
        
        closeConnection();
        
        return response;
    } 
    
    /**
     * Gets only the first result from set
     * 
     * @param query
     * @param parameters
     * @return 
     */
    public DatabaseBrokerResultRow getFirstResult(String query, List<Object> parameters) {
        if(!isActive()) {
            connect();
        } 
        
        PreparedStatement statement = this.getPreparedStatement(query);
        bindValuesToStatement(statement, parameters);
        
        DatabaseBrokerResultRow response = null;
        
        try {            
            ResultSet set = statement.executeQuery();
            
            if(set.next()) {
                response = currentResultSetRowToDatabaseBrokerRow(set);
            }
            
            set.close();
            
            statement.close();
            
            System.out.println("getFirstResult uspesan.");
        } catch(SQLException ex) {
            System.out.println("Greska prilikom getFirstResult. " + ex.getMessage());
        }
        
        closeConnection();
        
        return response;
    }
}
