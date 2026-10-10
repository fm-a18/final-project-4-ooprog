package utils;

import inventory.Bird;
import inventory.Cat;
import inventory.Dog;
import inventory.Pet;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PetManager {
    private static PetManager instance;
    private ArrayList<Pet> pets;
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private PetManager() {
        pets = new ArrayList<>();
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public void addPet() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3);

            if (choice == 0) {
                isDone = false;
                continue;
            }

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
                    FileHandler.printRecords(FileHandler.filterRecords("PET_LIST.txt", petName, Pet::getPetName));
                    break;
                }
                case 2: {
                    String petType = DataValidation.validateString("Enter pet type:");
                    FileHandler.printRecords(FileHandler.filterRecords("PET_LIST.txt", petType, Pet::getType));
                    break;
                }
                case 3: {
                    String petBreed = DataValidation.validateString("Enter pet breed:");
                    FileHandler.printRecords(FileHandler.filterRecords("PET_LIST.txt", petBreed, Pet::getBreed));
                    break;
                }
                case 4: {
                    String petGender = DataValidation.validateString("Enter pet gender:");
                    FileHandler.printRecords(FileHandler.filterRecords("PET_LIST.txt", petGender, Pet::getGender));
                    break;
                }
                case 5: {
                    String petAge = DataValidation.validateString("Enter pet age:");
                    FileHandler.printRecords(FileHandler.filterRecords("PET_LIST.txt", petAge, Pet::getAge));
                    break;
                }
                case 6: {
                    String petStatus = DataValidation.validateString("Enter adoption status:");
                    FileHandler.printRecords(FileHandler.filterRecords("PET_LIST.txt", petStatus, Pet::getAdoptionStatus));
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
}
