package utils;

import inventory.Pet;
import java.util.ArrayList;

public class PetManager {
    private static PetManager instance;
    private ArrayList<Pet> pets;

    private PetManager() {
        pets = new ArrayList<>();
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
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
        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("Search By Category",
                    "#Pet Details",
                    "Pet Name", "Type", "Breed", "Gender", "Age",
                    "#Pet Records",
                    "Adoption Status", "Adoption History", "Medical History");

            int choice = DataValidation.intChoiceValidation("Enter choice: ", 1, 2, 3, 4, 5, 6, 7, 8, 0);

            switch (choice) {
                case 1: {
                    String petName = DataValidation.validateString("Enter pet name:");
                    ArrayList<String> filterpetName = FileHandler.filterRecords("PET_LIST.txt", 1, petName);
                    return DisplayUtils.displayPetsForStaff(filterpetName);
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
                    isDone = true;
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
