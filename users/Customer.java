package users;

import inventory.AdoptionStatus;
import inventory.Pet;
import utils.FileHandler;
import utils.DataValidation;
import utils.DisplayUtils;
import utils.Reservation;
import utils.RecordManager;
import utils.generators.TransactionNumGenerator;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import static utils.RecordManager.RES_TRANSACTION_NUM;
import static utils.RecordManager.readRecords;
import static utils.RecordManager.viewAllPetAdoptedRecords;
import static utils.RecordManager.viewAllReserveRecords;
import static utils.DataValidation.dateForClaimingPet;
import static utils.RecordManager.AD_DATE_FINALIZED;
import static utils.RecordManager.RES_PET_ID;

public class Customer extends User {
    public Customer(String userID, String firstName, String lastName, int age, String emailAddress,
                    String address, String phoneNumber, String password) {
        super(userID, firstName, lastName, age, emailAddress,
                address, phoneNumber, password);
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public void showMenu(Scanner sc) {
        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("CUSTOMER MENU",
                    "#Browse Pets",
                    "View All Available Pets",
                    "Search by Category",
                    "Search Specific Pets",
                    "Filter by Price",

                    "#Adoption",
                    "Request Adoption",
                    "Cancel Adoption Request",

                    "#Return",
                    "Request Pet Return",

                    "#Exit");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 8);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> findSpecificPets();
                case 4 -> filterByPrice();
                case 5 -> requestAdoption();
                case 6 -> cancelAdoption();
                case 7 -> requestReturn();
                case 8 -> isDone = true;
            }
        }
        System.out.println("Logging out...");
    }

    private void viewAllPets() {
        ArrayList<Pet> pets = getAvailablePets();

        if (pets.isEmpty()) {
            DisplayUtils.centeredTitle("VIEW ALL AVAILABLE PETS", 100);
            System.out.println("No available pets found.");
            return;
        }

        showResults(pets, "VIEW ALL AVAILABLE PETS");
    }

    public ArrayList<Pet> getAvailablePets() {
        ArrayList<Pet> pets = FileHandler.viewAllPetRecords();
        ArrayList<Pet> available = availablePets(pets);

        return available;
    }

    private void searchByCategory() {
        
        if (getAvailablePets().isEmpty()) {
            DisplayUtils.centeredTitle("SEARCH BY CATEGORY", 100);
            System.out.println("No available pets found.");
            return;
        }

        DisplayUtils.printMenu("SEARCH BY CATEGORY",
                "#Select Category",
                "Pet Name",
                "Type",
                "Breed",
                "Gender",
                "Age");
        int choice = DataValidation.intChoiceValidation("Select Option",
                1, 2, 3, 4, 5);

        switch (choice) {
            case 1: // SEARCH PET BY NAME
                DisplayUtils.centeredTitle("SEARCH PET BY NAME", 60);
                String name = DataValidation.validateString("Enter Pet Name: ");

                showResults(FileHandler.filterRecords("PET_LIST.txt", name, Pet::getPetName), "VIEW BY PET NAME: " + name.toUpperCase());
                break;
            case 2: // VIEW PET BY PET TYPE
            ArrayList<Pet> pets = getSpecificPetDetailsPerType();
                String petType = "";
                for(Pet pet : pets){
                    petType = pet.getType();
                    break;
                }
                showResults(pets, "VIEW BY PET TYPE: " + petType.toUpperCase());
                break;
            case 3: // SEARCH PET BY PET BREED
                DisplayUtils.centeredTitle("SEARCH PET BY BREED", 60);
                String breed = DataValidation.validateString("Enter Pet Breed: ");

                showResults(FileHandler.filterRecords("PET_LIST.txt", breed, Pet::getBreed), "VIEW BY PET BREED " + breed.toUpperCase());
                break;
            case 4: // SEARCH PET BY GENDER
                DisplayUtils.printMenu("SEARCH BY PET GENDER",
                        "#Select Gender",
                        "Male",
                        "Female");
                int choiceGender = DataValidation.intChoiceValidation("Select Option",
                        1, 2, 0);

                if (choiceGender == 0) {
                    break;
                }

                String gender = (choiceGender == 1) ? "Male" : "Female";
                showResults(FileHandler.filterRecords("PET_LIST.txt", String.valueOf(gender), Pet::getGender), "VIEW BY PET GENDER " + gender.toUpperCase());
                break;
            case 5: // SEARCH PET BY AGE
            DisplayUtils.centeredTitle("SEARCH PET BY AGE", 60);
                int age = DataValidation.validatePetAgeInput("Enter Pet Age: ");

                showResults(FileHandler.filterRecords("PET_LIST.txt", age, Pet::getAge), "VIEW BY PET AGE " + age);
                break;
        }
    }

    private void showResults(ArrayList<Pet> pets, String title) {
        if (pets == null || pets.isEmpty()) {
            DisplayUtils.centeredTitle(title, 100);
            System.out.println("No available pet(s) found.");
            return;
        }
        DisplayUtils.centeredTitle(title, 100);
        DisplayUtils.displayPetsForCustomer(availablePets(pets));
    }

    private ArrayList<Pet> getSpecificPetDetailsPerType() {
        String type = "";

        if (getAvailablePets().isEmpty()) {
            DisplayUtils.centeredTitle("FIND SPECIFIC PETS [DOG/CAT/BIRD]", 100);
            System.out.println("No available pets found.");
            return null;
        }

        DisplayUtils.printMenu("PET TYPE",
                "#Select Pet Type",
                "Dog",
                "Cat",
                "Bird");
        int choiceType = DataValidation.intChoiceValidation("Select Option",
                1, 2, 3, 0);

        switch (choiceType) {
            case 1 -> type = "dog";
            case 2 -> type = "cat";
            case 3 -> type = "bird";
            case 0 -> type = "";
        }

        return FileHandler.filterRecords("PET_LIST.txt", type, Pet::getType);
    }

    private void findSpecificPets() {
        ArrayList<Pet> petTypes = availablePets(getSpecificPetDetailsPerType());

        showResults(petTypes, "FIND SPECIFIC PETS");

        if (petTypes == null || petTypes.isEmpty()) {
            return;
        }

        char viewDetailsChoice = DataValidation.charChoiceValidation("Do you want to view specific pet details? (y/n): ", 'Y', 'N');

        if (viewDetailsChoice == 'Y') {
            String type = petTypes.getFirst().getType();
            specificPetDetails(type, petTypes);
        }
    }

    private void specificPetDetails(String type, ArrayList<Pet> petTypes) { // EDIT THIS
        DisplayUtils.centeredTitle("VIEW SPECIFIC PET DETAILS", 60);
        String petID = DataValidation.validatePetIdInput("Enter Pet ID: ", type);

        for (Pet pet : petTypes) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                DisplayUtils.displayPetDetails(pet);
                return;
            }
        }
        System.out.println("No pet found with the given ID.");
    }

    private void filterByPrice() {
        ArrayList<Pet> pets = getAvailablePets();
        if (pets.isEmpty()) {
            DisplayUtils.centeredTitle("FILTER BY PRICE", 100);
            System.out.println("No available pets found.");
            return;
        }

        DisplayUtils.printMenu("FILTER BY PRICE",
                "#Select Option",
                "Ascending Price",
                "Descending Price",
                "Price Range");
        int choice = DataValidation.intChoiceValidation("Select Option",
                1, 2, 3, 0);

        switch (choice) {
            case 1:
                showResults(sortByPrice(choice, pets), "SORT BY ASCENDING PRICE");
                break;
            case 2:
                showResults(sortByPrice(choice, pets), "SORT BY DESCENDING PRICE");
                break;
            case 3:
                showResults(filterByPriceRange(pets), "FILTER BY PRICE RANGE");
                break;
        }
    }

    private ArrayList<Pet> sortByPrice(int order, ArrayList<Pet> pets) {
        ArrayList<Pet> sortPrice = new ArrayList<>(pets);

        Comparator<Pet> comparator = Comparator.comparingDouble(Pet::getPrice);

        if (order == 2) {
            comparator = comparator.reversed();
        }

        sortPrice.sort(comparator);
        return sortPrice;
    }

    private ArrayList<Pet> filterByPriceRange(ArrayList<Pet> pets) {
        DisplayUtils.centeredTitle("FILTER BY PRICE RANGE", 60);
        ArrayList<Pet> filteredPets = new ArrayList<>();
        double startingPrice = DataValidation.validatePriceInput("Enter Starting Price: ");
        double endingPrice = DataValidation.validatePriceInput("Enter Ending Price: ");

        for (Pet pet : pets) {
            if (pet.getPrice() >= startingPrice && pet.getPrice() <= endingPrice) {
                filteredPets.add(pet);
            }
        }
        return filteredPets;
    }

    private void requestAdoption(){
        // FILTER TO SHOW ONLY AVAILABLE PETS viewAllPets();
        ArrayList<Pet> availablePets = getAvailablePets();

        if (availablePets.isEmpty()) {
            System.out.println("No pets available for adoption.");
            return;
        }

        DisplayUtils.displayPetsForCustomer(availablePets);

        requestAdoptionCustomer(availablePets);
    }

    public void requestAdoptionCustomer(ArrayList<Pet> availablePets) {
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
        String reservationRecord = rm.buildAdoptionRequestRecord(this.getFirstName(), this.getLastName(), this.getEmailAddress(), selectedPet, paymentPlan, downPaymentAmount, balanceLeft, transactionNum, date);
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

    private void cancelAdoption() {
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

        if (found == null || !found.getEmail().equalsIgnoreCase(this.getEmailAddress())) {
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

        char cancelAdoptionConfirmation = DataValidation.charChoiceValidation("Are you sure you want to cancel your reservation? (Y/N): ", 'Y', 'N');

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

    private void requestReturn() {
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
                .charChoiceValidation("Are you sure you want to return your pet? (Y/N): ", 'Y', 'N');

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
}