package users;

import inventory.Dog;
import utils.DataValidation;
import utils.DisplayUtils;
import inventory.Pet;
import utils.PetManager;
import utils.FileHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

import static users.Admin.pets;

public class Coordinator extends User{
    public Coordinator(String userID, String firstName, String lastName, int age, String emailAddress,
                       String address, String phoneNumber, String password){
        super(userID, firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole(){
        return "Coordinator";
    }

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public void showMenu(Scanner sc){
        boolean isDone = false;

        while(!isDone){
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
                    "Request Pet Return"
            );
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 0);

            switch(choice) {
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
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory() {
        PetManager.getInstance().searchByCategory();
    }

    private void sortByCategory() {
        // TO BE CHANGED; STRATEGY NEEDED
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

    }

    private void addPet() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = false;

        while (!isDone) {
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
                    System.out.println("Pet Added on " + LocalDate.now().format(dateFormat));
                    break;
                }
                case 2: {
                    String petID = DataValidation.validatePetIdInput("Enter Pet ID: ", "Cat");
                    String petName = DataValidation.validateString("Enter Name: ");
                    String petBreed = DataValidation.validateString("Enter Breed: ");
                    char petGender = DataValidation.charChoiceValidation("Enter Gender (M/F): ", 'M', 'F');
                    double petPrice = DataValidation.validatePriceInput("Enter price of pet: ");
                    char choicev1 = DataValidation.charChoiceValidation("Does the pet have any existing medical records? (Y/N): ", 'Y', 'N');
                    if (choicev1 == 'Y') {
                        String petMedHis = DataValidation.validateString("Enter medical history of pet: ");
                    }
                    System.out.println("Pet Added on " + LocalDate.now().format(dateFormat));
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
                    System.out.println("Pet Added on " + LocalDate.now().format(dateFormat));
                    break;
                }
                case 0: {
                    isDone = true;
                    break;
                }
            }
        }

    }

    private void updatePetStatus() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = false;

        while (!isDone) {
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

    private void requestAdoption() {
        // to be filled
    }

    private void requestPetReturn() {
        // to be filled
    }
}
