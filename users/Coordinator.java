package users;

import utils.DataValidation;
import utils.DisplayUtils;
import inventory.Pet;
import utils.PetManager;
import utils.FileHandler;
import java.util.ArrayList;
import java.util.Scanner;

import static users.Admin.pets;

public class Coordinator extends User{
    public Coordinator(String userID, String firstName, String lastName, String emailAddress,
                       String address, int age, String phoneNumber, String password){
        super(userID, firstName, lastName, emailAddress,
                address, age, phoneNumber, password);
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
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory(Scanner sc) {
        // STRATEGY; TO BE CONTINUED
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Search By Category",
                    "#Pet Details",
                    "Pet Name", "Type", "Breed", "Gender", "Age",
                    "#Pet Records",
                    "Adoption Status", "Adoption History", "Medical History");

            int choice = DataValidation.intChoiceValidation("Enter choice: ", 1, 2, 3, 4, 5, 6, 7, 8, 0);

            switch (choice) {
                case 1: {
                    String petName = DataValidation.validateString("Enter pet name:");
                    FileHandler.filterRecords("PET_LIST.txt", 1, petName);
                    break;
                }
                case 2: {
                    String petType = DataValidation.validateString("Enter pet type:");
                    FileHandler.filterRecords("PET_LIST.txt", 2, petType);
                    break;
                }
                case 3: {
                    String petBreed = DataValidation.validateString("Enter pet breed:");
                    FileHandler.filterRecords("PET_LIST.txt", 3, petBreed);
                    break;
                }
                case 4: {
                    String petGender = DataValidation.validateString("Enter pet gender:");
                    FileHandler.filterRecords("PET_LIST.txt", 4, petGender);
                    break;
                }
                case 5: {
                    String petAge = DataValidation.validateString("Enter pet age:");
                    FileHandler.filterRecords("PET_LIST.txt", 5, petAge);
                    break;
                }
                case 6: {
                    String petStatus = DataValidation.validateString("Enter adoption status:");
                    FileHandler.filterRecords("PET_LIST.txt", 6, petStatus);
                    break;
                }
                case 7: {
                    // to be filled
                }
                case 8: {
                    // to be filled
                }
                case 0: {
                    isDone = false;
                    break;
                }
            }
        }
    }

    private void sortByCategory(Scanner sc) {
        // TO BE CHANGED; STRATEGY NEEDED
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

    }

    private void addPet(Scanner sc) {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3, 0);
            switch (choice) {
                case 1: {
                    String petID = DataValidation.validatePetIdInput("Enter Pet ID: ", "Dog");
                    String petFirstName = DataValidation.validateString("Enter First Name: ");
                    String petLastName = DataValidation.validateString("Enter Last Name: ");
                    String petBreed = DataValidation.validateString("Enter Breed: ");
                    char petGender = DataValidation.charChoiceValidation("Enter Gender (M/F): ", 'M', 'F');
                    double petPrice = DataValidation.validatePriceInput("Enter price of pet: ");
                    char choicev1 = DataValidation.charChoiceValidation("Does the pet have any existing medical records? (Y/N): ", 'Y', 'N');
                    if (choicev1 == 'Y') {
                        String petMedHis = DataValidation.validateString("Enter medical history of pet: ");
                    }
                    // to be filled, adoption histry
                    break;
                }
                case 2: {
                    String petID = DataValidation.validatePetIdInput("Enter Pet ID: ", "Cat");
                    String petFirstName = DataValidation.validateString("Enter First Name: ");
                    String petLastName = DataValidation.validateString("Enter Last Name: ");
                    String petBreed = DataValidation.validateString("Enter Breed: ");
                    char petGender = DataValidation.charChoiceValidation("Enter Gender (M/F): ", 'M', 'F');
                    double petPrice = DataValidation.validatePriceInput("Enter price of pet: ");
                    char choicev1 = DataValidation.charChoiceValidation("Does the pet have any existing medical records? (Y/N): ", 'Y', 'N');
                    if (choicev1 == 'Y') {
                        String petMedHis = DataValidation.validateString("Enter medical history of pet: ");
                    }
                    // to be filled, adoption histry
                    break;
                }
                case 3: {
                    String petID = DataValidation.validatePetIdInput("Enter Pet ID: ", "Bird");
                    String petFirstName = DataValidation.validateString("Enter First Name: ");
                    String petLastName = DataValidation.validateString("Enter Last Name: ");
                    String petBreed = DataValidation.validateString("Enter Breed: ");
                    char petGender = DataValidation.charChoiceValidation("Enter Gender (M/F): ", 'M', 'F');
                    double petPrice = DataValidation.validatePriceInput("Enter price of pet: ");
                    char choicev1 = DataValidation.charChoiceValidation("Does the pet have any existing medical records? (Y/N): ", 'Y', 'N');
                    if (choicev1 == 'Y') {
                        String petMedHis = DataValidation.validateString("Enter medical history of pet: ");
                    }
                    // to be filled, adoption histry
                    break;
                }
                case 0: {
                    isDone = false;
                    break;
                }
            }
        }

    }

    private void updatePetStatus(Scanner sc) {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3, 0);
            switch (choice) {
                case 1: {
                    String petID = DataValidation.validatePetIdInput("Enter existing pet ID: ", "Dog");
                    // to be filled;
                }
            }
        }
    }

    private void requestAdoption(Scanner sc) {
        // to be filled
    }

    private void requestPetReturn(Scanner sc) {
        // to be filled
    }
}
