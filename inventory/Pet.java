package inventory;

import java.util.Map;
import java.util.TreeMap;

public abstract class Pet {
    private final String petID;
    private final String name;
    private final String breed;
    private final char gender;
    private int age;
    private double price;
    private AdoptionStatus adoptionStatus;
    private final TreeMap<String, String> adoptionHistory;
    private final TreeMap<String, String> medicalHistory;

    public Pet(String petID, String name, String breed, char gender, int age, double price) {
        this.petID = petID;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.price = price;

        this.adoptionStatus = AdoptionStatus.AVAILABLE;
        this.adoptionHistory = new TreeMap<>();
        this.medicalHistory = new TreeMap<>();
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

    public String getGender() {
        if (gender == 'm' || gender == 'M') {
            return "Male";
        } else {
            return "Female";
        }
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

    public AdoptionStatus getAdoptionStatus() {
        return adoptionStatus;
    }

    public boolean idExists(String petID) {
        return this.petID.equalsIgnoreCase(petID);
    }

    public void setAdoptionStatus(AdoptionStatus adoptionStatus) {
        if (adoptionStatus == null) {
            throw new IllegalArgumentException("Adoption status cannot be null");
        }
        this.adoptionStatus = adoptionStatus;
    }

    public void setAdoptionHistory(String fileText) {
        adoptionHistory.clear();
        fillHistory(adoptionHistory, fileText);
    }

    public void setMedicalHistory(String fileText) {
        medicalHistory.clear();
        fillHistory(medicalHistory, fileText);
    }

    private static void fillHistory(Map<String, String> history, String fileText) {
        if (fileText.isEmpty()) {
            return;
        }

        String[] entries = fileText.split(";");
        for (String entry : entries) {
            String[] parts = entry.split("~", 2);
            if (parts.length == 2) {
                history.put(parts[0], parts[1]);
            }
        }
    }

    private static String historyToFileText(Map<String, String> history) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : history.entrySet()) {
            if (sb.length() > 0) {
                sb.append(";");
            }
            sb.append(entry.getKey()).append("~").append(entry.getValue());
        }

        return sb.toString();
    }

    private static String removeSeparators(String text) {
        return text.replace("|", " ").replace(";", " ").replace("~", " ");
    }

    public void addAdoptionHistory(String date, String details) {
        adoptionHistory.put(removeSeparators(date), removeSeparators(details));
    }

    public void addMedicalHistory(String date, String details) {
        medicalHistory.put(removeSeparators(date), removeSeparators(details));
    }

    private static String formatHistory(Map<String, String> history, String emptyMessage) {
        if (history == null || history.isEmpty()) {
            return emptyMessage;
        }

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : history.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append(" - ").append(entry.getValue());
        }

        return sb.toString();
    }

    public String getAdoptionHistory() {
        return formatHistory(adoptionHistory, "No adoption history");
    }

    public String getMedicalHistory() {
        return formatHistory(medicalHistory, "No medical history");
    }

    public String toFileString() {
        return String.join("|",
                petID,
                name,
                getType(),
                breed,
                String.valueOf(gender),
                String.valueOf(age),
                String.valueOf(price),
                adoptionStatus.name(),
                historyToFileText(medicalHistory),
                historyToFileText(adoptionHistory)
        );
    }

}
