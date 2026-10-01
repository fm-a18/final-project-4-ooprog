package users;

import inventory.Pet;
import strategy.PetManager;
import utils.DataValidation;
import utils.DisplayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;


public class Customer extends User{
    public Customer(String firstName, String lastName, String emailAddress,
                 String address, String birthDay, String phoneNumber, String password){
        super(firstName, lastName, emailAddress,
                address,birthDay, phoneNumber, password);
    }

    @Override
    public String getRole(){
        return "Customer";
    }

    public void showMenu(Scanner sc){
        boolean isDone = false;

        while(!isDone){
            DisplayUtils.printMenu("CUSTOMER MENU",
                    "#Browse Pets",
                    "View All Available Pets",
                    "Search by Category",
                    "Filter by Price",

                    "#Adoption",
                    "Request Adoption",
                    "Cancel Adoption Request",

                    "#Return",
                    "Request Pet Return"
            );
            int choice = DataValidation.intChoiceValidation(sc,"Select Option",
                    1, 2, 3, 4, 5, 6, 0);

            switch(choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory(sc);
                case 3 -> filterByPrice(sc);
                case 4 -> requestAdoption(sc);
                case 5 -> cancelAdoption(sc);
                case 6 -> requestReturn(sc);
                case 0 -> isDone = true;
            }
        }
    }

    ArrayList<Pet> pets = PetManager.getInstance().getAllPets();

    private void viewAllPets() {
        if(pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.displayPetsForCustomer(pets);
    }

    private void searchByCategory(Scanner sc) {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = false;
        while(!isDone){
            DisplayUtils.printMenu("SEARCH BY CATEGORY",
                    "#Select Category",
                    "Pet Name",
                    "Type",
                    "Breed",
                    "Gender",
                    "Age"
            );
            int choice = DataValidation.intChoiceValidation(sc, "Select Option",
                    1, 2, 3, 4, 5, 0);

            switch(choice){
                case 1: //SEARCH PET BY NAME
                    String name = DataValidation.validateName(sc, "Enter Pet Name: ");

                    ArrayList<Pet> petNames = searchPets(name, pets, Pet::getName);
                    if(petNames.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petNames);
                    break;
                case 2: //SEARCH PET BY PET TYPE
                    DisplayUtils.printMenu("SEARCH BY PET TYPE",
                            "#Select Pet Type",
                            "Dog",
                            "Cat",
                            "Bird"
                    );
                    int choiceType = DataValidation.intChoiceValidation(sc, "Select Option",
                            1, 2, 3, 0);

                    String type;
                    if(choiceType == 1){type = "Dog";}
                    else if(choiceType == 2){type = "Cat";}
                    else{type = "Bird";}

                    ArrayList<Pet> petTypes = searchPets(type, pets, Pet::getType);
                    if(petTypes.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petTypes);
                    break;
                case 3: //SEARCH PET BY PET BREED
                    String breed = DataValidation.validateName(sc, "Enter Pet Breed: ");

                    ArrayList<Pet> petBreeds = searchPets(breed, pets, Pet::getBreed);
                    if(petBreeds.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petBreeds);
                    break;
                case 4: //SEARCH PET BY GENDER
                    DisplayUtils.printMenu("SEARCH BY PET GENDER",
                            "#Select Gender",
                            "Male",
                            "Female"
                    );
                    int choiceGender = DataValidation.intChoiceValidation(sc, "Select Option",
                            1, 2, 0);

                    char gender;
                    if(choiceGender == 1){gender = 'M';}
                    else{gender = 'F';}

                    ArrayList<Pet> petGender = searchPets(gender, pets, Pet::getGender);
                    if(petGender.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petGender);
                    break;
                case 5: //SEARCH PET BY AGE
                    int age = DataValidation.validatePetAge(sc, "Enter Pet Age: ");

                    ArrayList<Pet> petAge = searchPets(age, pets, Pet::getAge);
                    if(petAge.isEmpty()){
                        System.out.println("No pets found");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petAge);
                    break;
            }
        }
    }

    private ArrayList<Pet> searchPets(Object key, ArrayList<Pet> pets, Function <Pet,?> getters){
        ArrayList<Pet> petArr = new ArrayList<>();

        String cat = String.valueOf(key);

        for(Pet pet : pets){
            String category = String.valueOf(getters.apply(pet));
            if(category.equalsIgnoreCase(cat)){
                petArr.add(pet);
            }
        }

        return petArr;
    }

    private void filterByPrice(Scanner sc) {
        ArrayList<String> priceArr = new ArrayList<>();

        double startingPrice = DataValidation.
    }

    private void requestAdoption(Scanner sc) {
    }

    private void cancelAdoption(Scanner sc) {
    }

    private void requestReturn(Scanner sc) {
    }
}
