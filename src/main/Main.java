package main;

import database.Model;
import java.util.Date;
import domain.City;
import domain.Gender;
import domain.Person;

public class Main {
    public static void main(String[] args) {

        City beograd = new City(11000L, "Beograd");

        Model person = new Person(
                "Test",
                "Testic",
                Gender.MUSKI,
                beograd,
                new Date(),
                true
        );
        
        person.save();
        
        System.out.println((new Person()).findById(1L));
    }
    
}
