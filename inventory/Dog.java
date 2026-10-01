package inventory;

import java.util.HashMap;

public class Dog extends Pet {
    public static final String TYPE = "Dog";
    public static final String ID_PREFIX = "DO";

    public Dog(String petID, String name, String type, String breed, char gender, int age, String adoptionStatus,
            double price, HashMap<String, String> adoptionHistory, HashMap<String, String> medicalHistory) {
        super(petID, name, TYPE, breed, gender, age, adoptionStatus, price, adoptionHistory, medicalHistory);
    }

    @Override
    public String getCategory() {
        return TYPE;
    }

    @Override
    public String getPetIDPrefix() {
        return ID_PREFIX;
    }
}
