package users;

import utils.DisplayUtils;

import java.util.Scanner;

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

    public void showMenu(Scanner sc){ //TO UPDATE
        DisplayUtils.printMenu("Welcome, Customer!",
                "View All Available Pets",
                "Search by Category",
                "Filter by Price",
                "Request Adoption",
                "Cancel Adoption");
    }
}
