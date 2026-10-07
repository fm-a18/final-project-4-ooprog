package users;

import inventory.Pet;
import utils.FileHandler;
import utils.PetManager;
import utils.DataValidation;
import utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Function;

public class Customer extends User {
    public Customer(String firstName, String lastName, int age, String emailAddress,
            String address, String phoneNumber, String password) {
        super(firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("CUSTOMER MENU",
                    "#Browse Pets",
                    "View All Available Pets",
                    "Search by Category",
                    "Filter by Price",

                    "#Adoption",
                    "Request Adoption",
                    "Cancel Adoption Request",

                    "#Return",
                    "Request Pet Return");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 0);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> searchSpecificPets();
                case 4 -> filterByPrice();
                case 5 -> requestAdoption();
                case 6 -> cancelAdoption();
                case 7 -> requestReturn();
                case 0 -> isDone = true;
            }
        }
    }

    ArrayList<Pet> pets = PetManager.getInstance().getAllPets();

    private void viewAllPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.displayPetsForCustomer(pets);
    }

    private void searchByCategory() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("SEARCH BY CATEGORY",
                    "#Select Category",
                    "Pet Name",
                    "Type",
                    "Breed",
                    "Gender",
                    "Age");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 0);

            switch (choice) {
                case 1: // SEARCH PET BY NAME
                    String name = DataValidation.validateString("Enter Pet Name: ");

                    ArrayList<Pet> petNames = FileHandler.filterRecords("PETS_LIST.txt", name, Pet::getPetName);

                    if (petNames.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petNames);
                    break;
                case 2: // SEARCH PET BY PET TYPE
                    DisplayUtils.printMenu("SEARCH BY PET TYPE",
                            "#Select Pet Type",
                            "Dog",
                            "Cat",
                            "Bird");
                    int choiceType = DataValidation.intChoiceValidation("Select Option",
                            1, 2, 3, 0);

                    String type;
                    if (choiceType == 1) {
                        type = "Dog";
                    } else if (choiceType == 2) {
                        type = "Cat";
                    } else {
                        type = "Bird";
                    }

                    ArrayList<Pet> petTypes = FileHandler.filterRecords("PETS_LIST.txt", type, Pet::getType);
                    if (petTypes.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petTypes);
                    break;
                case 3: // SEARCH PET BY PET BREED
                    String breed = DataValidation.validateString("Enter Pet Breed: ");

                    ArrayList<Pet> petBreeds = FileHandler.filterRecords("PETS_LIST.txt", breed, Pet::getBreed);
                    if (petBreeds.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petBreeds);
                    break;
                case 4: // SEARCH PET BY GENDER
                    DisplayUtils.printMenu("SEARCH BY PET GENDER",
                            "#Select Gender",
                            "Male",
                            "Female");
                    int choiceGender = DataValidation.intChoiceValidation("Select Option",
                            1, 2, 0);

                    char gender;
                    if (choiceGender == 1) {
                        gender = 'M';
                    } else {
                        gender = 'F';
                    }

                    ArrayList<Pet> petGender = FileHandler.filterRecords("PETS_LIST.txt", String.valueOf(gender),
                            Pet::getGender);
                    if (petGender.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petGender);
                    break;
                case 5: // SEARCH PET BY AGE
                    int age = DataValidation.validatePetAgeInput("Enter Pet Age: ");

                    ArrayList<Pet> petAge = FileHandler.filterRecords("PETS_LIST.txt", String.valueOf(age),
                            Pet::getAge);
                    if (petAge.isEmpty()) {
                        System.out.println("No pets found");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petAge);
                    break;
            }
        }
    }

    private void searchSpecificPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        String petID = DataValidation.validateString("Enter Pet ID: ");

        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                DisplayUtils.displayPetDetails(pet);
                return;
            } else {
                System.out.println("No pets found.");
                return;
            }
        }
    }

    private void filterByPrice() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("FILTER BY PRICE",
                    "#Select Option",
                    "Ascending Price",
                    "Descending Price",
                    "Price Range");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 0);

            switch (choice) {
                case 1:
                    ArrayList<Pet> sortedPetsAscending = sortByPrice(1, pets);
                    if (sortedPetsAscending.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(sortedPetsAscending);
                    break;
                case 2:
                    ArrayList<Pet> sortedPetsDescending = sortByPrice(2, pets);
                    if (sortedPetsDescending.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(sortedPetsDescending);
                    break;
                case 3:
                    ArrayList<Pet> filteredPets = filterByPriceRange();

                    if (filteredPets.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(filteredPets);
                    break;
                case 0:
                    isDone = true;
                    break;
            }
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

    private ArrayList<Pet> filterByPriceRange() {
        ArrayList<Pet> filteredPets = new ArrayList<>();
        double startingPrice = DataValidation.validatePriceInput("Enter Starting Price: ");
        double endingPrice = DataValidation.validatePriceInput("Enter Ending Price: ");

        for (Pet pet : pets) {
            if (pet.getPrice() >= startingPrice && pet.getPrice() <= endingPrice) {
                filteredPets.add(pet);
            }
        }
        return filteredPets;
    }

    private void requestAdoption() {

    }

    private void cancelAdoption() {
    }

    private void requestReturn() {
    }
}
