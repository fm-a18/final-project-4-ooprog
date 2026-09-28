import inventory.Pet;
import users.User;
import utils.DataValidation;
import utils.DisplayUtils;
import utils.FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static utils.DataValidation.emailExists;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static final ArrayList<User> listOfUsers = new ArrayList<>();
    public static final ArrayList<Pet> listOfPets = new ArrayList<>();

    public static void adminPermissions() {

    }

    public static void adoptionCoordinator() {

    }

    public static void adopter() {

    }

    public static void userLogin() {
        File file = new File("USER_LIST.txt");
        if (!file.exists()) {
            System.out.println("No User Records found.");
            return;
        }
        String email = DataValidation.validateEmailAddress(sc);
        String userData = FileHandler.findRecord("USER_LIST.txt", email, 2);
        if (userData == null) {
            System.out.println("Account not found");
            return;
        }
        String[] parts = userData.split("\\s*\\|\\s*");

        String password = DataValidation.validatePassword(sc);
        if (!parts[5].equals(password)) {
            System.out.println("Incorrect password.");
            return;
        }
        System.out.println("Login successful.");
    }

    public static void userSignUp() {
        String firstName = DataValidation.validateName(sc, "First");
        String lastName = DataValidation.validateName(sc, "Last");
        String emailAddress;
        boolean emailExists;
        do{
            emailAddress = DataValidation.validateEmailAddress(sc);
            emailExists = emailExists(emailAddress);
            if(emailExists){
                System.out.println("Email has already been registered. Please try a different one.");
            }
        } while(emailExists);

        String address = DataValidation.validateAddress(sc);
        String phoneNumber = DataValidation.validatePhoneNumber(sc);
        String password = DataValidation.validatePassword(sc);

        boolean isFileSaved = false;

        while (!isFileSaved) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("USER_LIST.txt", true))) {
                writer.write(String.format("%s|%s|%s|%s|%s|%s%n", firstName, lastName, emailAddress,
                        address, phoneNumber, password));
                System.out.println("User successfully registered!");
                isFileSaved = true;
            } catch (IOException e) {
                System.out.println("Error in writing file. Try again? (Y/N)");
                char retry = DataValidation.charChoiceValidation(sc, "Error in writing file. Try again? (Y/N)", 'Y', 'N');
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
                    "Login",
                    "Sign Up",
                    "Exit");
            int getRole = DataValidation.intChoiceValidation(sc, "Selection Option", 1, 2, 3);
            switch (getRole) {
                case 1 -> userLogin();
                case 2 -> userSignUp();
                case 3 -> isDone = true;
            }
        }

    }
}
