package inventory;

import java.util.HashMap;

public class Cat extends Pet {
    public static final String TYPE = "Cat";
    public static final String ID_PREFIX = "CA";

    public Cat(String petID, String name, String type, String breed, char gender, int age, String adoptionStatus,
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
