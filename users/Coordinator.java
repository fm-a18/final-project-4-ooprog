package users;

import utils.*;
import inventory.Pet;

import java.util.ArrayList;
import java.util.Scanner;

import inventory.*;
import utils.FileHandler;
import utils.RecordManager;

import static utils.RecordManager.US_EMAIL_ADDRESS;


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

    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("COORDINATOR MENU",
                    "#Pet Management",
                    "View All Available Pets",
                    "Search by Category",
                    "Sort by Category",
                    "Add Pet",
                    "Update Pet Medical History",

                    "#Customer Assistance",
                    "Request Adoption",
                    "Cancel Pet Adoption",
                    "Request Pet Return",
                    "#Exit", "Log Out");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 9);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> sortByCategory();
                case 4 -> addPet();
                case 5 -> updatePetMedicalHistory(); //wil change to med history
                case 6 -> requestPetAdoption();
                case 7 -> cancelPetAdoption();
                case 8 -> requestPetReturn();
                case 9 -> isDone = true;
            }
        }
    }

    private void viewAllPets() {
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();

        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory() {
        PetManager.getInstance().searchByCategory();
    }

    private void sortByCategory() {
        PetManager.getInstance().sortByCategory();
    }

    private void addPet() {
        PetManager.getInstance().addPet();
    }

    private void updatePetMedicalHistory() {
        PetManager.getInstance().updatePetMedicalHistory();
    }

    private void requestPetAdoption() {
        PetManager.getInstance().requestAdoption();
    }

    private void cancelPetAdoption() {
        PetManager.getInstance().cancelAdoption();
    }

    private void requestPetReturn() {
        PetManager.getInstance().requestReturn();
    }
}
