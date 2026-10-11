import inventory.Pet;
import users.User;
import utils.DataValidation;
import utils.DisplayUtils;
import utils.FileHandler;
import users.Coordinator;
import users.Customer;
import users.Admin;

import java.io.*;
import java.util.ArrayList;

import static utils.DataValidation.emailExists;

public class Main {
    public static final ArrayList<User> listOfUsers = new ArrayList<>();
    public static final ArrayList<Pet> listOfPets = new ArrayList<>();

    //TODO MIGHT MERGE COORDINATOR + ADMIN LANDING PAGE SINCE CODE BLOCK IS LITERALLY CARBON COPY
    //TODO MIGHT ALSO MERGE FOR LOGIN SINCE IT IS ALSO CARBON COPY(JUST MAKE THE LOGIN HAVE PARAMETERS)
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

    public static void coordinatorLandingPage(){
        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("COODINATOR",
                    "Log In",
                    "Exit");
            int getChoice = DataValidation.intChoiceValidation("Selection Option", 1, 2);
            switch (getChoice) {
                case 1 -> coordinatorLogin();
                case 2 -> isDone = true;
            }
        }
    }

    public static void adminLandingPage() {
        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("ADMIN",
                    "Log In",
                    "Exit");
            int getChoice = DataValidation.intChoiceValidation("Selection Option", 1, 2);
            switch (getChoice) {
                case 1 -> adminLogin();
                case 2 -> isDone = true;
            }
        }
    }

    public static void customerSignUp() {
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

    public static void customerLogin() {
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

    public static void coordinatorLogin() {
        File file = new File("COORDINATOR_LIST.txt");
        if (!file.exists()) {
            System.out.println("No Coordinator Records found.");
            return;
        }
        String email = DataValidation.validateEmailAddress();
        String coordinatorData = FileHandler.findRecord("COORDINATOR_LIST.txt", email, 4);
        if (coordinatorData == null) {
            System.out.println("Coordinator account not found");
            return;
        }
        String[] parts = coordinatorData.split("\\s*\\|\\s*");

        String password = DataValidation.validatePassword();
        if (!parts[7].equals(password)) {
            System.out.println("Incorrect password.");
            return;
        }
        System.out.println("Login successful.");
        Coordinator loggedInCoordinator = new Coordinator(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4],
                parts[5], parts[6], parts[7]);
        coordinatorIn(loggedInCoordinator);
    }

    public static void adminLogin(){
        File file = new File("ADMIN_LIST.txt");
        if (!file.exists()) {
            System.out.println("No Admin Records found.");
            return;
        }
        String email = DataValidation.validateEmailAddress();
        String adminData = FileHandler.findRecord("ADMIN_LIST.txt", email, 4);
        if (adminData == null) {
            System.out.println("Admin account not found");
            return;
        }
        String[] parts = adminData.split("\\s*\\|\\s*");

        String password = DataValidation.validatePassword();
        if (!parts[7].equals(password)) {
            System.out.println("Incorrect password.");
            return;
        }
        System.out.println("Login successful.");
        Admin loggedInAdmin = new Admin(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4],
                parts[5], parts[6], parts[7]);
        adminIn(loggedInAdmin);
    }

    public static void adopterIn(Customer loggedInCustomer) {
        listOfUsers.add(loggedInCustomer);
        loggedInCustomer.showMenu(DataValidation.sc);
    }
    public static void coordinatorIn(Coordinator loggedInCoordinator) {
        loggedInCoordinator.showMenu(DataValidation.sc);
    }
    public static void adminIn(Admin loggedInAdmin) {
        loggedInAdmin.showMenu(DataValidation.sc);
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
                case 1 -> customerLandingPage();
                case 2 -> coordinatorLandingPage();
                case 3 -> adminLandingPage();
                case 0 -> isDone = true;
            }
        }
        System.out.println("Thank you for using the program!");
    }
}
