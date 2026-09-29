package inventory;

import java.util.HashMap;

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

    public HashMap<String, String> getAdoptionHistory() {
        return adoptionHistory;
    }

    public void setAdoptionHistory(HashMap<String, String> adoptionHistory) {
        this.adoptionHistory = adoptionHistory;
    }

    public HashMap<String, String> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(HashMap<String, String> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
