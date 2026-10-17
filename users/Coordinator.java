package users;

import utils.*;
import inventory.Pet;

import java.util.ArrayList;
import java.util.Scanner;
import inventory.*;
import utils.FileHandler;
import utils.RecordManager;

import static utils.RecordManager.US_EMAIL_ADDRESS;

public class Coordinator extends User {
    private static PetManager instance;
    private ArrayList<Pet> pets;

    public Coordinator(String userID, String firstName, String lastName, int age, String emailAddress,
                       String address, String phoneNumber, String password) {
        super(userID, firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole() {
        return "Coordinator";
    }

    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
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
                    "#Exit", "Log Out");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory(sc);
                case 3 -> sortByCategory(sc);
                case 4 -> addPet(sc);
                case 5 -> updatePetStatus(sc);
                case 6 -> requestAdoption();
                case 7 -> requestPetReturn();
                case 8 -> isDone = true;
            }
        }
    }

    private void viewAllPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory(Scanner sc) {
        PetManager.getInstance().searchByCategory(sc);
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

    private void requestAdoption() {
        String customerEmail = DataValidation.validateEmailAddress();
        while(!DataValidation.emailExists(customerEmail)){
            System.out.println("Email Address does not exist!");
            customerEmail = DataValidation.validateEmailAddress();
        }

        String customerDetails = FileHandler.findRecord("USER_LIST.txt", customerEmail, US_EMAIL_ADDRESS);

        String[] parts = customerDetails.split("\\s*\\|\\s*");
        Customer customer = new Customer(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5], parts[6], parts[7]);

        // FILTER TO SHOW ONLY AVAILABLE PETS viewAllPets();
        ArrayList<Pet> availablePets = customer.getAvailablePets();

        if (availablePets.isEmpty()) {
            System.out.println("No pets available for adoption.");
            return;
        }

        DisplayUtils.displayPetsForStaff(availablePets);

        customer.requestAdoptionCustomer(availablePets);
    }

    private void requestPetReturn() {
        String transactionNum = DataValidation.transactionNumberValidation(
                "Enter Transaction Number from your Request of Adoption Confirmation Message: ");

        Reservation found = null;

        for (Reservation adopted : RecordManager.viewAllPetAdoptedRecords()) {
            if (adopted.getTransactionNum().equalsIgnoreCase(transactionNum)) {
                found = adopted;
                break;
            }
        }

        if (found == null || !found.getEmail().equalsIgnoreCase(this.getEmailAddress())) {
            System.out.println("No adoption found for transaction number: " + transactionNum);
            return;
        }

        if(FileHandler.findRecord("RETURN_REQUEST", transactionNum, RES_TRANSACTION_NUM) != null){
            System.out.println("A return request for " + found.getTransactionNum() + " is already being processed.");
            return;
        }

        String adoptedPet = FileHandler.findRecord("ADOPTION_LIST.txt", transactionNum, RES_TRANSACTION_NUM);
        String dateAdopted = adoptedPet.split("\\s*\\|\\s*")[RecordManager.AD_DATE_FINALIZED];

        System.out.println("Adoption found!");
        DisplayUtils.printSummaryBox("PET DETAILS",
                new String[][] {
                        { "Customer's Name", found.getFirstName() + " " + found.getLastName()},
                        { "Transaction Number", found.getTransactionNum()},
                        { "Pet ID", found.getPetID()},
                        { "Date Adopted", dateAdopted},
                }
        );

        char returnAdoptionConfirmation = DataValidation
                .charChoiceValidation("Are you sure you want to return your pet? (y/n): ", 'y', 'n');

        if (returnAdoptionConfirmation == 'n') {
            System.out.println("Return Adoption Request Cancelled.");
            return;
        }

        ArrayList<Pet> pets = FileHandler.viewAllPetRecords();
        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(found.getPetID())) {
                pet.setAdoptionStatus(AdoptionStatus.RETURN_PENDING);
                break;
            }
        }

        FileHandler.appendRecord("RETURN_REQUEST.txt", adoptedPet);
        System.out.println(
                "Your return request is now being processed. Wait for the Admin or Coordinator to approve your request.");
    }
}
