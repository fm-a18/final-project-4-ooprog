package users;

import inventory.AdoptionStatus;
import utils.PetManager;
import utils.DataValidation;
import utils.FileHandler;
import java.util.Scanner;

import static utils.DisplayUtils.printMenu;

public class Admin extends User {

    public Admin(String userID, String firstName, String lastName, int age, String emailAddress,
            String address, String phoneNumber, String password) {
        super(userID, firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
            printMenu("ADMIN MENU",
                    "#Pet Management",
                    "View All Available Pets",
                    "Search by Category",
                    "Sort by Category",
                    "Add Pet",

                    "#Adoption Management",
                    "Approve Adoption Request",
                    "Cancel Adoption Request",
                    "Approve Return Request",

                    "#Shelter Management",
                    "Manage Incoming Pets",

                    "#Exit");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 0);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> filterByStatus();
                case 4 -> addPet();
                case 5 -> approveAdoptionRequest();
                case 6 -> cancelAdoptionRequest();
                case 7 -> approvePetReturn();
                case 8 -> manageIncomingPets();
                case 0 -> isDone = true;
            }
        }
    }

    private void viewAllPets() {
        PetManager.getInstance().getAllPets();
    }

    private void searchByCategory() {
        PetManager.getInstance().searchByCategory();
    }

    private void filterByStatus() {
        PetManager.getInstance().filterByStatus();
    }

    private void addPet() {
        PetManager.getInstance().addPet(pet);
    }

    public String findAndConfirmTransaction(String fileName, String action) {
        String findTransactionNum = DataValidation.transactionNumberValidation("Transaction Number: ");
        String record = FileHandler.findRecord(fileName, findTransactionNum, 0);

        if (record == null) {
            System.out.println("Transaction not found.");
            return null;
        }

        System.out.println("Transaction number found.");

        char approve = DataValidation.charChoiceValidation("Approve " + action + "?", 'Y', 'N');
        if (approve != 'Y') {
            System.out.println(action + " not approved.");
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
        System.out.println("Adoption Requests");
        String record = findAndConfirmTransaction("RESERVE.txt", "Adoption Request");
        if (record == null)
            return;

        String[] parts = record.split("\\s*\\|\\s*");
        String petID = parts[RES_PET_ID];
        String transactionNum = parts[RES_TRANSACTION_ID];

        FileHandler.appendRecord("ADOPTED_LIST.txt", record); // Documentation
        FileHandler.removeRecord("RESERVE.txt", transactionNum, 0);
        PetManager.getInstance().findPetID(petID).setAdoptionStatus(AdoptionStatus.ADOPTED);
    }
}
