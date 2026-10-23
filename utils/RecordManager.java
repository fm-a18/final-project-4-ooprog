package utils;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import inventory.*;

public class RecordManager {
    // ALL TXT FILES
    public static final String DATA_DIR = "final-project-4-ooprog/data/";

    public static final String FILE_RESERVED = DATA_DIR + "RESERVED.txt";
    public static final String FILE_ADOPTED = DATA_DIR + "ADOPTED_LIST.txt";
    public static final String FILE_CANCELLATION_REQUEST = DATA_DIR + "CANCELLATION_REQUEST.txt";
    public static final String FILE_CANCELLED_LIST = DATA_DIR + "CANCELLED_LIST.txt";
    public static final String FILE_RETURN_REQUEST = DATA_DIR + "RETURN_REQUEST.txt";
    public static final String FILE_RETURNED_LIST = DATA_DIR + "RETURNED_LIST.txt";
    public static final String FILE_PET_LIST = DATA_DIR + "PET_LIST.txt";
    public static final String FILE_INCOMING_LIST = DATA_DIR + "INCOMING_PET_LIST.txt";
    public static final String FILE_USER_LIST = DATA_DIR + "USER_LIST.txt";

    // FOR USER_LIST.txt
    public static final int US_USER_ID = 0, US_FIRST_NAME = 1, US_LAST_NAME = 2, US_AGE = 3,
            US_EMAIL_ADDRESS = 4, US_ADDRESS = 5, US_PHONE_NUMBER = 6, US_PASSWORD = 7, US_ROLE = 8;
    public static final int US_FIELD_COUNT = 9;

    // FOR PET_LIST.txt
    private static final int PET_ID = 0, PET_NAME = 1, PET_TYPE = 2, PET_BREED = 3, PET_GENDER = 4,
            PET_AGE = 5, PET_PRICE = 6, PET_STATUS = 7, PET_MEDICAL = 8, PET_ADOPTION = 9;
    public static final int PET_FIELD_COUNT = 10;

    // FOR RESERVED.txt
    public static final int RES_TRANSACTION_NUM = 0;
    public static final int RES_FIRST_NAME = 1;
    public static final int RES_LAST_NAME = 2;
    public static final int RES_EMAIL = 3;
    public static final int RES_PET_ID = 4;
    public static final int RES_PAYMENT_PLAN = 5;
    public static final int RES_DOWN_PAYMENT = 6;
    public static final int RES_BALANCE_LEFT = 7;
    public static final int RES_DATE_REQUESTED = 8;
    public static final int RES_DATE_CLAIMING = 9;
    public static final int RES_FIELD_COUNT = 10;

    // ADOPTED_LIST.txt
    public static final int AD_DATE_FINALIZED = 10;
    public static final int AD_APPROVED_BY = 11;
    public static final int AD_FIELD_COUNT = 12;

    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    // reader
    public static <T> ArrayList<T> readRecords(String fileName, int expectedFields, Function<String[], T> parser) {
        ArrayList<T> records = new ArrayList<>();
        File file = new File(fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank())
                    continue;

                String[] parts = line.split("\\s*\\|\\s*", -1);
                if (parts.length != expectedFields) {
                    System.out.println("Invalid record format: " + line);
                    continue;
                }

                try {
                    T record = parser.apply(parts);
                    if (record != null) {
                        records.add(record);
                    }
                } catch (RuntimeException e) {
                    System.out.println("Invalid data in record: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading " + fileName + ".");
        }
        return records;
    }

    public static Pet parsePet(String[] pet) {
        String id = pet[PET_ID].trim();
        String name = pet[PET_NAME].trim();
        String type = pet[PET_TYPE].trim();
        String breed = pet[PET_BREED].trim();
        char gender = pet[PET_GENDER].trim().charAt(0);
        int age = Integer.parseInt(pet[PET_AGE].trim());
        double price = Double.parseDouble(pet[PET_PRICE].trim().replace(",", ""));

        Pet parsedPet = switch (type.toLowerCase()) {
            case "dog" -> new Dog(id, name, breed, gender, age, price);
            case "cat" -> new Cat(id, name, breed, gender, age, price);
            case "bird" -> new Bird(id, name, breed, gender, age, price);
            default -> null;
        };

        if (parsedPet != null) {
            parsedPet.setAdoptionStatus(AdoptionStatus.valueOf(pet[PET_STATUS].trim().toUpperCase()));
            parsedPet.setMedicalHistory(pet[PET_MEDICAL].trim());
            parsedPet.setAdoptionHistory(pet[PET_ADOPTION].trim());
        }
        return parsedPet;
    }

    public static Reservation parseReservation(String[] pet) {
        return new Reservation(
                pet[RES_TRANSACTION_NUM].trim(),
                pet[RES_FIRST_NAME].trim(),
                pet[RES_LAST_NAME].trim(),
                pet[RES_EMAIL].trim(),
                pet[RES_PET_ID].trim(),
                pet[RES_PAYMENT_PLAN].trim(),
                Double.parseDouble(pet[RES_DOWN_PAYMENT].trim().replace(",", "")),
                Double.parseDouble(pet[RES_BALANCE_LEFT].trim().replace(",", "")),
                LocalDate.parse(pet[RES_DATE_REQUESTED].trim(), dateFormat),
                LocalDate.parse(pet[RES_DATE_CLAIMING].trim(), dateFormat));
    }

    public static ArrayList<Pet> viewAllPetRecords() {
        return readRecords(FILE_PET_LIST, PET_FIELD_COUNT, RecordManager::parsePet);
    }

    public static ArrayList<Reservation> viewAllReserveRecords() {
        return readRecords(FILE_RESERVED, RES_FIELD_COUNT, RecordManager::parseReservation);
    }

    public static Reservation parseAdoption(String[] parts) {
        return parseReservation(parts);
    }

    public static ArrayList<Reservation> viewAllPetAdoptedRecords() {
        return readRecords(FILE_ADOPTED, AD_FIELD_COUNT, RecordManager::parseAdoption);
    }

    public static String buildAdoptedRecord(String reservationRecord, String approvedByEmail) {
        String[] parts = Arrays.copyOf(reservationRecord.split("\\s*\\|\\s*", -1), AD_FIELD_COUNT);
        parts[AD_DATE_FINALIZED] = LocalDate.now().format(dateFormat);
        parts[AD_APPROVED_BY] = approvedByEmail;
        return String.join("|", parts);
    }

    public static String buildUserRecord(String userID, String firstName, String lastName, int age,
            String email, String address, String phone,
            String password, String role) {
        String[] parts = new String[US_FIELD_COUNT];
        parts[US_USER_ID] = userID;
        parts[US_FIRST_NAME] = firstName;
        parts[US_LAST_NAME] = lastName;
        parts[US_AGE] = String.valueOf(age);
        parts[US_EMAIL_ADDRESS] = email;
        parts[US_ADDRESS] = address;
        parts[US_PHONE_NUMBER] = phone;
        parts[US_PASSWORD] = password;
        parts[US_ROLE] = role;
        return String.join("|", parts);
    }

    public String buildAdoptionRequestRecord(String firstName, String lastName, String emailAddress, Pet selectedPet,
            String paymentPlan, double downPayment, double balanceLeft, String transactionNum, LocalDate date) {
        String[] parts = new String[RES_FIELD_COUNT];

        parts[RES_TRANSACTION_NUM] = transactionNum;
        parts[RES_FIRST_NAME] = firstName;
        parts[RES_LAST_NAME] = lastName;
        parts[RES_EMAIL] = emailAddress;
        parts[RES_PET_ID] = selectedPet.getPetID();
        parts[RES_PAYMENT_PLAN] = paymentPlan;
        parts[RES_DOWN_PAYMENT] = String.format("%,.2f", downPayment);
        parts[RES_BALANCE_LEFT] = String.format("%,.2f", balanceLeft);
        parts[RES_DATE_REQUESTED] = LocalDate.now().format(dateFormat);
        parts[RES_DATE_CLAIMING] = date.format(dateFormat);

        return String.join("|", parts);
    }

}
