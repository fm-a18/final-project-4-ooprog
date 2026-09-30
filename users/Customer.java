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
                    "Sort by Category",
                    "Filter by Price",

                    "#Adoption",
                    "Request Adoption",
                    "Cancel Adoption Request",

                    "#Return",
                    "Request Pet Return"
            );
            int choice = DataValidation.intChoiceValidation(sc,"Select Option",
                    1, 2, 3, 4, 5, 6, 7, 0);

            switch(choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory(sc);
                case 3 -> sortByCategory(sc);
                case 4 -> filterByPrice(sc);
                case 5 -> requestAdoption(sc);
                case 6 -> cancelAdoption(sc);
                case 7 -> requestReturn(sc);
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
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
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

    private void sortByCategory(Scanner sc) {
    }

    private void filterByPrice(Scanner sc) {
    }

    private void requestAdoption(Scanner sc) {
    }

    private void cancelAdoption(Scanner sc) {
    }

    private void requestReturn(Scanner sc) {
    }
}
