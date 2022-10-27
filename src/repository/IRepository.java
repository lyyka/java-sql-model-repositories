package repository;

import java.util.List;

public interface IRepository {
    public void create();
    
    public Object getById(Long id);
    
    public List<Object> all();
    
    public void update();
    
    public void delete();
}
