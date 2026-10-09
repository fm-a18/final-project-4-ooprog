package users;

import inventory.AdoptionStatus;
import inventory.Pet;
import utils.FileHandler;
import utils.DataValidation;
import utils.DisplayUtils;
import utils.Reservation;
import utils.RecordManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
//import java.util.function.Function;

public class Customer extends User {
    public Customer(String userID, String firstName, String lastName, int age, String emailAddress, String address, String phoneNumber, String password) {
        super(userID, firstName, lastName, emailAddress,
                address, age, phoneNumber, password);
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
                    "View Specific Pet Details",
                    "Filter by Price",

                    "#Adoption",
                    "Request Adoption",
                    "Cancel Adoption Request",

                    "#Return",
                    "Request Pet Return",

                    "#Exit",
                    "Log Out");
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7);

            switch (choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> findSpecificPets();
                case 4 -> filterByPrice();
                case 5 -> requestAdoption();
                case 6 -> cancelAdoption();
                case 7 -> requestReturn();
                case 0 -> isDone = true;
            }
        }
    }

    private final ArrayList<Pet> pets = FileHandler.viewAllPetRecords();
    private final ArrayList<User> users = new ArrayList<>();

    private void viewAllPets() {
        ArrayList<Pet> pets = FileHandler.viewAllPetRecords();

        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.displayPetsForCustomer(pets);
    }

    private void searchByCategory() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("SEARCH BY CATEGORY",
                    "#Select Category",
                    "Pet Name",
                    "Type",
                    "Breed",
                    "Gender",
                    "Age"
            );
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 0);

            switch(choice){
                case 1: //SEARCH PET BY NAME
                    String name = DataValidation.validateString("Enter Pet Name: ");

                    ArrayList<Pet> petNames = FileHandler.filterRecords("PET_LIST.txt", name, Pet::getPetName);

                    if(petNames.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petNames);
                    break;
                case 2: //VIEW PET BY PET TYPE
                    ArrayList<Pet> petTypes = getSpecificPetDetailsPerType();
                    if(petTypes.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petTypes);
                    break;
                case 3: //SEARCH PET BY PET BREED
                    String breed = DataValidation.validateString("Enter Pet Breed: ");

                    ArrayList<Pet> petBreeds = FileHandler.filterRecords("PET_LIST.txt", breed, Pet::getBreed);
                    if(petBreeds.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petBreeds);
                    break;
                case 4: // SEARCH PET BY GENDER
                    DisplayUtils.printMenu("SEARCH BY PET GENDER",
                            "#Select Gender",
                            "Male",
                            "Female"
                    );
                    int choiceGender = DataValidation.intChoiceValidation("Select Option",
                            1, 2, 0);

                    char gender;
                    if (choiceGender == 1) {
                        gender = 'M';
                    } else {
                        gender = 'F';
                    }

                    ArrayList<Pet> petGender = FileHandler.filterRecords("PET_LIST.txt", String.valueOf(gender), Pet::getGender);
                    if(petGender.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petGender);
                    break;
                case 5: //SEARCH PET BY AGE
                    int age = DataValidation.validatePetAgeInput("Enter Pet Age: ");

                    ArrayList<Pet> petAge = FileHandler.filterRecords("PETS_LIST.txt", String.valueOf(age), Pet::getAge);
                    if(petAge.isEmpty()){
                        System.out.println("No pets found");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petAge);
                    break;
                case 0: isDone = true; break;
            }
        }
    }

    private ArrayList<Pet> getSpecificPetDetailsPerType(){
        String type = "";
        boolean isDone = false;
        while(!isDone){
            DisplayUtils.printMenu("SEARCH BY PET TYPE",
                "#Select Pet Type",
                "Dog",
                "Cat",
                "Bird"
            );
            int choiceType = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 0);

            switch(choiceType){
                case 1 -> type = "dog";
                case 2 -> type = "cat";
                case 3 -> type = "bird";
                case 0 -> isDone = true;
            }
        }

        return FileHandler.filterRecords("PET_LIST.txt", type, Pet::getType);
    }

    
    private void findSpecificPets() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        ArrayList<Pet> petTypes = getSpecificPetDetailsPerType();

        if(petTypes.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.displayPetsForCustomer(petTypes);

        char viewDetailsChoice = DataValidation.charChoiceValidation("Do you want to view specific pet details? (y/n): ", 'y', 'n');



        if(viewDetailsChoice == 'y'){
            String type = petTypes.get(0).getType();
            specificPetDetails(type, petTypes);
        }else{
            return;
        }
    }

    private void specificPetDetails(String type, ArrayList<Pet> petTypes) { // EDIT THIS
        String petID = DataValidation.validatePetIdInput("Enter Pet ID: ", type);

        for(Pet pet : petTypes){
            if(pet.getPetID().equalsIgnoreCase(petID)){
                DisplayUtils.printSummaryBox("PET DETAILS",
                        new String[][] {
                                { "Name", pet.getPetName() },
                                { "Type", pet.getType() },
                                { "Breed", pet.getBreed() },
                                { "Gender", String.valueOf(pet.getGender()) },
                                { "Age", String.valueOf(pet.getAge()) },
                                { "Adoption History", pet.getAdoptionHistory() },
                                { "Medical History", pet.getMedicalHistory() }
                        }
                );
                return;
            }
        }
        System.out.println("No pet found with the given ID.");
    }

    private void filterByPrice() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = false;
        while (!isDone) {
            DisplayUtils.printMenu("FILTER BY PRICE",
                    "#Select Option",
                    "Ascending Price",
                    "Descending Price",
                    "Price Range"
            );
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 0);

            switch (choice) {
                case 1:
                    ArrayList<Pet> sortedPetsAscending = sortByPrice(1, pets);
                    if (sortedPetsAscending.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(sortedPetsAscending);
                    break;
                case 2:
                    ArrayList<Pet> sortedPetsDescending = sortByPrice(2, pets);
                    if (sortedPetsDescending.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(sortedPetsDescending);
                    break;
                case 3:
                    ArrayList<Pet> filteredPets = filterByPriceRange();

                    if (filteredPets.isEmpty()) {
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(filteredPets);
                    break;
                case 0: isDone = true; break;
            }
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

    private ArrayList<Pet> filterByPriceRange() {
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

    private void requestAdoption() {  
        //FILTER TO SHOW ONLY AVAILABLE PETS viewAllPets();
        ArrayList<Pet> availablePets = new ArrayList<>();
        ArrayList<Pet> pets = FileHandler.viewAllPetRecords();

        for (Pet pet : pets) {
            if (pet.getAdoptionStatus() == AdoptionStatus.AVAILABLE.toString()) {
                availablePets.add(pet);
            }
        }

        DisplayUtils.displayPetsForCustomer(availablePets);

        String petID = DataValidation.validatePetID("\nEnter Pet ID to request adoption: ", "^(?i)D|C|B\\d{4}$");

        Pet selectedPet = null;
    
        for(Pet pet : pets){
           if(pet.idExists(petID)){
            System.out.println("Pet ID: " + petID + " found and is available for adoption.");
            selectedPet = pet;
            break;
           }
        }

        if(selectedPet == null) {
            System.out.println("Pet ID: " + petID + " not found.");
            return;
        }



        char requestAdoption = DataValidation.charChoiceValidation("Do you want to request adoption for Pet ID: " + petID + "? (y/n): ", 'y', 'n');

        if (requestAdoption == 'n') {
            System.out.println("Adoption request cancelled.");
            return;
        }

        System.out.println("Fill out the following information for the adoption request:");
        String paymentPlan = paymentPlanSelection();
        double downPaymentAmount = downPayment(paymentPlan, selectedPet.getPrice());
        double balanceLeft = selectedPet.getPrice() - downPaymentAmount;
        

        System.out.println("\nConfirm adoption request by entering your email address.");
        String confirmByEmail = DataValidation.validateEmailAddress();

        while(!DataValidation.emailExists(confirmByEmail)){
            System.out.println("Email is not found. Please enter another email.");
            confirmByEmail = DataValidation.validateEmailAddress();
        }

        String transactionNum = DataValidation.transactionNumGenerator();
        selectedPet.setAdoptionStatus(AdoptionStatus.PENDING_REVIEW);
        System.out.println("Adoption request for Pet ID: " + petID + " with transaction number " + transactionNum + " has been submitted for review.");

        //APPEND INFORMATION FROM USER_LIST.TXT AND PET_LIST.TXT TO RESERVE.TXT
        String reservationRecord = buildAdoptionStatusRecord(selectedPet, paymentPlan, downPaymentAmount, balanceLeft, transactionNum);
        
        FileHandler.appendRecord("RESERVED.txt", reservationRecord);
    }

    private static String paymentPlanSelection(){
        boolean isDone = false;
        while(!isDone){
            DisplayUtils.printMenu("SELECT PAYMENT PLAN",
                "#Available Payment Plans",
                "Full Payment",
                "50% Down Payment",
                "75% Down Payment"
            );

            int choice = DataValidation.intChoiceValidation("Select Payment Plan for Adoption Request: ",1, 2, 3, 0);
            
            switch(choice){
                case 1 -> {System.out.println("You have selected Full Payment."); return "Full Payment";}
                case 2 -> {System.out.println("You have selected 50% Down Payment."); return "50% Down Payment";}
                case 3 -> {System.out.println("You have selected 75% Down Payment."); return "75% Down Payment";}
                case 0 -> isDone = true;
            }
        }
        return null;
    }

    private static double downPayment(String paymentPlan, double petPrice) {
        double amount = 0;

        switch(paymentPlan) {
            case "Full Payment" -> amount = 0;
            case "50% Down Payment" -> amount = petPrice * 0.5;
            case "75% Down Payment" -> amount = petPrice * 0.75;
        }
        return amount;
    }

    private static final int AS_TRANSACTION_NUM   = 0;
    private static final int AS_FIRST_NAME       = 1;
    private static final int AS_LAST_NAME        = 2;
    private static final int AS_EMAIL            = 3;
    private static final int AS_PET_ID           = 4;
    private static final int AS_PAYMENT_PLAN     = 5;
    private static final int AS_DOWN_PAYMENT     = 6;
    private static final int AS_BALANCE_LEFT     = 7;
    private static final int AS_DATE_REQUESTED   = 8;
    private static final int AS_FIELD_COUNT      = 9;

    private String buildAdoptionStatusRecord(Pet selectedPet, String paymentPlan, double downPayment, double balanceLeft, String transactionNum) {
        String parts[] = new String[AS_FIELD_COUNT];

        parts[AS_TRANSACTION_NUM] = transactionNum;
        parts[AS_FIRST_NAME] = this.getFirstName();
        parts[AS_LAST_NAME] = this.getLastName();
        parts[AS_EMAIL] = this.getEmailAddress();
        parts[AS_PET_ID] = selectedPet.getPetID();
        parts[AS_PAYMENT_PLAN] = paymentPlan;
        parts[AS_DOWN_PAYMENT] = String.format("%,.2f", downPayment);
        parts[AS_BALANCE_LEFT] = String.format("%,.2f", balanceLeft);
        parts[AS_DATE_REQUESTED] = java.time.LocalDate.now().toString();

        return String.join("|", parts);
    }

    private void cancelAdoption() {
        ArrayList<Reservation> reserveDetails = RecordManager.viewAllReserveRecords();
        String transactionNum = DataValidation.transactionNumberValidation("Enter Transaction Number from your Request of Adoption Confirmation Message: ");

        Reservation found = null;

        for(Reservation reserve : reserveDetails){
            if(reserve.getTransactionNum().equalsIgnoreCase(transactionNum)){
                found = reserve;
                break;
            }
        }

        if(found != null){
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
                            { "Date Requested", found.getDateRequested().toString()}
                        }
                );
        }else {
            System.out.println("No reservation found for transaction number: " + transactionNum);
        }

        char cancelAdoptionConfirmation = DataValidation.charChoiceValidation("Are you sure you want to cancel your reservation? (y/n): ", 'y', 'n');

        if(cancelAdoptionConfirmation == 'n'){
            System.out.println("Adoption Cancellation Cancelled.");
            return;
        }

        ArrayList<Pet> pets = FileHandler.viewAllPetRecords();
        for(Pet pet : pets){
            if(found.getPetID().equalsIgnoreCase(pet.getPetID())){
                pet.setAdoptionStatus(AdoptionStatus.AVAILABLE);
                break;
            }
        }

        FileHandler.removeRecord("RESERVED.txt", found.getPetID(), AS_PET_ID);
        System.out.println("Cancellation of Pet Request Adoption Successful.");
    }

    private void requestReturn() {
    }
}
