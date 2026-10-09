import inventory.Pet;
import users.User;
import utils.DataValidation;
import utils.DisplayUtils;
import utils.FileHandler;
import users.Customer;

import java.io.*;
import java.util.ArrayList;

import static utils.DataValidation.emailExists;

public class Main {
    public static final ArrayList<User> listOfUsers = new ArrayList<>();
    public static final ArrayList<Pet> listOfPets = new ArrayList<>();

    public static void adminPermissions() {
        System.out.println("Admin");
    }

    public static void adoptionCoordinator() {
        System.out.println("Coordinator");
    }

    public static void adopterSigning() {
        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("CUSTOMER",
                    "Sign Up",
                    "Log In",
                    "Exit");
            int getChoice = DataValidation.intChoiceValidation("Selection Option", 1, 2, 3);
            switch (getChoice) {
                case 1 -> userSignUp();
                case 2 -> userLogin();
                case 3 -> isDone = true;
            }
        }
    }

    public static void adopterIn(Customer loggedInCustomer) {
        listOfUsers.add(loggedInCustomer);
        loggedInCustomer.showMenu(DataValidation.sc);
    }

    public static void userLogin() {
        File file = new File("USER_LIST.txt");
        if (!file.exists()) {
            System.out.println("No User Records found.");
            return;
        }
        String email = DataValidation.validateEmailAddress();
        String userData = FileHandler.findRecord("USER_LIST.txt", email, 4);
        if (userData == null) {
            System.out.println("Account not found");
            return;
        }
        String[] parts = userData.split("\\s*\\|\\s*");

        String password = DataValidation.validatePassword();
        if (!parts[7].equals(password)) {
            System.out.println("Incorrect password.");
            return;
        }
        System.out.println("Login successful.");
        Customer loggedInCustomer = new Customer(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4],
                parts[5], parts[6], parts[7]);
        adopterIn(loggedInCustomer);
    }

    public static void userSignUp() {
        String firstName = DataValidation.validateString("Enter your First Name: ");
        String lastName = DataValidation.validateString("Enter your Last Name: ");
        int age = DataValidation.validateCustomerAgeInput("Enter your Age: ");
        String emailAddress;
        boolean emailExists;
        do {
            emailAddress = DataValidation.validateEmailAddress();
            emailExists = emailExists(emailAddress);
            if (emailExists) {
                System.out.println("Email has already been registered. Please try a different one.");
            }
        } while (emailExists);

        String address = DataValidation.validateAddress();
        String phoneNumber = DataValidation.validatePhoneNumber();
        String password = DataValidation.validatePassword();

        boolean isFileSaved = false;

        while (!isFileSaved) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("USER_LIST.txt", true))) {
                writer.write(String.format("%s|%s|%s|%s|%s|%s|%s%n", firstName, lastName, age, emailAddress,
                        address, phoneNumber, password));
                System.out.println("User successfully registered!");
                isFileSaved = true;
            } catch (IOException e) {
                System.out.println("Error in writing file. Try again? (Y/N)");
                char retry = DataValidation.charChoiceValidation("Error in writing file. Try again? (Y/N)", 'Y', 'N');
                if (retry != 'Y') {
                    isFileSaved = true;
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome!");

        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("PET ADOPTION SYSTEM",
                    "Customer",
                    "Coordinator",
                    "Admin",
                    "Exit Program");
            int getRole = DataValidation.intChoiceValidation("Selection Option", 1, 2, 3, 4);
            switch (getRole) {
                case 1 -> adopterSigning();
                case 2 -> adoptionCoordinator();
                case 3 -> adminPermissions();
                case 4 -> isDone = true;
            }
        }
        System.out.println("Thank you!");
    }
}
