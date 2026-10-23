package users;

import utils.*;
import inventory.Pet;

import java.util.ArrayList;
import java.util.Scanner;

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

    @Override
    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("COORDINATOR MENU",
                    "#Pet Management",
                    "View All Pets",
                    "Search by Category",
                    "Sort by Category",
                    "Search Specific Pet",
                    "Add Pet",
                    "Update Pet Medical History",

                    "#Customer Assistance",
                    "Request Adoption",
                    "Cancel Pet Adoption",
                    "Request Pet Return",
                    "#Exit", "Log Out");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> sortByCategory();
                case 4 -> searchForSpecificPet();
                case 5 -> addPet();
                case 6 -> updatePetMedicalHistory();
                case 7 -> requestPetAdoption();
                case 8 -> cancelPetAdoption();
                case 9 -> requestPetReturn();
                case 10 -> isDone = true;
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

    private void searchForSpecificPet() {
        PetManager.getInstance().viewSpecificPet();
    }

    private void addPet() {
        PetManager.getInstance().addPet();
    }

    private void updatePetMedicalHistory() {
        PetManager.getInstance().updatePetMedicalHistory();
    }

    private void requestPetAdoption() {
        PetManager.getInstance().requestAdoption(new Customer(getUserID(), getFirstName(), getLastName(),
                getAge(), getEmailAddress(), getAddress(), getPhoneNumber(), getPassword()));
    }

    private void cancelPetAdoption() {
        PetManager.getInstance().cancelAdoption(new Customer(getUserID(), getFirstName(), getLastName(),
                getAge(), getEmailAddress(), getAddress(), getPhoneNumber(), getPassword()));
    }

    private void requestPetReturn() {
        PetManager.getInstance().requestReturn(new Customer(getUserID(), getFirstName(), getLastName(),
                getAge(), getEmailAddress(), getAddress(), getPhoneNumber(), getPassword()));
    }
}