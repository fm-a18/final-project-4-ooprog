package users;

import utils.DataValidation;
import utils.DisplayUtils;
import inventory.Pet;
import utils.PetManager;
import utils.FileHandler;
import java.util.ArrayList;
import java.util.Scanner;

public class Coordinator extends User{
    public Coordinator(String firstName, String lastName, String emailAddress,
                       String address, String birthDay, String phoneNumber, String password){
        super(firstName, lastName, emailAddress,
                address,birthDay, phoneNumber, password);
    }

    @Override
    public String getRole(){
        return "Coordinator";
    }

    public void showMenu(Scanner sc){
        DisplayUtils.printMenu("Pet Management",
                "View All Available Pets",
                "Search by Category",
                "Sort by Category",
                "Add Pet",
                "Update Pet Status"
        );

        DisplayUtils.printMenu("Customer Assistance",
                "Request for Adoption",
                "Request for Pet Return"
        );

        boolean isDone = false;

        while(!isDone){
            DisplayUtils.printMenu("ADMIN MENU",
                    "#Pet Management",
                    "View All Available Pets",
                    "Search by Category",
                    "Sort by Category",
                    "Add Pet",
                    "Update Pet Status",

                    "#Customer Assistance",
                    "Request Adoption",
                    "Request Pet Return"
            );
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 0);

            switch(choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory(sc);
                case 3 -> sortByCategory(sc);
                case 4 -> addPet(sc);
                case 5 -> updatePetStatus(sc);
                case 6 -> requestAdoption(sc);
                case 7 -> requestPetReturn(sc);
                case 0 -> isDone = true;
            }
        }
    }

    private void viewAllPets() {
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory(Scanner sc) {
        DisplayUtils.printMenu("Search By Category",
                "#Pet Details",
                "Pet Name", "Type", "Breed", "Gender", "Age",
                "#Pet Records",
                "Adoption Status", "Adoption History", "Medical History");

        int choice = DataValidation.intChoiceValidation("Enter choice: ", 1, 2, 3, 4, 5, 6, 7, 8, 0);

        switch(choice) {
            case 1: {
                String petName = DataValidation.validateString("Enter pet name:");
                FileHandler.filterRecords("PET_LIST.txt", 1, petName);
            }
            case 2: {
                String petType = DataValidation.validateString("Enter pet type:");
                FileHandler.filterRecords("PET_LIST.txt", 2, petType);
            }
            case 3: {
                String petBreed = DataValidation.validateString("Enter pet breed:");
                FileHandler.filterRecords("PET_LIST.txt", 3, petBreed);
            }
            case 4: {
                String petGender = DataValidation.validateString("Enter pet gender:");
                FileHandler.filterRecords("PET_LIST.txt", 4, petGender);
            }
            case 5: {
                String petAge = DataValidation.validateString("Enter pet age:");
                FileHandler.filterRecords("PET_LIST.txt", 5, petAge);
            }
            case 6: {
                String petStatus = DataValidation.validateString("Enter adoption status:");
                FileHandler.filterRecords("PET_LIST.txt", 6, petStatus);
            }
            case 7: {
                // to be filled
            }
            case 8: {
                // to be filled
            }
        }
    }

    private void sortByCategory(Scanner sc) {
    }

    private void addPet(Scanner sc) {
    }

    private void updatePetStatus(Scanner sc) {
    }

    private void requestAdoption(Scanner sc) {
    }

    private void requestPetReturn(Scanner sc) {
    }
}
