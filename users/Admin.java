package users;

import inventory.AdoptionStatus;
import inventory.Pet;
import utils.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Override
    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
            printMenu("ADMIN MENU",
                    "#Pet Management",
                    "View All Pets",
                    "Search by Category",
                    "Sort by Category",
                    "Search Specific Pet",
                    "Add Pet",
                    "Update Pet Medical History",

                    "#Adoption Management",
                    "Approve Adoption Request",
                    "Cancel Adoption Request",
                    "Approve Return Request",

                    "#Shelter Management",
                    "Manage Incoming Pets",

                    "#Exit",
                    "Log Out");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> sortByCategory();
                case 4 -> searchForSpecificPet();
                case 5 -> addPet();
                case 6 -> updatePetMedicalHistory();
                case 7 -> approveAdoptionRequest();
                case 8 -> cancelAdoptionRequest();
                case 9 -> approvePetReturn();
                case 10 -> manageIncomingPets();
                case 11 -> isDone = true;
            }
        }
    }

    private void viewAllPets() {
        var pets = PetManager.getInstance().getAllPets();
        if (pets == null || pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory() {
        PetManager.getInstance().searchByCategory();
    }

    private void sortByCategory() {
        PetManager.getInstance().sortByCategory();
    }

    private void searchForSpecificPet() {
        PetManager.getInstance().viewSpecificPet();
    }

    private void addPet() {
        PetManager.getInstance().addPet();
    }

    private void updatePetMedicalHistory() {
        PetManager.getInstance().updatePetMedicalHistory();
    }

    private String findPendingTransaction(String fileName, int fieldCount, String action) {
        ArrayList<Reservation> pending = RecordManager.readRecords(fileName, fieldCount,
                RecordManager::parseReservation);

        if (pending.isEmpty()) {
            System.out.println("No pending " + action.toLowerCase() + "(s) found.");
            return null;
        }

        DisplayUtils.displayPendingRequests("PENDING " + action.toUpperCase() + "S", pending);

        String findTransactionNum = DataValidation.transactionNumberValidation();

        if (findTransactionNum == null || findTransactionNum.isBlank()) {
            System.out.println("Invalid Transaction Number.");
            return null;
        }

        String record = FileHandler.findRecord(fileName, findTransactionNum, RecordManager.RES_TRANSACTION_NUM);

        if (record == null) {
            System.out.println("Transaction not found.");
            return null;
        }
        System.out.println("Transaction number found.");

        return record;
    }

    private Pet findPet(String petID) {
        Pet pet = PetManager.getInstance().findPetID(petID);
        if (pet == null) {
            System.out.println("Pet " + petID + " not found in the pet list. No changes were made.");
        }
        return pet;
    }

    private void approveAdoptionRequest() {
        String reservationRecord = findPendingTransaction(RecordManager.FILE_RESERVED, RecordManager.RES_FIELD_COUNT, "Adoption Request");
        if (reservationRecord == null)
            return;

        String[] parts = reservationRecord.split("\\s*\\|\\s*");
        String petID = parts[RecordManager.RES_PET_ID];
        String transactionNum = parts[RecordManager.RES_TRANSACTION_NUM];

        Pet pet = findPet(petID);
        if (pet == null)
            return;

        AdoptionStatus currentStatus = pet.getAdoptionStatus();
        if (currentStatus == AdoptionStatus.CANCELLATION_REVIEW) {
            System.out.println("This pet already has a pending cancellation request.");
            return;
        } else if (currentStatus == AdoptionStatus.RETURN_PENDING) {
            System.out.println("This pet already has a pending return request.");
            return;
        }

        if (currentStatus == AdoptionStatus.ADOPTED) {
            System.out.println("This pet is already adopted and cannot be approved again.");
            return;
        }

        char decision = DataValidation.charChoiceValidation("Approve, Reject, or Skip", 'A', 'R', 'S');

        if (decision == 'S') {
            System.out.println("No changes made.");
            return;
        }

        if (decision == 'R') {
            FileHandler.removeRecord(RecordManager.FILE_RESERVED, transactionNum, RecordManager.RES_TRANSACTION_NUM);
            PetManager.getInstance().updatePetStatus(petID, AdoptionStatus.AVAILABLE);
            System.out.printf("Adoption request %s rejected.\n", transactionNum);
            return;
        }

        String adoptedRecord = RecordManager.buildAdoptedRecord(reservationRecord, this.getEmailAddress());

        FileHandler.appendRecord(RecordManager.FILE_ADOPTED, adoptedRecord);
        FileHandler.removeRecord(RecordManager.FILE_RESERVED, transactionNum, RecordManager.RES_TRANSACTION_NUM);
        PetManager.getInstance().updatePetStatus(petID, AdoptionStatus.ADOPTED);

        System.out.printf("Adoption approved for transaction %s.\n", transactionNum);

    }

    private void cancelAdoptionRequest() {
        String record = findPendingTransaction(RecordManager.FILE_CANCELLATION_REQUEST, RecordManager.RES_FIELD_COUNT, "Cancellation Request");
        if (record == null)
            return;

        String[] parts = record.split("\\s*\\|\\s*");
        String petID = parts[RecordManager.RES_PET_ID];
        String transactionNum = parts[RecordManager.RES_TRANSACTION_NUM];

        Pet pet = findPet(petID);
        if (pet == null)
            return;

        AdoptionStatus currentStatus = pet.getAdoptionStatus();
        if (currentStatus == AdoptionStatus.ADOPTED) {
            System.out.println("This pet is already adopted, so its reservation cannot be cancelled.");
            return;
        }

        char decision = DataValidation.charChoiceValidation("Approve, Reject, or Skip", 'A', 'R', 'S');

        if (decision == 'S') {
            System.out.println("No changes made.");
            return;
        }

        if (decision == 'R') {
            FileHandler.removeRecord(RecordManager.FILE_CANCELLATION_REQUEST, transactionNum, RecordManager.RES_TRANSACTION_NUM);
            PetManager.getInstance().updatePetStatus(petID, AdoptionStatus.ADOPTION_REVIEW);
            System.out.printf("Cancellation request %s rejected. The reservation stays active.\n", transactionNum);
            return;
        }

        FileHandler.appendRecord(RecordManager.FILE_CANCELLED_LIST, record);
        FileHandler.removeRecord(RecordManager.FILE_CANCELLATION_REQUEST, transactionNum, RecordManager.RES_TRANSACTION_NUM);
        FileHandler.removeRecord(RecordManager.FILE_RESERVED, transactionNum, RecordManager.RES_TRANSACTION_NUM);
        PetManager.getInstance().updatePetStatus(petID, AdoptionStatus.AVAILABLE);

        System.out.printf("Cancellation approved for transaction %s.\n", transactionNum);

    }

    private void approvePetReturn() {
        String record = findPendingTransaction(RecordManager.FILE_RETURN_REQUEST, RecordManager.AD_FIELD_COUNT, "Return Request");
        if (record == null)
            return;

        String[] parts = record.split("\\s*\\|\\s*");
        String petID = parts[RecordManager.RES_PET_ID];
        String transactionNum = parts[RecordManager.RES_TRANSACTION_NUM];

        Pet pet = findPet(petID);
        if (pet == null)
            return;

        char decision = DataValidation.charChoiceValidation("Approve, Reject, or Skip", 'A', 'R', 'S');

        if (decision == 'S') {
            System.out.println("No changes made.");
            return;
        }

        if (decision == 'R') {
            FileHandler.removeRecord(RecordManager.FILE_RETURN_REQUEST, transactionNum, RecordManager.RES_TRANSACTION_NUM);
            PetManager.getInstance().updatePetStatus(petID, AdoptionStatus.ADOPTED);
            System.out.printf("Return request %s rejected. The pet stays adopted.\n", transactionNum);
            return;
        }

        String detailsOfReturn = DataValidation.validateString("Details");

        pet.addAdoptionHistory(LocalDate.now(), detailsOfReturn);
        FileHandler.appendRecord(RecordManager.FILE_RETURNED_LIST, record);
        FileHandler.removeRecord(RecordManager.FILE_RETURN_REQUEST, transactionNum, RecordManager.RES_TRANSACTION_NUM);
        FileHandler.removeRecord(RecordManager.FILE_ADOPTED, transactionNum, RecordManager.RES_TRANSACTION_NUM);
        PetManager.getInstance().updatePetStatus(petID, AdoptionStatus.AVAILABLE);

        System.out.printf("Return of pet approved for transaction %s.\n", transactionNum);

    }

    private void manageIncomingPets() {
        System.out.println("Incoming Pet Requests");

        var incomingPets = RecordManager.readRecords(RecordManager.FILE_INCOMING_LIST,
                RecordManager.PET_FIELD_COUNT, RecordManager::parsePet);

        if (incomingPets == null || incomingPets.isEmpty()) {
            System.out.println("No incoming pets to review.");
            return;
        }

        DisplayUtils.displayPetsForStaff(new ArrayList<>(incomingPets));
        String petID = DataValidation.validatePetIdInput();

        Pet selectedPet = null;
        for (Pet pet : incomingPets) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                selectedPet = pet;
                break;
            }
        }

        if (selectedPet == null) {
            System.out.println("Pet " + petID + " is not in the incoming list.");
            return;
        }

        if (PetManager.getInstance().findPetID(petID) != null) {
            System.out.println("This pet ID already exists in the active inventory. Approval cancelled.");
            return;
        }

        DisplayUtils.printSummaryBox("INCOMING PET", new String[][] {
                { "Pet ID", selectedPet.getPetID() },
                { "Name", selectedPet.getPetName() },
                { "Type", selectedPet.getType() },
                { "Breed", selectedPet.getBreed() },
                { "Gender", selectedPet.getGender() },
                { "Age", String.valueOf(selectedPet.getAge()) },
                { "Price", String.format("Php %,.2f", selectedPet.getPrice()) },
                { "Medical History", selectedPet.getMedicalHistory() }
        });

        char decision = DataValidation.charChoiceValidation("Approve, Reject, or Skip", 'A', 'R', 'S');

        if (decision == 'S') {
            System.out.println("No changes made.");
            return;
        }

        if (decision == 'R') {
            FileHandler.removeRecord(RecordManager.FILE_INCOMING_LIST, petID, 0);
            System.out.printf("Incoming pet with ID %s rejected and removed.\n", petID);
            return;
        }

        if (!PetManager.getInstance().approveIncomingPet(selectedPet)) {
            System.out.println("Could not add the pet to inventory. It stays in the incoming list.");
            return;
        }
        FileHandler.removeRecord(RecordManager.FILE_INCOMING_LIST, petID, 0);

        System.out.printf("Incoming pet with ID %s approved and added to inventory.\n", petID);
    }
}