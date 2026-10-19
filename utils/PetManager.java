package utils;

import inventory.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;

public class PetManager {
    private static PetManager instance;
    private final ArrayList<Pet> pets;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);

    private PetManager() {
        pets = FileHandler.viewAllPetRecords();
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public void addPet() {
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3);

            String petID = DataValidation.validatePetIdInput();
            String petName = DataValidation.validateString("Enter Name: ");
            String petBreed = DataValidation.validateString("Enter Breed: ");
            char petGender = DataValidation.charChoiceValidation("Enter Gender (M/F): ", 'M', 'F');
            int petAge = DataValidation.validatePetAgeInput("Enter age: ");
            double petPrice = DataValidation.validatePriceInput("Enter price of pet: ");

            Pet pet = switch (choice) {
                case 1 -> new Dog(petID, petName, petBreed, petGender, petAge, petPrice);
                case 2 -> new Cat(petID, petName, petBreed, petGender, petAge, petPrice);
                default -> new Bird(petID, petName, petBreed, petGender, petAge, petPrice);
            };

            char hasRecords = DataValidation.charChoiceValidation("Does the pet have any existing medical records? (Y/N): ", 'Y', 'N');
            if (hasRecords == 'Y') {
                String petMedCon = DataValidation.validateString("Enter medical condition/treatment name: ");
                String petMedHis = String.valueOf(DataValidation.validateDate("Enter date of the condition/treatment: "));
                findPetID(petID).addMedicalHistory(petMedHis, petMedCon);
            }

            System.out.println("Pet Added on " + LocalDate.now().format(dateFormat));

            pets.add(pet);
            boolean isFileSaved = false; //Double check
            while (!isFileSaved) {
                try (BufferedWriter writer =
                             new BufferedWriter(new FileWriter(RecordManager.FILE_PET_LIST, true))) {

                    writer.write(String.format(
                            "%s|%s|%s|%c|%d|%.2f%n",
                            petID,
                            petName,
                            petBreed,
                            petGender,
                            petAge,
                            petPrice
                    ));

                    System.out.println("Pet successfully added!");
                    isFileSaved = true;

                } catch (IOException e) {
                    System.out.println("Error writing file.");
                    char retry = DataValidation.charChoiceValidation("Try again?", 'Y', 'N');
                    if (retry != 'Y') {
                        isFileSaved = true;
                    }
                }
            }
            isDone = false;
        }
    }

    public ArrayList<Pet> getAllPets() {
        if (pets.isEmpty()) {
            ArrayList<Pet> all = PetManager.getInstance().getAllPets();
            if (all == null) {
                return null;
            }
        }

        return pets;
    }

    public void searchByCategory(Scanner sc) {
        if (pets.isEmpty()) {
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
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petName, Pet::getPetName);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 2: {
                    String petType = DataValidation.validateString("Enter pet type:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petType, Pet::getType);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 3: {
                    String petBreed = DataValidation.validateString("Enter pet breed:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petBreed, Pet::getBreed);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 4: {
                    String petGender = DataValidation.validateString("Enter pet gender:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petGender, Pet::getGender);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 5: {
                    String petAge = DataValidation.validateString("Enter pet age:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petAge, Pet::getAge);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 6: {
                    String petStatus = DataValidation.validateString("Enter adoption status:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petStatus, Pet::getAdoptionStatus);
                    DisplayUtils.displayPetsForStaff(listOfPet);
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

    public boolean updatePetStatus(String petID, AdoptionStatus adoptionStatus) {
        Pet pet = findPetID(petID);
        if (pet == null) {
            return false;
        }

        pet.setAdoptionStatus(adoptionStatus);

        boolean isSaved = FileHandler.updateRecord(RecordManager.FILE_PET_LIST, pet.getPetID(),
                0, pet.toFileString());
        if (!isSaved) {
            System.out.println("Warning: " + petID + " is not in " + RecordManager.FILE_PET_LIST + ". Status changed in memory only.");
        }

        return true;
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

            FileHandler.sortCategory(RecordManager.FILE_PET_LIST, choice, choicev1, choicev2 == 1);
        }


    }
}
