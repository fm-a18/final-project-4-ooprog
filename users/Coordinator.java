package users;

import utils.DataValidation;
import utils.DisplayUtils;
import inventory.Pet;
import utils.PetManager;
import java.util.ArrayList;
import java.util.Scanner;
import inventory.*;

public class Coordinator extends User {
    private static PetManager instance;
    private ArrayList<Pet> pets;

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
                    "Update Pet Status",

                    "#Customer Assistance",
                    "Request Adoption",
                    "Request Pet Return",
                    "#Exit", "Log Out");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory(sc);
                case 3 -> sortByCategory(sc);
                case 4 -> addPet(sc);
                case 5 -> updatePetStatus(sc);
                case 6 -> requestAdoption();
                case 7 -> requestPetReturn();
                case 8 -> isDone = true;
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
        PetManager.getInstance().searchByCategory(sc);
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
//        Customer.requestAdoption(); to fix
    }

    private void requestPetReturn() {
//        Customer.requestReturn(); to fix
    }
}
