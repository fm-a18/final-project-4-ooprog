import users.User;
import utils.DataValidation;
import utils.DisplayUtils;
import utils.FileHandler;
import users.Coordinator;
import users.Customer;
import users.Admin;
import utils.RecordManager;

import static utils.RecordManager.US_ADDRESS;
import static utils.RecordManager.US_AGE;
import static utils.RecordManager.US_EMAIL_ADDRESS;
import static utils.RecordManager.US_FIELD_COUNT;
import static utils.RecordManager.US_FIRST_NAME;
import static utils.RecordManager.US_LAST_NAME;
import static utils.RecordManager.US_PASSWORD;
import static utils.RecordManager.US_PHONE_NUMBER;
import static utils.RecordManager.US_ROLE;
import static utils.RecordManager.US_USER_ID;

import java.io.*;

public class Main {
    public static void customerLandingPage() {
        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("CUSTOMER",
                    "Sign Up",
                    "Log In",
                    "Exit");
            int getChoice = DataValidation.intChoiceValidation("Selection Option", 1, 2, 3);
            switch (getChoice) {
                case 1 -> customerSignUp();
                case 2 -> customerLogin();
                case 3 -> isDone = true;
            }
        }
    }

    public static void employeeLandingPage() {
        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("EMPLOYEE",
                    "Log In As Coordinator", "Log In As Admin",
                    "Exit");
            int getChoice = DataValidation.intChoiceValidation("Selection Option", 1, 2, 3);
            switch (getChoice) {
                case 1 -> coordinatorLogin();
                case 2 -> adminLogin();
                case 3 -> isDone = true;
            }
        }
    }

    public static void customerSignUp() {
        System.out.println("===================== CUSTOMER SIGNUP ======================");
        String firstName = DataValidation.validateString("Enter your First Name");
        String lastName = DataValidation.validateString("Enter your Last Name");
        int age = DataValidation.validateCustomerAgeInput();
        String emailAddress = DataValidation.validateNewEmailAddress();
        String address = DataValidation.validateAddress();
        String phoneNumber = DataValidation.validatePhoneNumber();
        String password = DataValidation.validatePassword();
        String userID = utils.generators.UserIdGenerator.userIdGenerator();
        boolean isFileSaved = false;
        String role = "Customer";
        while (!isFileSaved) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(RecordManager.FILE_USER_LIST, true))) {
                String record = RecordManager.buildUserRecord(
                        userID,
                        firstName,
                        lastName,
                        age,
                        emailAddress,
                        address,
                        phoneNumber,
                        password,
                        role
                );

                writer.write(record);
                writer.newLine();

                System.out.println("User successfully registered!");
                isFileSaved = true;
            } catch (IOException e) {
                System.out.println("Error in writing file. Try again?");
                char retry = DataValidation.charChoiceValidation("Error in writing file. Try again?", 'Y', 'N');
                if (retry != 'Y') {
                    isFileSaved = true;
                }
            }
        }
    }

    public static void customerLogin() {
        login("CUSTOMER LOGIN", "CUSTOMER");
    }

    public static void coordinatorLogin() {
        login("COORDINATOR LOGIN", "COORDINATOR");
    }

    public static void adminLogin() {
        login("ADMIN LOGIN", "ADMIN");
    }

    private static void login(String title, String expectedRole) {
        String[] parts = authenticate(title, expectedRole);
        if (parts == null) {
            return;
        }
        System.out.println("Login successful.");
        createUser(parts).showMenu(DataValidation.sc);
    }

    private static String[] authenticate(String title, String expectedRole) {
        if (!new File(RecordManager.FILE_USER_LIST).exists()) {
            System.out.println("No User Records found.");
            return null;
        }

        DisplayUtils.centeredTitle(title, DisplayUtils.MENU_WIDTH);
        String email = DataValidation.validateEmailAddress();
        String userData = FileHandler.findRecord(RecordManager.FILE_USER_LIST, email, US_EMAIL_ADDRESS);
        String password = DataValidation.validatePasswordForLogin();

        if (userData == null) {
            System.out.println("Invalid email, password, or account type.");
            return null;
        }

        String[] parts = userData.split("\\s*\\|\\s*", -1);
        if (parts.length != US_FIELD_COUNT) {
            System.out.println("This account record is damaged. Check USER_LIST.txt.");
            return null;
        }

        boolean isAuthorized = parts[US_PASSWORD].equals(password)
                && parts[US_ROLE].equalsIgnoreCase(expectedRole);
        if (!isAuthorized) {
            System.out.println("Invalid email, password, or account type.");
            return null;
        }
        return parts;
    }

    private static User createUser(String[] p) {
        int age = Integer.parseInt(p[US_AGE]);
        return switch (p[US_ROLE].toUpperCase()) {
            case "ADMIN" -> new Admin(p[US_USER_ID], p[US_FIRST_NAME], p[US_LAST_NAME], age,
                    p[US_EMAIL_ADDRESS], p[US_ADDRESS], p[US_PHONE_NUMBER], p[US_PASSWORD]);
            case "COORDINATOR" -> new Coordinator(p[US_USER_ID], p[US_FIRST_NAME], p[US_LAST_NAME], age,
                    p[US_EMAIL_ADDRESS], p[US_ADDRESS], p[US_PHONE_NUMBER], p[US_PASSWORD]);
            default -> new Customer(p[US_USER_ID], p[US_FIRST_NAME], p[US_LAST_NAME], age,
                    p[US_EMAIL_ADDRESS], p[US_ADDRESS], p[US_PHONE_NUMBER], p[US_PASSWORD]);
        };
    }

    public static void main(String[] args) {
        if (!FileHandler.ensureFilesExist()) { // for debugging
            return; // data folder not found, so nothing is created or written
        }
        System.out.println("Welcome!");

        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("PET ADOPTION SYSTEM",
                    "Customer",
                    "Employee(Coordinator / Admin)",
                    "Exit Program");
            int getRole = DataValidation.intChoiceValidation("Selection Option", 1, 2, 3);
            switch (getRole) {
                case 1 -> customerLandingPage();
                case 2 -> employeeLandingPage();
                case 3 -> isDone = true;
            }
        }
        System.out.println("Thank you for using the program!");
    }
}
