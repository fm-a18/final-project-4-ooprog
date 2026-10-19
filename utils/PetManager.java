package utils;

import inventory.*;
import users.Customer;
import utils.generators.TransactionNumGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;

import static utils.RecordManager.viewAllReserveRecords;
import static utils.RecordManager.RES_TRANSACTION_NUM;

public class PetManager {
    private static PetManager instance;
    private final ArrayList<Pet> pets;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);

    private PetManager() {
        pets = FileHandler.viewAllPetRecords();
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public void addPet(){
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3);

            String petID = DataValidation.validatePetIdInput();
            String petName = DataValidation.validateString("Enter Name: ");
            String petBreed = DataValidation.validateString("Enter Breed: ");
            char petGender = DataValidation.charChoiceValidation("Enter Gender (M/F): ", 'M', 'F');
            int petAge = DataValidation.validatePetAgeInput("Enter age: ");
            double petPrice = DataValidation.validatePriceInput("Enter price of pet: ");
            char hasRecords = DataValidation.charChoiceValidation("Does the pet have any existing medical records? (Y/N): ", 'Y', 'N');
            if (hasRecords == 'Y') {
                String petMedCon = DataValidation.validateString("Enter medical condition/treatment name: ");
                String petMedHis = String.valueOf(DataValidation.validateDate("Enter date of the condition/treatment: "));
                findPetID(petID).addMedicalHistory(petMedHis, petMedCon);
            }

            System.out.println("Pet Added on " + LocalDate.now().format(dateFormat));

            Pet pet = switch (choice) {
                case 1 -> new Dog(petID, petName, petBreed, petGender, petAge, petPrice);
                case 2 -> new Cat(petID, petName, petBreed, petGender, petAge, petPrice);
                default -> new Bird(petID, petName, petBreed, petGender, petAge, petPrice);
            };
            pets.add(choice, pet);
            isDone = false;
        }
    }

    public ArrayList<Pet> getAllPets() {
        if(pets.isEmpty()){
            ArrayList<Pet> all = PetManager.getInstance().getAllPets();
            if (all == null) {
                return null;
            }
        }

        return pets;
    }

    public void searchByCategory(Scanner sc){
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Search By Category",
                    "#Pet Details",
                    "Pet Name", "Type", "Breed", "Gender", "Age",
                    "#Pet Records",
                    "Adoption Status", "#Exit", "Log Out");

            int choice = DataValidation.intChoiceValidation("Enter choice: ", 1, 2, 3, 4, 5, 6, 7);

            switch (choice) {
                case 1: {
                    String petName = DataValidation.validateString("Enter pet name:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petName, Pet::getPetName);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 2: {
                    String petType = DataValidation.validateString("Enter pet type:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petType, Pet::getType);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 3: {
                    String petBreed = DataValidation.validateString("Enter pet breed:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petBreed, Pet::getBreed);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 4: {
                    String petGender = DataValidation.validateString("Enter pet gender:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petGender, Pet::getGender);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 5: {
                    String petAge = DataValidation.validateString("Enter pet age:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petAge, Pet::getAge);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 6: {
                    String petStatus = DataValidation.validateString("Enter adoption status:");
                    ArrayList<Pet> listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petStatus, Pet::getAdoptionStatus);
                    DisplayUtils.displayPetsForStaff(listOfPet);
                    break;
                }
                case 7: {
                    isDone = false;
                    break;
                }
            }
        }
    }

    public Pet findPetID(String petID) {
        System.out.println("Size: " + pets.size());
        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                return pet;
            }
        }
        return null;
    }

    public void updatePetStatus(Scanner sc) {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird", "#Exit", "Back");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3, 4);
            String cat = "";

            if (choice==4) {
                isDone = false;
                continue;
            }
            switch (choice) {
                case 1 -> cat = "Dog";
                case 2 -> cat = "Cat";
                default -> cat = "Bird";
            }

            String petID = DataValidation.validatePetIdInput();
            PetManager.getInstance().findPetID(petID).setAdoptionStatus(AdoptionStatus.ADOPTED);
        }
    }

    public void sortByCategory(Scanner sc) {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = true;

        while (isDone) {
            DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3);

            DisplayUtils.printMenu("Sort By..", "Age", "Price", "Gender", "#Exit", "Back");
            int choicev1 = DataValidation.intChoiceValidation("Enter choice: ", 1, 2, 3, 4);

            if (choicev1 == 4) {
                return;
            }

            DisplayUtils.printMenu("Order: ", "Ascending", "Descending");
            int choicev2 = DataValidation.intChoiceValidation("Enter choice: ", 1, 2);

            FileHandler.sortCategory(RecordManager.FILE_PET_LIST, choice, choicev1, choicev2 == 1);
        }


    }

    //-------------CUSTOMER METHODS
    //PET AVAILABLE
    public ArrayList<Pet> getAvailablePets() {
        ArrayList<Pet> pets = FileHandler.viewAllPetRecords();
        ArrayList<Pet> available = availablePets(pets);

        return available;
    }

    public ArrayList<Pet> availablePets(ArrayList<Pet> pets){
        ArrayList<Pet> availablePets = new ArrayList<>();
        ArrayList<String> reserve = new ArrayList<>();
        for(Reservation pet : viewAllReserveRecords()){
            reserve.add(pet.getPetID().toUpperCase());
        }

        for(Pet pet : pets){
            if(!reserve.contains(pet.getPetID().toUpperCase())){
                availablePets.add(pet);
            }
        }
        return availablePets;
    }

    //------------REQUEST ADOPTION (CUSTOMER)
    public void requestAdoption(Customer customer){
        ArrayList<Pet> availablePets = getAvailablePets();

        if (availablePets.isEmpty()) {
            System.out.println("No pets available for adoption.");
            return;
        }

        DisplayUtils.displayPetsForCustomer(availablePets);

        requestAdoptionCustomer(availablePets, customer);
    }

    public void requestAdoptionCustomer(ArrayList<Pet> availablePets, Customer customer) {
        String petID = DataValidation.validatePetID("\nEnter Pet ID to request adoption: ", "^(?i)[DCB]\\d{4}$");

        Pet selectedPet = null;

        for (Pet pet : availablePets) {
            if (pet.idExists(petID)) {
                selectedPet = pet;
                break;
            }
        }

        if (selectedPet == null) {
            System.out.println("Pet ID: " + petID + " not found.");
            return;
        }

        System.out.println("Pet ID: " + petID + " found and is available for adoption.");

        char requestAdoption = DataValidation.charChoiceValidation("\nDo you want to request adoption for Pet ID: " + petID + "? (y/n): ", 'y', 'n');

        if (requestAdoption == 'N') {
            System.out.println("Adoption request cancelled.");
            return;
        }else{
            System.out.println("Fill out the following information for the adoption request:");
        }
        
        String paymentPlan = paymentPlanSelection();
        if (paymentPlan == null) {
            System.out.println("Adoption request cancelled.");
            return;
        }

        double downPaymentAmount = downPayment(paymentPlan, selectedPet.getPrice());
        double balanceLeft = selectedPet.getPrice() - downPaymentAmount;

        LocalDate date = DataValidation.validateDate("Enter date for claiming of pet (mm-dd-yyyy)");
        while (!DataValidation.dateForClaimingPet(date)) {
            System.out.println("Date should be on or within 7 days after the adoption request.");
            date = DataValidation.validateDate("Enter date for claiming of pet: ");
        }

        System.out.println("\nConfirm adoption request by entering your email address.");
        String confirmByEmail = DataValidation.validateEmailAddress();

        while (!DataValidation.emailExists(confirmByEmail)) {
            System.out.println("Email is not found. Please enter another email.");
            confirmByEmail = DataValidation.validateEmailAddress();
        }

        String transactionNum = TransactionNumGenerator.transactionNumGenerator();
        RecordManager rm = new RecordManager();
        String reservationRecord = rm.buildAdoptionRequestRecord(customer.getFirstName(), customer.getLastName(), customer.getEmailAddress(), selectedPet, paymentPlan, downPaymentAmount, balanceLeft, transactionNum, date);
        FileHandler.appendRecord("RESERVED.txt", reservationRecord);
        selectedPet.setAdoptionStatus(AdoptionStatus.ADOPTION_REVIEW);

        DisplayUtils.printSummaryBox("ADOPTION REQUEST DETAILS",
                new String[][] {
                        { "Transaction Number", transactionNum},
                        { "Pet ID", selectedPet.getPetID()},
                        { "Payment Plan", paymentPlan},
                        { "Down Payment", String.valueOf(downPaymentAmount)},
                        { "Balance", String.valueOf(balanceLeft)},
                        { "Date Requested", LocalDate.now().toString()},
                        { "Date Claiming", date.toString()}
                }
        );
        System.out.println("Adoption request for Pet ID: " + petID + " with transaction number " + transactionNum + " has been submitted for review.");
    }

    private static String paymentPlanSelection() {
        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("SELECT PAYMENT PLAN",
                    "#Available Payment Plans",
                    "Full Payment",
                    "50% Down Payment",
                    "75% Down Payment");

            int choice = DataValidation.intChoiceValidation("Select Payment Plan for Adoption Request: ", 1, 2, 3, 0);

            switch (choice) {
                case 1 -> {
                    System.out.println("You have selected Full Payment.");
                    return "Full Payment";
                }
                case 2 -> {
                    System.out.println("You have selected 50% Down Payment.");
                    return "50% Down Payment";
                }
                case 3 -> {
                    System.out.println("You have selected 75% Down Payment.");
                    return "75% Down Payment";
                }
                case 0 -> isDone = true;
            }
        }
        return null;
    }

    private static double downPayment(String paymentPlan, double petPrice) {
        double amount = 0;

        switch (paymentPlan) {
            case "Full Payment" -> amount = 0;
            case "50% Down Payment" -> amount = petPrice * 0.5;
            case "75% Down Payment" -> amount = petPrice * 0.75;
        }
        return amount;
    }

    //------------CANCEL REQUEST ADOPTION (CUSTOMER)
    public void cancelAdoption(Customer customer) {
        ArrayList<Reservation> reserveDetails = RecordManager.viewAllReserveRecords();
        if(reserveDetails.isEmpty()){
            System.out.println("No pets are reserved.");
            return;
        }
        String transactionNum = DataValidation.transactionNumberValidation(
                "Enter Transaction Number from your Request of Adoption Confirmation Message: ");

        Reservation found = null;

        for (Reservation reserve : reserveDetails) {
            if (reserve.getTransactionNum().equalsIgnoreCase(transactionNum)) {
                found = reserve;
                break;
            }
        }

        if (found == null || !found.getEmail().equalsIgnoreCase(customer.getEmailAddress())) {
            System.out.println("No reservation found for transaction number: " + transactionNum);
            return;
        }

        System.out.println("Reservation found!");
        DisplayUtils.printSummaryBox("PET DETAILS",
                new String[][] {
                        { "Customer's Name", found.getFirstName() + " " + found.getLastName()},
                        { "Customer's Email", found.getEmail()},
                        { "Transaction Number", found.getTransactionNum()},
                        { "Pet ID", found.getPetID()},
                        { "Payment Plan", found.getPaymentPlan()},
                        { "Down Payment", String.valueOf(found.getDownPayment())},
                        { "Balance", String.valueOf(found.getBalanceLeft())},
                        { "Date Requested", found.getDateRequested().toString()},
                        { "Date Claiming", found.getDateClaiming().toString()}
                }
        );

        char cancelAdoptionConfirmation = DataValidation.charChoiceValidation("Are you sure you want to cancel your reservation? (y/n): ", 'y', 'n');

        if(cancelAdoptionConfirmation == 'N'){
            System.out.println("Adoption Cancellation Cancelled.");
            return;
        }

        ArrayList<Pet> pets = FileHandler.viewAllPetRecords();
        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(found.getPetID())) {
                pet.setAdoptionStatus(AdoptionStatus.CANCELLATION_REVIEW);
                break;
            }
        }

        String petRecord = FileHandler.findRecord("RESERVED.txt", transactionNum, RES_TRANSACTION_NUM);
        FileHandler.appendRecord("CANCELLATION_REQUEST.txt", petRecord);
        System.out.println(
                "Your cancellation request of pet request adoption is now being processed. Wait for the Admin or Coordinator to approve your request.");
    }

    //------------REQUEST RETURN ADOPTION (CUSTOMER)
    public void requestReturn(Customer customer) {
        ArrayList<Reservation> adopted = RecordManager.viewAllPetAdoptedRecords();

        if(adopted.isEmpty()){
            System.out.println("No pets available for return.");
            return;
        }
        String transactionNum = DataValidation.transactionNumberValidation(
                "Enter Transaction Number from your Request of Adoption Confirmation Message: ");

        Reservation found = null;

        for (Reservation adopt : adopted) {
            if (adopt.getTransactionNum().equalsIgnoreCase(transactionNum)) {
                found = adopt;
                break;
            }
        }

        if (found == null || !found.getEmail().equalsIgnoreCase(customer.getEmailAddress())) {
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

        if (returnAdoptionConfirmation == 'N') {
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
