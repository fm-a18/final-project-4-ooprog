package users;

import utils.DataValidation;
import utils.DisplayUtils;

import java.util.Scanner;

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
            int choice = DataValidation.intChoiceValidation("Select Option",
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

    private void viewAllPets() {
    }

    private void searchByCategory(Scanner sc) {
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
