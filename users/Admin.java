package users;

import inventory.AdoptionStatus;
import inventory.Pet;
import utils.PetManager;
import utils.DataValidation;
import utils.DisplayUtils;
import utils.FileHandler;

import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends User {
    public static final Scanner sc = new Scanner(System.in);

    public Admin(String firstName, String lastName, int age, String emailAddress,
            String address, String phoneNumber, String password) {
        super(firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public void showMenu() {
        boolean isDone = false;

        while (!isDone) {
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
                    "Manage Incoming Pets");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 0);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> sortByCategory();
                case 4 -> addPet();
                case 5 -> updatePetStatus();
                case 6 -> approveAdoptionRequest();
                case 7 -> cancelAdoptionRequest();
                case 8 -> approvePetReturn();
                case 9 -> manageIncomingPets();
                case 0 -> isDone = true;
            }
        }
    }

    public static final ArrayList<Pet> pets = new ArrayList<>();

    private void viewAllPets() {
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory() {
        ArrayList<Pet> pets = PetManager.getInstance().getAllPets();

    }

    private void sortByCategory() {
    }

    private void addPet() {
        ArrayList<Pet> pets = PetManager.getInstance().addPet(pet);
    }

    private void updatePetStatus() {
        String searchForID = DataValidation.petIdValidation();
    }

    private String findAndConfirmTransaction(String fileName, String action) {
        String findTransactionNum = DataValidation.transactionNumValidation();
        String record = FileHandler.findRecord("RESERVE.txt", findTransactionNum, 0);

        if (record == null) {
            System.out.println("Transaction not found.");
            return null;
        }

        System.out.println("Transaction number found.");

        char approve = DataValidation.charChoiceValidation("Approve Adoption Request?", 'Y', 'N');
        if (approve != 'Y') {
            System.out.println("Adoption not approved.");
            return null;
        }
        return record;
    }

    private void approveAdoptionRequest() {
        System.out.println("Adoption Requests");
        String reservationRecord = findAndConfirmTransaction("RESERVE.txt", "Adoption Request");
        if (reservationRecord == null)
            return;

        String[] parts = reservationRecord.split("\\s*\\|\\s*");
        String petID = parts[RES_PET_ID];
        String transactionNum = parts[RES_TRANSACTION_ID];

        String adoptedRecord = buildAdoptedRecord(reservationRecord, this.getEmailAddress());

        FileHandler.appendRecord("ADOPTED_LIST.txt", adoptedRecord);
        FileHandler.removeRecord("RESERVE.txt", transactionNum, 0);
        PetManager.getInstance().findPetID(petID).setAdoptionStatus(AdoptionStatus.ADOPTED);

    }

    private static final int RES_TRANSACTION_ID = 0;
    private static final int RES_FIRST_NAME = 1;
    private static final int RES_LAST_NAME = 2;
    private static final int RES_EMAIL = 3;
    private static final int RES_PET_ID = 4;
    private static final int RES_PAYMENT_TYPE = 5;
    private static final int RES_DOWN_PAYMENT = 6;
    private static final int RES_BALANCE_LEFT = 7;
    private static final int RES_DATE_REQUESTED = 8;

    private String buildAdoptedRecord(String reservationRecord, String approvedByEmail) {
        String[] parts = reservationRecord.split("\\s*\\|\\s*");
        String dateFinalized = java.time.LocalDate.now().toString();

        return String.join("|",
                parts[RES_TRANSACTION_ID], parts[RES_FIRST_NAME], parts[RES_LAST_NAME],
                parts[RES_EMAIL], parts[RES_PET_ID], parts[RES_PAYMENT_TYPE],
                parts[RES_DOWN_PAYMENT], parts[RES_BALANCE_LEFT],
                parts[RES_DATE_REQUESTED], dateFinalized, approvedByEmail);
    }

    private void cancelAdoptionRequest() {
        System.out.println("Cancellation Requests");
        String record = findAndConfirmTransaction("CANCELLATION_REQUEST.txt", "Cancellation Request");
        if (record == null)
            return;

        String[] parts = record.split("\\s*\\|\\s*");
        String petID = parts[RES_PET_ID];
        String transactionNum = parts[RES_TRANSACTION_ID];

        FileHandler.appendRecord("CANCELLED_LIST.txt", record);
        FileHandler.removeRecord("CANCELLATION_REQUEST.txt", transactionNum, 0);
        PetManager.getInstance().findPetID(petID).setAdoptionStatus(AdoptionStatus.AVAILABLE);
    }

    private void approvePetReturn() {
        System.out.println("Return Requests");
        String record = findAndConfirmTransaction("RETURN_REQUEST.txt", "Return Request");
        if (record == null)
            return;

        String[] parts = record.split("\\s*\\|\\s*");
        String petID = parts[RES_PET_ID];
        String transactionNum = parts[RES_TRANSACTION_ID];

        FileHandler.appendRecord("RETURNED_LIST.txt", record); // Documentation
        FileHandler.removeRecord("RETURN_REQUEST.txt", transactionNum, 0);
        PetManager.getInstance().findPetID(petID).setAdoptionStatus(AdoptionStatus.AVAILABLE);
    }

    private void manageIncomingPets() {
    }
}
