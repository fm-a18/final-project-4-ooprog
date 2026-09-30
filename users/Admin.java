package users;

import inventory.Pet;
import strategy.PetManager;
import utils.DataValidation;
import utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends User{
    public Admin(String firstName, String lastName, String emailAddress,
                 String address, String birthDay, String phoneNumber, String password){
        super(firstName, lastName, emailAddress,
                address,birthDay, phoneNumber, password);
    }

    @Override
    public String getRole(){
        return "Admin";
    }

    public void showMenu(Scanner sc){
        boolean isDone = false;

        while(!isDone){
            DisplayUtils.printMenu("ADMIN MENU",
                    "#Pet Management",
                    "View All Available Pets",
                    "Search by Category",
                    "Sort by Category",
                    "Add Pet",
                    "Update Pet Status",

                    "#Adoption Management",
                    "Approve Adoption Request",
                    "Cancel Adoption Request",
                    "Approve Return Request",

                    "#Shelter Management",
                    "Manage Incoming Pets"
            );
            int choice = DataValidation.intChoiceValidation(sc,"Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 0);

            switch(choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory(sc);
                case 3 -> sortByCategory(sc);
                case 4 -> addPet(sc);
                case 5 -> updatePetStatus(sc);
                case 6 -> approveAdoptionRequest(sc);
                case 7 -> cancelAdoptionRequest(sc);
                case 8 -> approvePetReturn(sc);
                case 9 -> manageIncomingPets(sc);
                case 0 -> isDone = true;
            }
        }
    }

    private void viewAllPets(){
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory(Scanner sc) {
    }

    private void sortByCategory(Scanner sc) {
    }

    private void addPet(Scanner sc) {
    }

    private void updatePetStatus(Scanner sc) {
    }

    private void approveAdoptionRequest(Scanner sc) {
    }

    private void cancelAdoptionRequest(Scanner sc) {
    }

    private void approvePetReturn(Scanner sc) {
    }

    private void manageIncomingPets(Scanner sc) {
    }
}
