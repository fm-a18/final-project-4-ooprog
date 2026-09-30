package inventory;

import java.util.HashMap;

public class Bird extends Pet {
    public Bird(String petID, String name, String type, String breed, char gender, int age, String adoptionStatus,
            double price, HashMap<String, String> adoptionHistory, HashMap<String, String> medicalHistory) {
        super(petID, name, "Dog", breed, gender, age, adoptionStatus, price, adoptionHistory, medicalHistory);
    }
}
