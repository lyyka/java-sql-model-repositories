package domain;

import database.Model;
import repository.ModelRepository;
import repository.PersonModelRepository;
import java.util.Date;

public class Person extends Model {
    String name;
    String lastname;
    Gender gender;
    City city;
    Date birthday;
    boolean employed;

    public Person() {
    }

    public Person(String name, String lastname, Gender gender, City city, Date birthday, boolean employed) {
        this.name = name;
        this.lastname = lastname;
        this.gender = gender;
        this.city = city;
        this.birthday = birthday;
        this.employed = employed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isEmployed() {
        return employed;
    }

    public void setEmployed(boolean employed) {
        this.employed = employed;
    }

    @Override
    public String toString() {
        return "Person{" + "name=" + name + ", lastname=" + lastname + ", city=" + city + '}';
    }
    
    // Database setup

    @Override
    public ModelRepository getRepository() {
        return new PersonModelRepository(this);
    }

    @Override
    public String tableName() {
        return "persons";
    }
}
