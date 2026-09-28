package users;

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
        DisplayUtils.printMenu("Welcome, Customer!",
                "View All Available Pets",
                "Search by Category",
                "Filter by Price",
                "Request Adoption",
                "Cancel Adoption");
    }
}
