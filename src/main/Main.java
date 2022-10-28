package main;

import database.Model;
import domain.Person;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Model> models = (new Person()).all();
        
        for(Model model : models) {
//            System.out.println(model);
            model.delete();
        }
    }
}
