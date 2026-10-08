package users;

import java.util.Scanner;

public abstract class User {
    private final String userID;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String address;
    private final int age;
    private final String phoneNumber;
    private final String password;

    public User(String userID, String firstName, String lastName, String emailAddress,
                String address, int age, String phoneNumber, String password){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.address = address;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    private static  int ctr = 0;

    public String getUserID(){
        ctr++;
        return "UD" + String.format("%07d", ctr);
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public String getAddress(){
        return address;
    }

    public int getAge(){
        return age;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getPassword(){
        return password;
    }

    public abstract String getRole();

    public abstract void showMenu(Scanner sc);
}
