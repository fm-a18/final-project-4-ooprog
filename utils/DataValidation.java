package utils;

import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DataValidation {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public static String validateName(Scanner sc, String field) {
        boolean isValidated = false;
        String input = "";
        while (!isValidated) {
            System.out.printf("%s Name: ", field);
            try {
                input = sc.nextLine().trim();
            } catch (java.util.NoSuchElementException e) {
                System.out.printf("Error: %s Name. Please try again.\n", field);
                continue;
            }

            if (input.isEmpty()) {
                System.out.printf("Error: %s name cannot be empty.\n", field);
            } else if (!input.matches("^[\\p{L}.,' -]+$")) {
                System.out.println("Error: Only letters, spaces, periods, commas, apostrophes, and hyphens allowed.");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static String validateEmailAddress(Scanner sc) {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            System.out.println("Email Address: ");
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

    public static String validateAddress(Scanner sc) {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            try {
                input = sc.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Error: Address cannot be empty. Please try again.");
                continue;
            }
            if (input.isEmpty()) {
                System.out.println("Error: Address cannot be empty.");
            } else if (!input.matches("^(?=.*[A-Za-z])[A-Za-z0-9.,#\\-/\\s]{10,150}$")) {
                System.out.println("Invalid address. Please enter a valid address (10-150 characters).");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static String validatePhoneNumber(Scanner sc) {
        boolean isValidated = false;
        String input = "";

        while (!isValidated) {
            System.out.println("Phone Number: ");
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
                System.out.println("Invalid Phone Number. Please use the following format: 09123456789, 0912-345-6789, or 0912 345 6789.");
            } else {
                input = digitFormat.substring(0, 4) + " "
                        + digitFormat.substring(4, 7) + " "
                        + digitFormat.substring(7);
                isValidated = true;
            }
        }
        return input;
    }

    public static String validatePassword(Scanner sc) {
        boolean isValidated = false;
        String input = "";
        while (!isValidated) {
            System.out.println("Password: ");
            try {
                input = sc.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Error: Password cannot be empty. Please try again.");
                continue;
            }
            if (input.isEmpty()) {
                System.out.println("Error: Email Address cannot be empty.");
            } else if (!input.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                System.out.println("Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
            } else {
                isValidated = true;
            }
        }
        return input;
    }

    public static int intChoiceValidation(Scanner sc, String prompt, int... choices) {
        int value = 0;
        boolean isValidated = false;
        while (!isValidated) {
            System.out.print(prompt + "(" + getValidIntChoices(choices) + "): ");
            String input = sc.nextLine().trim();

            if (!input.matches("[1-9][0-9]*")) {
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

    public static char charChoiceValidation(Scanner sc, String prompt, char... choices) {
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
                        getValidIntChoices(choices) + "."
        );
    }

    public static void printCharChoiceError(char... choices) {
        System.out.println(
                "Invalid choice. Choose from: " +
                        getValidCharChoices(choices) + "."
        );
    }
}
