package database;

import java.util.List;
import repository.ModelRepository;

abstract public class Model {
    private boolean persisted = false;
    
    private Long id;
    
    abstract public ModelRepository getRepository();
    
    abstract public String tableName();
    
    /**
     * Get column name used for ID
     * @return 
     */
    public String getIdColumName()
    {
        return "id";
    }
    
    /**
     * Get ID value
     * @return 
     */
    public Long getId() {
        return id;
    }
    
    /**
     * ID setter
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Change indicator if the model is persisted
     * @param persisted 
     */
    public void setPersisted(boolean persisted) {
        this.persisted = persisted;
    }
    
    /**
     * Delete model
     */
    public void delete()
    {
        if(persisted && id != null) {
            this.getRepository().deleteById(this.getId());
        }
    }
    
    /**
     * Get all entities
     * @return 
     */
    public List<Model> all()
    {
        return this.getRepository().allModels();
    }
    
    /**
     * Find model by ID
     * @param id
     * @return 
     */
    public Model findById(Long id)
    {
        return this.getRepository().getById(id);
    }
    
    /**
     * Save/update model
     */
    public void save()
    {
        if(persisted) {
            this.getRepository().update();
        } else {
            this.getRepository().create();
            persisted = true;
        }
    }
}
