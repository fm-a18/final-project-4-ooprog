package utils;

import java.util.Random;
import java.util.Scanner;
import inventory.Bird;
import inventory.Cat;
import inventory.Dog;
import inventory.Pet;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DataValidation {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    public static final Scanner sc = new Scanner(System.in);

    public static String validateString(String prompt) { // TODO IMPROVE ERROR MESSAGE
        boolean isValidated = false;
        String input = "";
        while (!isValidated) {
            System.out.print(prompt);
            try {
                input = sc.nextLine().trim();
            } catch (java.util.NoSuchElementException e) {
                System.out.print("Invalid String input. Please try again.\n");
                continue;
            }

            if (input.isEmpty()) {
                System.out.print("Invalid String input. Input cannot be empty. Try Again\n");
            } else if (!input.matches("^[\\p{L}.,' -]+$")) {
                System.out.println("Error: Only letters, spaces, periods, commas, apostrophes, and hyphens allowed.");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static int validateCustomerAgeInput(String prompt) { // TODO IMPROVE ERROR MESSAGE AND REFLECT MIN AND MAX
                                                                // HUMAN AGE
        boolean isRunning = true;
        int number = 0;
        String inputNumber = "";
        while (isRunning) {
            System.out.print(prompt);
            inputNumber = sc.nextLine().trim();
            if (!inputNumber.matches("-?(0|[1-9]\\d*)")) {
                System.out.println("Invalid Input. Only input positive integers without leading zeros. Try Again.");
                continue;
            }
            try {
                number = Integer.parseInt(inputNumber);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input. Number is too large. Try Again.");
                continue;
            }
            if (number < 18 || number > 90) {
                System.out.println("Invalid Input. The age can only be from 18 - 90 years. Try again.");
                continue;
            }
            isRunning = false;
        }
        return number;
    }

    public static int validatePetAgeInput(String prompt) { // TODO IMPROVE ERROR MESSAGE AND REFLECT MIN AND MAX PET AGE
        boolean isRunning = true;
        int number = 0;
        String inputNumber = "";
        while (isRunning) {
            System.out.print(prompt);
            inputNumber = sc.nextLine().trim();
            if (!inputNumber.matches("-?(0|[1-9]\\d*)")) {
                System.out.println("Invalid Input. Only input positive integers without leading zeros. Try Again.");
                continue;
            }
            try {
                number = Integer.parseInt(inputNumber);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input. Number is too large. Try Again.");
                continue;
            }
            if (number < 1 || number > 15) {
                System.out.println("Invalid Input. The age of the pet can only be from 1 - 15 years. Try again.");
                continue;
            }
            isRunning = false;
        }
        return number;
    }

    public static String validateEmailAddress() {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            System.out.print("Enter your Email Address: ");
            try {
                input = sc.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Error: Email Address cannot be empty. Please try again.");
                continue;
            }
            if (input.isEmpty()) {
                System.out.println("Error: Email Address cannot be empty.");
            } else if (!input.matches("^[\\w._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println("Invalid Email Address. Please follow the following format: name@example.com");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static boolean emailExists(String emailAddress) {
        return FileHandler.findRecord("USER_LIST.txt", emailAddress, 2) != null;
    }

    public static String validateAddress() {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            System.out.println("Email Address: ");
            try {
                System.out.print("Enter your Address: ");
                input = sc.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Error: Email Address cannot be empty. Please try again.");
                continue;
            }
            if (input.isEmpty()) {
                System.out.println("Error: Email Address cannot be empty.");
            } else if (!emailExists(input)) {
                System.out.println("Error: Email already exists.");
            } else if (!input.matches("^(?=.*[A-Za-z])[A-Za-z0-9.,#\\-/\\s]{10,150}$")) {
                System.out.println("Invalid Email Address. Please enter a valid Email Address.");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static LocalDate validateDate(String prompt) { // coordinator
        LocalDate date = null;
        while (date == null) {
            System.out.print(prompt + ": ");
            String input = sc.nextLine().trim();

            try {
                date = LocalDate.parse(input, dateFormat);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid Date Format. Please use (MM-DD-YYYY).");
            }
        }
        return date;
    }

    public static String validatePhoneNumber() {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            System.out.print("Enter your Phone Number: ");
            try {
                input = sc.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Error: Phone Number cannot be empty. Please try again.");
                continue;
            }
            if (input.isEmpty()) {
                System.out.println("Error: Phone Number cannot be empty.");
                continue;
            }

            String digitFormat = input.replaceAll("[-\\s]", "");

            if (!digitFormat.matches("^09\\d{9}$")) {
                System.out.println(
                        "Invalid Phone Number. Please use the following format: 09123456789, 0912-345-6789, or 0912 345 6789.");
            } else {
                input = digitFormat.substring(0, 4) + " "
                        + digitFormat.substring(4, 7) + " "
                        + digitFormat.substring(7);
                isValidated = true;
            }
        }
        return input;
    }

    public static String validatePassword() {
        boolean isValidated = false;
        String input = "";
        while (!isValidated) {
            System.out.print("Enter your Password: ");
            try {
                input = sc.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Error: Password cannot be empty. Please try again.");
                continue;
            }
            if (input.isEmpty()) {
                System.out.println("Error: Email Address cannot be empty.");
            } else if (!input.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                System.out.println(
                        "Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static int intChoiceValidation(String prompt, int... choices) {
        int value = 0;
        boolean isValidated = false;
        while (!isValidated) {
            System.out.print(prompt + "(" + getValidIntChoices(choices) + "): ");
            String input = sc.nextLine().trim();

            if (!input.matches("0|[1-9][0-9]*")) {
                printIntChoiceError(choices);
                continue;
            }

            try {
                value = Integer.parseInt(input);
                boolean found = false;
                for (int choice : choices) {
                    if (value == choice) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    isValidated = true;
                } else {
                    printIntChoiceError(choices);
                }
            } catch (NumberFormatException e) {
                printIntChoiceError(choices);
            }
        }
        return value;
    }

    public static char charChoiceValidation(String prompt, char... choices) {
        boolean isValidated = false;
        char input = ' ';
        while (!isValidated) {
            try {
                System.out.print(prompt + "(" + getValidCharChoices(choices) + "): ");
                String line = sc.nextLine().trim().toUpperCase();
                if (line.length() != 1) {
                    printCharChoiceError(choices);
                    continue;
                }
                input = line.charAt(0);
                boolean found = false;
                for (char choice : choices) {
                    if (input == Character.toUpperCase(choice)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    isValidated = true;
                } else {
                    printCharChoiceError(choices);
                }
            } catch (Exception e) {
                printCharChoiceError(choices);
            }
        }
        return input;
    }

    public static String getValidIntChoices(int... choices) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < choices.length; i++) {
            sb.append(choices[i]);

            if (i < choices.length - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    public static String getValidCharChoices(char... choices) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < choices.length; i++) {
            sb.append(Character.toUpperCase(choices[i]));

            if (i < choices.length - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    public static void printIntChoiceError(int... choices) {
        System.out.println(
                "Invalid choice. Choose from: " +
                        getValidIntChoices(choices) + ".");
    }

    public static void printCharChoiceError(char... choices) {
        System.out.println(
                "Invalid choice. Choose from: " +
                        getValidCharChoices(choices) + ".");
    }

    public static double validatePriceInput(String prompt) { // TODO ADJUST ERROR MESSAGE(REFLECT ACTUAL
                                                             // LIMIT)
        boolean isRunning = true;
        double number = 0;
        String inputNumber = "";

        while (isRunning) {
            System.out.print(prompt);
            inputNumber = sc.nextLine().trim();

            if (!inputNumber.matches("-?(0|[1-9]\\d*)(\\.\\d+)?")) {
                System.out.println("Invalid Input. Only input numbers without leading zeroes. Try Again.");
                continue;
            }
            number = Double.parseDouble(inputNumber);
            if (number < 500 || number > 1000) {
                System.out.println("Invalid Price Input. Price must be between P500.00 - P1000.00. Try Again.");
                continue;
            }
            isRunning = false;
        }
        return number;
    }

    public static String getCategoryPrefix(String category) {
        if (category == null) {
            return null;
        }
        switch (category.trim().toLowerCase()) {
            case "dog":
                return Dog.ID_PREFIX;
            case "cat":
                return Cat.ID_PREFIX;
            case "bird":
                return Bird.ID_PREFIX;
            default:
                return null;
        }
    }

    // Prompts until the user enters an ID matching the category prefix + 4 digits
    // (e.g. DO0001).
    public static String validatePetIdInput(String prompt, String category) {
        String prefix = getCategoryPrefix(category);
        if (prefix == null) {
            throw new IllegalArgumentException("Unknown pet category: " + category);
        }

        String input = "";
        boolean isValidated = false;
        while (!isValidated) {
            System.out.print(prompt);
            input = sc.nextLine().trim().toUpperCase();

            if (input.isEmpty()) {
                System.out.println("Error: Pet ID cannot be empty.");
            } else if (!input.matches("^" + prefix + "\\d{4}$")) {
                System.out.println("Invalid Pet ID. A " + category + " ID must start with " + prefix
                        + " followed by 4 digits (e.g., " + prefix + "0001).");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static String validatePetID(String prompt, String regex) {
        String id = "";
        boolean isValid = false;
        while (!isValid) {
            System.out.println(
                    "PET ID FORMAT: First letter of pet type (D, C, B) followed by 4 digits (e.g., D0001, C0002, B0003)");
            System.out.print(prompt);
            id = sc.nextLine().trim().toUpperCase();

            if (id.isEmpty()) {
                System.out.println("Error: Pet ID cannot be empty.");
            } else if (!id.matches(regex)) {
                System.out.println("Invalid Pet ID. Try another ID.");
            } else {
                isValid = true;
            }
        }
        return id;
    }

    public static String categoryValidation(Scanner sc) {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            System.out.print("Category: ");
            try {
                input = sc.nextLine().trim();
            } catch (java.util.NoSuchElementException e) {
                System.out.println("Error: No input available. Please try again.");
                continue;
            }

            if (input.isEmpty()) {
                System.out.println("Error: Category cannot be empty.");
            } else if (input.equalsIgnoreCase("Dog")) {
                input = "Dog";
                isValidated = true;
            } else if (input.equalsIgnoreCase("Cat")) {
                input = "Cat";
                isValidated = true;
            } else if (input.equalsIgnoreCase("Bird")) {
                input = "Bird";
                isValidated = true;
            } else {
                System.out.println("Error: Category " + input + " does not exist!");
            }
        }
        return input;
    }

    // Builds a placeholder Pet of the given category, e.g. for lookups or
    // comparisons. Returns null if unknown.
    public static Pet createTempPet(String category, String petID) {
        String name = "TEMP";
        String breed = "N/A";
        char gender = 'U';
        int age = 0;
        String status = "Available";
        double price = 0.0;
        HashMap<String, String> adoptionHistory = new HashMap<>();
        HashMap<String, String> medicalHistory = new HashMap<>();

        if (category == null) {
            return null;
        }
        switch (category.trim().toLowerCase()) {
            case "dog":
                return new Dog(petID, name, breed, gender, age, price);
            case "cat":
                return new Cat(petID, name, breed, gender, age, price);
            case "bird":
                return new Bird(petID, name, breed, gender, age, price);
            default:
                return null;
        }
    }

    // TRANSACTION NUMBER VALIDATION FOR ADOPTION STATUS
    private static int transactionCtr = 0;

    public static String transactionNumGenerator() {
        transactionCtr++;
        return "T" + String.format("%07d", transactionCtr);
    }

    public static String transactionNumberValidation(String prompt) {
        String transactionNum = "";
        boolean isValid = false;
        while (!isValid) {
            System.out.print(prompt);
            try {
                transactionNum = sc.nextLine().trim().toUpperCase();
            } catch (NoSuchElementException e) {
                System.out.println("Error: Transaction Number cannot be empty. Please try again.");
                continue;
            }

            if (transactionNum.isEmpty()) {
                System.out.println("Error: Transaction Number cannot be empty.");
            } else if (!transactionNum.matches("^T\\d{7}$")) {
                System.out.println(
                        "Invalid Transaction Number. Please input the transaction number given after the request for adoption is granted.");
            } else {
                isValid = true;
            }
        }
        return transactionNum;
    }
}