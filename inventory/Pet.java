package inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class Pet {
    private final String petID;
    private final String name;
    private final String breed;
    private final char gender;
    private int age;
    private double price;
    private AdoptionStatus adoptionStatus;
    private HashMap<String, String> adoptionHistory;
    private HashMap<String, String> medicalHistory;

    public Pet(String petID, String name, String breed, char gender, int age, double price) {
        this.petID = petID;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.price = price;

        this.adoptionStatus = AdoptionStatus.AVAILABLE;
        this.adoptionHistory = new HashMap<>();
        this.medicalHistory = new HashMap<>();
    }

    public abstract String getPetIDPrefix();

    public abstract String getType();

    public String getPetID() {
        return petID;
    }

    public String getPetName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public char getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAdoptionStatus() {
        return adoptionStatus.toString();
    }

    public boolean idExists(String petID){
        return this.petID.equalsIgnoreCase(petID);
    }

    public void setAdoptionStatus(AdoptionStatus adoptionStatus) {
        if (adoptionStatus == null) {
            throw new IllegalArgumentException("Adoption status cannot be null");
        }
        this.adoptionStatus = adoptionStatus;
    }

    public void addAdoptionHistory(String date, String details) {
        adoptionHistory.put(date, details);
    }

    public void addMedicalHistory(String date, String details) {
        medicalHistory.put(date, details);
    }

    private static String formatHistory(HashMap<String, String> history, String emptyMessage) {
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
