package users;

import inventory.AdoptionStatus;
import inventory.Dog;
import utils.DataValidation;
import utils.DisplayUtils;
import inventory.Pet;
import utils.PetManager;
import utils.FileHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import static users.Admin.pets;
import java.time.format.DateTimeFormatter;

import static users.Admin.pets;

public class Coordinator extends User {
    public Coordinator(String userID, String firstName, String lastName, int age, String emailAddress,
            String address, String phoneNumber, String password) {
        super(userID, firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole() {
        return "Coordinator";
    }

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("COORDINATOR MENU",
                    "#Pet Management",
                    "View All Available Pets",
                    "Search by Category",
                    "Sort by Category",
                    "Add Pet",
                    "Update Pet Status",

                    "#Customer Assistance",
                    "Request Adoption",
                    "Cancel Adoption Request",
                    "Request Pet Return");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 0);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> sortByCategory();
                case 4 -> addPet();
                case 5 -> updatePetStatus();
                case 6 -> requestAdoption();
                case 7 -> cancelAdoptionRequest();
                case 8 -> requestPetReturn();
                case 0 -> isDone = true;
            }
        }
    }

    private void viewAllPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory(Scanner sc) {
        PetManager.getInstance().searchByCategory();
    }

    private void sortByCategory(Scanner sc) {
        PetManager.getInstance().sortByCategory(sc);
    }

    private void addPet(Scanner sc) {
        PetManager.getInstance().addPet();
    }

    private void updatePetStatus(Scanner sc) {
        PetManager.getInstance().updatePetStatus(sc);
    }

    private void requestAdoption() {
        // to be filled
    }

    private void requestPetReturn() {
        // to be filled
    }
}
