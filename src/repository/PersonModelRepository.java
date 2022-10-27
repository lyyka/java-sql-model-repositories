package repository;

import database.ModelAttributeValue;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import domain.City;
import domain.Gender;
import domain.Person;

public class PersonModelRepository extends ModelRepository<Person> {
    Person person;
    
    public PersonModelRepository(Person model) {
        super(model);
        this.person = model;
    }
    
    @Override
    public Map<String, ModelAttributeValue> mapDatabaseFields() {
        Map<String, ModelAttributeValue> res = new HashMap<>();
        res.put("name", new ModelAttributeValue() {
            @Override
            public Object get() {
                return person.getName();
            }

            @Override
            public void set(Object value) {
                person.setName((String) value);
            }
        });
        
        res.put("lastname", new ModelAttributeValue() {
            @Override
            public Object get() {
                return person.getLastname();
            }

            @Override
            public void set(Object value) {
                person.setLastname((String) value);
            }
        });
        
        res.put("gender", new ModelAttributeValue() {
            @Override
            public Object get() {
                return person.getGender().toString();
            }

            @Override
            public void set(Object value) {
                person.setGender(Gender.valueOf((String) value));
            }
        });
        
        res.put("city", new ModelAttributeValue() {
            @Override
            public Object get() {
                return person.getCity().getZipCode().toString();
            }

            @Override
            public void set(Object value) {
                person.setCity(new City((Long) value, ""));
            }
        });
        
        res.put("birthday", new ModelAttributeValue() {
            @Override
            public Object get() {
                return person.getBirthday();
            }

            @Override
            public void set(Object value) {
                person.setBirthday((Date) value);
            }
        });
        
        res.put("employed", new ModelAttributeValue() {
            @Override
            public Object get() {
                return person.isEmployed();
            }

            @Override
            public void set(Object value) {
                person.setEmployed((Boolean) value);
            }
        });
        
        return res;
    }
}
