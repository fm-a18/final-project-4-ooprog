package inventory;

import utils.RecordManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public abstract class Pet {
    private final String petID;
    private final String name;
    private final String breed;
    private final char gender;
    private int age;
    private final double price;
    private AdoptionStatus adoptionStatus;
    private final TreeMap<LocalDate, ArrayList<String>> adoptionHistory;
    private final TreeMap<LocalDate, ArrayList<String>> medicalHistory;

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

    // History
    private static LocalDate parseHistoryDate(String text) {
        try {
            return LocalDate.parse(text, RecordManager.dateFormat);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(text);
        }
    }

    private static void fillHistory(Map<LocalDate, ArrayList<String>> history, String fileText) {
        if (fileText.isEmpty()) {
            return;
        }

        for (String entry : fileText.split(";")) {
            String[] parts = entry.split("~", 2);
            if (parts.length != 2) {
                continue;
            }
            try {
                history.computeIfAbsent(parseHistoryDate(parts[0]), d -> new ArrayList<>()).add(parts[1]);
            } catch (DateTimeParseException e) {
                System.out.println("Skipped history entry with invalid date: " + entry);
            }
        }
    }

    private static String historyToFileText(Map<LocalDate, ArrayList<String>> history) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<LocalDate, ArrayList<String>> entry : history.entrySet()) {
            for (String details : entry.getValue()) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(entry.getKey().format(RecordManager.dateFormat)).append("~").append(details);
            }
        }
        return sb.toString();
    }

    private static String removeSeparators(String text) {
        return text.replace("|", " ").replace(";", " ").replace("~", " ");
    }

    private static String formatHistory(Map<LocalDate, ArrayList<String>> history, String emptyMessage) {
        if (history == null || history.isEmpty()) {
            return emptyMessage;
        }

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<LocalDate, ArrayList<String>> entry : history.entrySet()) {
            for (String details : entry.getValue()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(entry.getKey().format(RecordManager.dateFormat)).append(" - ").append(details);
            }
        }
        return sb.toString();
    }

    public void addAdoptionHistory(LocalDate date, String details) {
        adoptionHistory.computeIfAbsent(date, d -> new ArrayList<>()).add(removeSeparators(details));
    }

    public void addMedicalHistory(LocalDate date, String details) {
        medicalHistory.computeIfAbsent(date, d -> new ArrayList<>()).add(removeSeparators(details));
    }

    public String getAdoptionHistory() {
        return formatHistory(adoptionHistory, "No adoption history");
    }

    public String getMedicalHistory() {
        return formatHistory(medicalHistory, "No medical history");
    }

    public void setAdoptionHistory(String fileText) {
        adoptionHistory.clear();
        fillHistory(adoptionHistory, fileText);
    }

    public void setMedicalHistory(String fileText) {
        medicalHistory.clear();
        fillHistory(medicalHistory, fileText);
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
