package users;

import inventory.AdoptionStatus;
import inventory.Dog;
import utils.DataValidation;
import utils.DisplayUtils;
import inventory.Pet;
import utils.PetManager;
import utils.FileHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import static users.Admin.pets;
import java.time.format.DateTimeFormatter;

import static users.Admin.pets;

public class Coordinator extends User{
    public Coordinator(String firstName, String lastName, String emailAddress,
                       String address, String birthDay, String phoneNumber, String password){
        super(firstName, lastName, emailAddress,
                address,birthDay, phoneNumber, password);
    }

    @Override
    public String getRole(){
        return "Coordinator";
    }

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

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
        PetManager.getInstance().searchByCategory();
    }

    private void sortByCategory(Scanner sc) {
        PetManager.getInstance().sortByCategory(sc);
    }

    private void addPet(Scanner sc) {
        PetManager.getInstance().addPet();
    }

    private void updatePetStatus(Scanner sc) {
        PetManager.getInstance().updatePetStatus(sc);
    }

    private void requestAdoption(Scanner sc) {
        // to be filled
    }

    private void requestPetReturn(Scanner sc) {
        // to be filled
    }
}
