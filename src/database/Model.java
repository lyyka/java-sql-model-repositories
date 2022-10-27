/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import repository.ModelRepository;

/**
 *
 * @author kredium
 */
abstract public class Model {
    private boolean persisted = false;
    
    private Long id;
    
    abstract protected ModelRepository getRepository();
    
    abstract public String tableName();
    
    public String getIdColumName()
    {
        return "id";
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void delete()
    {
        if(persisted && id != null) {
            this.getRepository().delete();
        }
    }
    
    public Model findById(Long id)
    {
        return this.getRepository().getById(id);
    }
    
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
