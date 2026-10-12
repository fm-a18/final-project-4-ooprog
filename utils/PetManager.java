package utils;

import inventory.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;

import static users.Admin.pets;

public class PetManager {
    private static PetManager instance;
    private ArrayList<Pet> pets;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);

    private PetManager() {
        pets = new ArrayList<>();
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public void addPet(){
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3);

            String type = switch (choice) {
                case 1 -> "Dog";
                case 2 -> "Cat";
                default -> "Bird";
            };

            String petID = DataValidation.validatePetIdInput("Enter Pet ID: ", type);
            String petName = DataValidation.validateString("Enter Name: ");
            String petBreed = DataValidation.validateString("Enter Breed: ");
            char petGender = DataValidation.charChoiceValidation("Enter Gender (M/F): ", 'M', 'F');
            int petAge = DataValidation.validatePetAgeInput("Enter age: ");
            double petPrice = DataValidation.validatePriceInput("Enter price of pet: ");
            char hasRecords = DataValidation.charChoiceValidation("Does the pet have any existing medical records? (Y/N): ", 'Y', 'N');
            if (hasRecords == 'Y') {
                String petMedHis = DataValidation.validateString("Enter medical history of pet: ");
            }

            System.out.println("Pet Added on " + LocalDate.now().format(dateFormat));

            Pet pet = switch (choice) {
                case 1 -> new Dog(petID, petName, petBreed, petGender, petAge, petPrice);
                case 2 -> new Cat(petID, petName, petBreed, petGender, petAge, petPrice);
                default -> new Bird(petID, petName, petBreed, petGender, petAge, petPrice);
            };
            pets.add(choice, pet);
            isDone = false;
        }
    }

    public ArrayList<Pet> getAllPets() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return null;
        }

        return pets;
    }

    public void searchByCategory(){
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
                    "Adoption Status", "#Exit", "Log Out");

            int choice = DataValidation.intChoiceValidation("Enter choice: ", 1, 2, 3, 4, 5, 6, 7);

            switch (choice) {
                case 1: {
                    String petName = DataValidation.validateString("Enter pet name:");
                    ArrayList<Pet> listpet = FileHandler.filterRecords("PET_LIST.txt", petName, Pet::getPetName);
                    DisplayUtils.displayPetsForStaff(listpet);
                    break;
                }
                case 2: {
                    String petType = DataValidation.validateString("Enter pet type:");
                    ArrayList<Pet> listpet = FileHandler.filterRecords("PET_LIST.txt", petType, Pet::getType);
                    DisplayUtils.displayPetsForStaff(listpet);
                    break;
                }
                case 3: {
                    String petBreed = DataValidation.validateString("Enter pet breed:");
                    ArrayList<Pet> listpet = FileHandler.filterRecords("PET_LIST.txt", petBreed, Pet::getBreed);
                    DisplayUtils.displayPetsForStaff(listpet);
                    break;
                }
                case 4: {
                    String petGender = DataValidation.validateString("Enter pet gender:");
                    ArrayList<Pet> listpet = FileHandler.filterRecords("PET_LIST.txt", petGender, Pet::getGender);
                    DisplayUtils.displayPetsForStaff(listpet);
                    break;
                }
                case 5: {
                    String petAge = DataValidation.validateString("Enter pet age:");
                    ArrayList<Pet> listpet = FileHandler.filterRecords("PET_LIST.txt", petAge, Pet::getAge);
                    DisplayUtils.displayPetsForStaff(listpet);
                    break;
                }
                case 6: {
                    String petStatus = DataValidation.validateString("Enter adoption status:");
                    ArrayList<Pet> listpet = FileHandler.filterRecords("PET_LIST.txt", petStatus, Pet::getAdoptionStatus);
                    DisplayUtils.displayPetsForStaff(listpet);
                    break;
                }
                case 7: {
                    isDone = false;
                    break;
                }
            }
        }
    }

    public Pet findPetID(String petID) {
        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                return pet;
            }
        }
        return null;
    }

    public void updatePetStatus(Scanner sc) {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird", "#Exit", "Back");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3, 4);
            String cat = "";

            if (choice==4) {
                isDone = false;
                continue;
            }
            switch (choice) {
                case 1 -> cat = "Dog";
                case 2 -> cat = "Cat";
                default -> cat = "Bird";
            }

            String petID = DataValidation.validatePetIdInput("Enter existing pet ID: ", cat);
            PetManager.getInstance().findPetID(petID).setAdoptionStatus(AdoptionStatus.ADOPTED);
        }
    }

    public void sortByCategory(Scanner sc) {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3);

            DisplayUtils.printMenu("Sort By..", "Age", "Price", "Gender", "#Exit", "Back");
            int choicev1 = DataValidation.intChoiceValidation("Enter choice: ", 1, 2, 3, 4);

            if (choicev1 == 4) {
                return;
            }

            DisplayUtils.printMenu("Order: ", "Ascending", "Descending");
            int choicev2 = DataValidation.intChoiceValidation("Enter choice: ", 1, 2);

            FileHandler.sortCategory("PET_LIST.txt", choice, choicev1, choicev2 == 1);
        }
    }
}
