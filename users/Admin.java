package users;

import inventory.AdoptionStatus;
import inventory.Pet;
import utils.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

                    "#Exit",
                    "Log Out");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8, 9);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> filterByStatus();
                case 4 -> addPet();
                case 5 -> approveAdoptionRequest();
                case 6 -> cancelAdoptionRequest();
                case 7 -> approvePetReturn();
                case 8 -> manageIncomingPets();
                case 9 -> isDone = true;
            }
        }
    }

    private void viewAllPets() {
        var pets = PetManager.getInstance().getAllPets();
        if (pets == null || pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        DisplayUtils.displayPetsForStaff(pets);
    }

    private void searchByCategory() {
        PetManager.getInstance().searchByCategory();
    }

    private void filterByStatus() {
        PetManager.getInstance().sortByCategory();
    }

    private void addPet() {
        PetManager.getInstance().addPet();
    }

    public String findAndConfirmTransaction(String fileName, String action) {
        String findTransactionNum = DataValidation.transactionNumberValidation("Transaction Number: ");

        if (findTransactionNum == null || findTransactionNum.isBlank()) {
            System.out.println("Invalid Transaction Number.");
            return null;
        }

        String record = FileHandler.findRecord(fileName, findTransactionNum, 0);

        if (record == null){
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

    private Pet findPet(String petID){
        Pet pet = PetManager.getInstance().findPetID(petID);
        if (pet == null) {
            System.out.println("Pet " + petID + " not found in the pet list. No changes were made.");
        }
        return pet;
    }

    private void approveAdoptionRequest() {
        System.out.println("Adoption Requests");
        String reservationRecord = findAndConfirmTransaction(RecordManager.FILE_RESERVED, "Adoption Request");
        if (reservationRecord == null)
            return;

        String[] parts = reservationRecord.split("\\s*\\|\\s*");
        String petID = parts[RES_PET_ID];
        String transactionNum = parts[RES_TRANSACTION_ID];

        Pet pet = findPet(petID);
        if (pet == null)
            return;

        AdoptionStatus currentStatus = pet.getAdoptionStatus();
        if (currentStatus == AdoptionStatus.CANCELLATION_REVIEW) {
            System.out.println("This pet already has a pending cancellation request.");
            return;
        } else if (currentStatus == AdoptionStatus.RETURN_PENDING){
            System.out.println("This pet already has a pending return request.");
            return;
        }

        if (currentStatus == AdoptionStatus.ADOPTED) {
            System.out.println("This pet is already adopted and cannot be approved again.");
            return;
        }

        String adoptedRecord = RecordManager.buildAdoptedRecord(reservationRecord, this.getEmailAddress());

        FileHandler.appendRecord(RecordManager.FILE_ADOPTED, adoptedRecord);
        FileHandler.removeRecord(RecordManager.FILE_RESERVED, transactionNum, 0);
        PetManager.getInstance().updatePetStatus(petID, AdoptionStatus.ADOPTED);

        System.out.printf("Adoption approved for transaction %s.\n", transactionNum);

    }

    private static final int RES_TRANSACTION_ID = 0;
    private static final int RES_PET_ID = 4;

    private void cancelAdoptionRequest() {
        System.out.println("Cancellation Requests");
        String record = findAndConfirmTransaction(RecordManager.FILE_CANCELLATION_REQUEST, "Cancellation Request");
        if (record == null)
            return;

        String[] parts = record.split("\\s*\\|\\s*");
        String petID = parts[RES_PET_ID];
        String transactionNum = parts[RES_TRANSACTION_ID];

        Pet pet = findPet(petID);
        if (pet == null)
            return;

        AdoptionStatus currentStatus = pet.getAdoptionStatus();
        if (currentStatus == AdoptionStatus.ADOPTED) {
            System.out.println("This pet is already adopted and cannot be approved again.");
            return;
        }

        FileHandler.appendRecord(RecordManager.FILE_CANCELLED_LIST, record);
        FileHandler.removeRecord(RecordManager.FILE_CANCELLATION_REQUEST, transactionNum, 0); //Remove from cancellation_request
        FileHandler.removeRecord(RecordManager.FILE_RESERVED, transactionNum, RES_TRANSACTION_ID); //Remove from reserved_req
        PetManager.getInstance().updatePetStatus(petID, AdoptionStatus.AVAILABLE);

        System.out.printf("Cancellation approved for transaction %s.\n", transactionNum);

    }

    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private void approvePetReturn() {
        System.out.println("Return Requests");
        String record = findAndConfirmTransaction(RecordManager.FILE_RETURN_REQUEST, "Return Request");
        if (record == null)
            return;

        String[] parts = record.split("\\s*\\|\\s*");
        String petID = parts[RES_PET_ID];
        String transactionNum = parts[RES_TRANSACTION_ID];

        Pet pet = findPet(petID);
        if (pet == null)
            return;

        String detailsOfReturn = DataValidation.validateString("Details");

        pet.addAdoptionHistory(LocalDate.now().format(dateFormat), detailsOfReturn);
        FileHandler.appendRecord(RecordManager.FILE_RETURNED_LIST, record); // Documentation
        FileHandler.removeRecord(RecordManager.FILE_RETURN_REQUEST, transactionNum, 0); //Remove from return list
        FileHandler.removeRecord(RecordManager.FILE_ADOPTED, transactionNum, 0); //remove from adopted
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
        //view specific pet details

        Pet selectedPet = null;
        for(Pet pet : incomingPets){
            if(pet.getPetID().equalsIgnoreCase(petID)){
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
                { "Age", String.valueOf(selectedPet.getAge()) },
                { "Price", String.format("Php %,.2f", selectedPet.getPrice()) }
        });

        char approve = DataValidation.charChoiceValidation("Approve this pet to be added to inventory?", 'Y', 'N');
        if (approve != 'Y') {
            System.out.println("Approval cancelled.");
            return;
        }

        PetManager.getInstance().updatePetStatus(String.valueOf(selectedPet), AdoptionStatus.AVAILABLE);
        FileHandler.appendRecord(RecordManager.FILE_PET_LIST, selectedPet.toFileString());
        FileHandler.removeRecord(RecordManager.FILE_INCOMING_LIST, petID, 0);

        PetManager.getInstance().getAllPets().add(selectedPet);

        System.out.printf("Incoming pet with ID %s approved and added to inventory.\n", petID);

    }
}
