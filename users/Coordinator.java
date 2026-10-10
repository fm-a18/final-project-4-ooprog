package users;

import utils.DataValidation;
import utils.DisplayUtils;
import utils.PetManager;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Coordinator extends User{
    public Coordinator(String userID, String firstName, String lastName, int age, String emailAddress,
                       String address, String phoneNumber, String password){
        super(userID, firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole(){
        return "Coordinator";
    }

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public void showMenu(Scanner sc){
        boolean isDone = false;

        while(!isDone){
            DisplayUtils.printMenu("COORDINATOR MENU",
                    "#Pet Management",
                    "View All Available Pets",
                    "Search by Category",
                    "Sort by Category",
                    "Add Pet",
                    "Update Pet Status",

                    "#Customer Assistance",
                    "Request Adoption",
                    "Request Pet Return",
                    "#Exit",
                    "Log Out"
            );
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8);

            switch(choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> sortByCategory();
                case 4 -> addPet();
                case 5 -> updatePetStatus();
                case 6 -> requestAdoption();
                case 7 -> requestPetReturn();
                case 8 -> isDone = true;
            }
        }
    }

    private void viewAllPets() {
        PetManager.getInstance().getAllPets();
    }

    private void searchByCategory() {
        PetManager.getInstance().searchByCategory();
    }

    private void sortByCategory() {
        // TO BE CHANGED; STRATEGY NEEDED
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

    }

    private void addPet() {
        PetManager.getInstance().addPet();
    }

    private void updatePetStatus() {
        // to be filled
    }

    private void requestAdoption() {
        // to be filled
    }

    private void requestPetReturn() {
        // to be filled
    }
}
