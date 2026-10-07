package users;

import java.util.Scanner;

public abstract class User {
    private final String firstName;
    private final String lastName;
    private final int age;
    private String emailAddress;
    private String address;
    private String phoneNumber;
    private String password;

    public User(String firstName, String lastName, int age, String emailAddress,
            String address, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.emailAddress = emailAddress;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public abstract String getRole();

    public abstract void showMenu(Scanner sc);
}
