package inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class Pet {
    private String petID;
    private String name;
    private String type;
    private String breed;
    private char gender;
    private int age;
    private String adoptionStatus;
    private double price;
    private HashMap<String, String> adoptionHistory;
    private HashMap<String, String> medicalHistory;

    public Pet(String petID, String name, String type, String breed, char gender, int age, String adoptionStatus,
            double price, HashMap<String, String> adoptionHistory, HashMap<String, String> medicalHistory) {
        this.petID = petID;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.adoptionStatus = adoptionStatus;
        this.price = price;
        this.adoptionHistory = adoptionHistory;
        this.medicalHistory = medicalHistory;
    }

    public String getPetID() {
        return petID;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

    public String getPetName() {
        return name;
    }

    public void setPetName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAdoptionStatus() {
        return adoptionStatus;
    }

    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAdoptionHistory(HashMap<String, String> adoptionHistory) {
        this.adoptionHistory = adoptionHistory;
    }

    public void setMedicalHistory(HashMap<String, String> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    private String formatHistory(HashMap<String, String> history, String emptyMessage) {
    if (history == null || history.isEmpty()) {
        return emptyMessage;
    }

    StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, String> entry : history.entrySet()) {
        sb.append(String.format("%s - %s%n", entry.getKey(), entry.getValue()));
    }

    return sb.toString();
}

    public String getAdoptionHistory() {
        return formatHistory(adoptionHistory, "No adoption history");
    }

    public String getMedicalHistory() {
        return formatHistory(medicalHistory, "No medical history");
    }
}
