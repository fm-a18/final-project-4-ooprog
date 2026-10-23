package users;

import inventory.Pet;
import utils.*;

import static utils.RecordManager.FILE_PET_LIST;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Customer extends User {
    public Customer(String userID, String firstName, String lastName, int age, String emailAddress,
            String address, String phoneNumber, String password) {
        super(userID, firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("CUSTOMER MENU",
                    "#Browse Pets",
                    "View All Available Pets",
                    "Search by Category",
                    "Search Specific Pets",
                    "Filter by Price",

                    "#Adoption",
                    "Request Adoption",
                    "Cancel Adoption Request",

                    "#Return",
                    "Request Pet Return",

                    "#Exit",
                    "Log Out");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> findSpecificPets();
                case 4 -> filterByPrice();
                case 5 -> PetManager.getInstance().requestAdoption(this);
                case 6 -> PetManager.getInstance().cancelAdoption(this);
                case 7 -> PetManager.getInstance().requestReturn(this);
                case 8 -> isDone = true;
            }
        }
        System.out.println("Logging out...");
    }

    private void viewAllPets() {
        ArrayList<Pet> availablePets = PetManager.getInstance().getAvailablePets();

        if (availablePets.isEmpty()) {
            DisplayUtils.centeredTitle("VIEW ALL AVAILABLE PETS", 100);
            System.out.println("No available pets found.");
            return;
        }

        showResults(availablePets, "VIEW ALL AVAILABLE PETS");
    }

    private void searchByCategory() {
        ArrayList<Pet> availablePets = PetManager.getInstance().getAvailablePets();

        if (availablePets.isEmpty()) {
            DisplayUtils.centeredTitle("SEARCH BY CATEGORY", 100);
            System.out.println("No available pets found.");
            return;
        }

        DisplayUtils.printMenu("SEARCH BY CATEGORY",
                "#Select Category",
                "Pet Name",
                "Type",
                "Breed",
                "Gender",
                "Age");
        int choice = DataValidation.intChoiceValidation("Select Option",
                1, 2, 3, 4, 5);

        switch (choice) {
            case 1: // SEARCH PET BY NAME
                DisplayUtils.centeredTitle("SEARCH PET BY NAME", 60);
                String name = DataValidation.validateString("Enter Pet Name");

                showResults(FileHandler.filterRecords(FILE_PET_LIST, name, Pet::getPetName),
                        "VIEW BY PET NAME: " + name.toUpperCase());
                break;
            case 2: // VIEW PET BY PET TYPE
                ArrayList<Pet> pets = getSpecificPetDetailsPerType();
                String petType = "";
                for (Pet pet : pets) {
                    petType = pet.getType();
                    break;
                }
                showResults(pets, "VIEW BY PET TYPE: " + petType.toUpperCase());
                break;
            case 3: // SEARCH PET BY PET BREED
                DisplayUtils.centeredTitle("SEARCH PET BY BREED", 60);
                String breed = DataValidation.validateString("Enter Pet Breed");

                showResults(FileHandler.filterRecords(FILE_PET_LIST, breed, Pet::getBreed),
                        "VIEW BY PET BREED " + breed.toUpperCase());
                break;
            case 4: // SEARCH PET BY GENDER
                DisplayUtils.printMenu("SEARCH BY PET GENDER",
                        "#Select Gender",
                        "Male",
                        "Female");
                int genderChoice = DataValidation.intChoiceValidation("Select Option", 1, 2);

                String gender = (genderChoice == 1) ? "Male" : "Female";
                showResults(FileHandler.filterRecords(FILE_PET_LIST, gender, Pet::getGender),
                        "VIEW BY PET GENDER " + gender.toUpperCase());
                break;
            case 5: // SEARCH PET BY AGE
                DisplayUtils.centeredTitle("SEARCH PET BY AGE", 60);
                int age = DataValidation.validatePetAgeInput();

                showResults(FileHandler.filterRecords(FILE_PET_LIST, age, Pet::getAge), "VIEW BY PET AGE " + age);
                break;
        }
    }

    private void showResults(ArrayList<Pet> pets, String title) {
        ArrayList<Pet> available = (pets == null) ? new ArrayList<>()
                : PetManager.getInstance().availablePets(pets);

        if (available.isEmpty()) {
            DisplayUtils.centeredTitle(title, 100);
            System.out.println("No available pet(s) found.");
            return;
        }

        DisplayUtils.customerPetHeader(title);
        DisplayUtils.displayPetsForCustomer(available);
    }

    private ArrayList<Pet> getSpecificPetDetailsPerType() {
        ArrayList<Pet> availablePets = PetManager.getInstance().getAvailablePets();
        String type = "";

        if (availablePets.isEmpty()) {
            DisplayUtils.centeredTitle("FIND SPECIFIC PETS [DOG/CAT/BIRD]", 100);
            System.out.println("No available pets found.");
            return null;
        }

        DisplayUtils.printMenu("PET TYPE",
                "#Select Pet Type",
                "Dog",
                "Cat",
                "Bird");
        int choiceType = DataValidation.intChoiceValidation("Select Option",
                1, 2, 3);

        switch (choiceType) {
            case 1 -> type = "dog";
            case 2 -> type = "cat";
            case 3 -> type = "bird";
            case 0 -> type = "";
        }

        return FileHandler.filterRecords(RecordManager.FILE_PET_LIST, type, Pet::getType);
    }

    private void findSpecificPets() {
        ArrayList<Pet> pets = getSpecificPetDetailsPerType();
        if (pets == null || pets.isEmpty()) {
            return;
        }

        ArrayList<Pet> petTypes = PetManager.getInstance().availablePets(pets);

        showResults(petTypes, "FIND SPECIFIC PETS");

        if (petTypes == null || petTypes.isEmpty()) {
            return;
        }

        char viewDetailsChoice = DataValidation
                .charChoiceValidation("Do you want to view specific pet details?", 'Y', 'N');

        if (viewDetailsChoice == 'Y') {
            specificPetDetails(petTypes);
        }
    }

    private void specificPetDetails(ArrayList<Pet> petTypes) { // EDIT THIS
        DisplayUtils.centeredTitle("VIEW SPECIFIC PET DETAILS", 60);
        String petID = DataValidation.validatePetIdInput();

        for (Pet pet : petTypes) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                DisplayUtils.displayPetDetails(pet);
                return;
            }
        }
        System.out.println("No pet found with the given ID.");
    }

    private void filterByPrice() {
        ArrayList<Pet> availablePets = PetManager.getInstance().getAvailablePets();

        if (availablePets.isEmpty()) {
            DisplayUtils.centeredTitle("FILTER BY PRICE", 100);
            System.out.println("No available pets found.");
            return;
        }

        DisplayUtils.printMenu("FILTER BY PRICE",
                "#Select Option",
                "Ascending Price",
                "Descending Price",
                "Price Range");
        int choice = DataValidation.intChoiceValidation("Select Option",
                1, 2, 3);

        switch (choice) {
            case 1:
                showResults(sortByPrice(choice, availablePets), "SORT BY ASCENDING PRICE");
                break;
            case 2:
                showResults(sortByPrice(choice, availablePets), "SORT BY DESCENDING PRICE");
                break;
            case 3:
                showResults(filterByPriceRange(availablePets), "FILTER BY PRICE RANGE");
                break;
        }
    }

    private ArrayList<Pet> sortByPrice(int order, ArrayList<Pet> pets) {
        ArrayList<Pet> sortPrice = new ArrayList<>(pets);

        Comparator<Pet> comparator = Comparator.comparingDouble(Pet::getPrice);

        if (order == 2) {
            comparator = comparator.reversed();
        }

        sortPrice.sort(comparator);
        return sortPrice;
    }

    private ArrayList<Pet> filterByPriceRange(ArrayList<Pet> pets) {
        DisplayUtils.centeredTitle("FILTER BY PRICE RANGE", 60);
        ArrayList<Pet> filteredPets = new ArrayList<>();
        double startingPrice = DataValidation.validatePriceInput("Enter Starting Price");
        double endingPrice = DataValidation.validatePriceInput("Enter Ending Price");

        for (Pet pet : pets) {
            if (pet.getPrice() >= startingPrice && pet.getPrice() <= endingPrice) {
                filteredPets.add(pet);
            }
        }
        return filteredPets;
    }
}