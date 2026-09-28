package users;

import java.util.Scanner;

public abstract class User {
    private final String firstName;
    private final String lastName;
    private String emailAddress;
    private String address;
    private final String birthDay;
    private String phoneNumber;
    private String password;

    public User(String firstName, String lastName, String emailAddress,
                String address, String birthDay, String phoneNumber, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.address = address;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.password = password;
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

    public String getBirthDay(){
        return birthDay;
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
