package utils;

import inventory.*;
import payment.FiftyPercentDP;
import payment.FullPayment;
import payment.PaymentPlan;
import payment.SeventyFivePercentDP;
import users.Customer;
import utils.generators.TransactionNumGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import static utils.RecordManager.viewAllReserveRecords;
import static utils.RecordManager.FILE_ADOPTED;
import static utils.RecordManager.FILE_RESERVED;
import static utils.RecordManager.FILE_RETURN_REQUEST;
import static utils.RecordManager.RES_TRANSACTION_NUM;

public class PetManager {
    private static PetManager instance;
    private final ArrayList<Pet> pets;

    private PetManager() {
        pets = FileHandler.viewAllPetRecords();
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public void addPet() {
        DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird");
        int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3);

        String petID = DataValidation.validatePetID(choice);
        while (findPetID(petID) != null
                || FileHandler.findRecord(RecordManager.FILE_INCOMING_LIST, petID, 0) != null) {
            System.out.println("Pet ID already exists. Please use another ID.");
            petID = DataValidation.validatePetID(choice);
        }
        String petName = DataValidation.validateString("Name: ");
        String petBreed = DataValidation.validateString("Breed: ");
        char petGender = DataValidation.charChoiceValidation("Gender", 'M', 'F');
        int petAge = DataValidation.validatePetAgeInput();
        double petPrice = DataValidation.validatePriceInput("Price of Pet(100.00 - 10,000.00): ");

        Pet pet = createNewPet(choice, petID, petName, petBreed, petGender, petAge, petPrice);

        char hasRecords = DataValidation.charChoiceValidation("Does the pet have any existing medical records?", 'Y',
                'N');
        if (hasRecords == 'Y') {
            promptMedicalRecord(pet);
        }

        if (saveIncomingPet(pet)) {
            System.out.println("Pet successfully submitted for approval and saved to INCOMING_PET_LIST.txt");
        } else {
            System.out.println("Pet was not saved.");
        }

    }

    private boolean saveIncomingPet(Pet pet) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RecordManager.FILE_INCOMING_LIST, true))) {
            writer.write(pet.toFileString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing incoming pet file.");
            return false;
        }
    }

    private Pet createNewPet(int choice, String id, String name, String breed,
                             char gender, int age, double price) {
        return switch (choice) {
            case 1 -> new Dog(id, name, breed, gender, age, price);
            case 2 -> new Cat(id, name, breed, gender, age, price);
            default -> new Bird(id, name, breed, gender, age, price);
        };
    }

    private boolean savePet(Pet pet) {
        boolean isFileSaved = false;
        while (!isFileSaved) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(RecordManager.FILE_PET_LIST, true))) {

                writer.write(pet.toFileString());
                writer.newLine();
                return true;

            } catch (IOException e) {
                System.out.println("Error writing file.");
                char retry = DataValidation.charChoiceValidation("Try again?", 'Y', 'N');
                if (retry != 'Y') {
                    isFileSaved = true;
                }
            }
        }
        return false;
    }

    public boolean registerPet(Pet pet) { // For incoming pets
        if (findPetID(pet.getPetID()) != null) {
            return false;
        }

        pet.setAdoptionStatus(AdoptionStatus.AVAILABLE);
        if (!savePet(pet)) {
            return false;
        }

        pets.add(pet);
        return true;
    }

    public void updatePetMedicalHistory() {
        String petID = DataValidation.validatePetIdInput();
        Pet pet = findPetID(petID);
        if (pet == null) {
            System.out.println("Pet " + petID + " not found.");
            return;
        }

        System.out.println("Current medical history: " + pet.getMedicalHistory());
        promptMedicalRecord(pet);

        if (FileHandler.updateRecord(RecordManager.FILE_PET_LIST, pet.getPetID(), 0, pet.toFileString())) {
            System.out.println("Medical record added.");
        } else {
            System.out
                    .println("Warning: could not save to " + RecordManager.FILE_PET_LIST + ". Changed in memory only.");
        }
    }

    private void promptMedicalRecord(Pet pet) {
        LocalDate date = DataValidation.validateDate("Enter date of the condition/treatment(MM-DD-YYYY)");
        String condition = DataValidation.validateMedicalRecord();
        pet.addMedicalHistory(date, condition);
    }

    public void viewSpecificPet() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.printMenu("PET TYPE",
                "#Select Pet Type",
                "Dog",
                "Cat",
                "Bird");
        int choice = DataValidation.intChoiceValidation("Select Option", 1, 2, 3);

        String type = switch (choice) {
            case 1 -> Dog.TYPE;
            case 2 -> Cat.TYPE;
            default -> Bird.TYPE;
        };

        ArrayList<Pet> petsOfType = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getType().equals(type)) {
                petsOfType.add(pet);
            }
        }

        if (petsOfType.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.displayPetsForStaff(petsOfType);

        char viewDetails = DataValidation.charChoiceValidation("Do you want to view specific pet details?", 'Y', 'N');
        if (viewDetails != 'Y') {
            return;
        }

        DisplayUtils.centeredTitle("VIEW SPECIFIC PET DETAILS", DisplayUtils.MENU_WIDTH);
        String petID = DataValidation.validatePetIdInput();

        for (Pet pet : petsOfType) {
            if (pet.idExists(petID)) {
                DisplayUtils.displayPetDetails(pet);
                return;
            }
        }
        System.out.println("No pet found with the given ID in this category.");
    }

    public ArrayList<Pet> getAllPets() {
        return pets;
    }

    public void searchByCategory() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }
        boolean isDone = false;

        while (!isDone) {
            DisplayUtils.printMenu("Search By Category",
                    "#Pet Details",
                    "Pet Name", "Type", "Breed", "Gender", "Age",
                    "#Pet Records",
                    "Adoption Status", "#Return", "Back");

            int choice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3, 4, 5, 6, 7);
            ArrayList<Pet> listOfPet = new ArrayList<>();

            switch (choice) {
                case 1: {
                    String petName = DataValidation.validateString("Enter pet name: ");
                    listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petName,
                            Pet::getPetName);
                    break;
                }
                case 2: {
                    String petType = "";
                    DisplayUtils.printMenu("PET TYPE",
                            "#Select Pet Type",
                            "Dog",
                            "Cat",
                            "Bird");
                    int choiceType = DataValidation.intChoiceValidation("Select Option",
                            1, 2, 3);

                    switch (choiceType) {
                        case 1 -> petType = "dog";
                        case 2 -> petType = "cat";
                        case 3 -> petType = "bird";
                        default -> throw new IllegalStateException("Unexpected choice: " + choiceType);
                    }
                    listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petType,
                            Pet::getType);
                    break;
                }
                case 3: {
                    String petBreed = DataValidation.validateString("Enter pet breed: ");
                    listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petBreed,
                            Pet::getBreed);
                    break;
                }
                case 4: {
                    char g = DataValidation.charChoiceValidation("Enter pet gender", 'M', 'F');
                    String petGender = (g == 'M') ? "Male" : "Female";
                    listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petGender,
                            Pet::getGender);
                    break;
                }
                case 5: {
                    int petAge = DataValidation.validatePetAgeInput();
                    listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petAge,
                            Pet::getAge);
                    break;
                }
                case 6: {
                    String petStatus = "";
                    DisplayUtils.printMenu("PET TYPE",
                            "#Select Adoption Status",
                            AdoptionStatus.AVAILABLE.toString(),
                            AdoptionStatus.ADOPTION_REVIEW.toString(),
                            AdoptionStatus.CANCELLATION_REVIEW.toString(),
                            AdoptionStatus.ADOPTED.toString(),
                            AdoptionStatus.RETURN_PENDING.toString());
                    int choiceType = DataValidation.intChoiceValidation("Select Option",
                            1, 2, 3, 4, 5);

                    switch (choiceType) {
                        case 1 -> petStatus = AdoptionStatus.AVAILABLE.toString();
                        case 2 -> petStatus = AdoptionStatus.ADOPTION_REVIEW.toString();
                        case 3 -> petStatus = AdoptionStatus.CANCELLATION_REVIEW.toString();
                        case 4 -> petStatus = AdoptionStatus.ADOPTED.toString();
                        case 5 -> petStatus = AdoptionStatus.RETURN_PENDING.toString();
                        default -> throw new IllegalStateException("Unexpected choice: " + petStatus);
                    }
                    listOfPet = FileHandler.filterRecords(RecordManager.FILE_PET_LIST, petStatus,
                            Pet::getAdoptionStatus);
                    break;
                }
                case 7: {
                    isDone = true;
                    break;
                }
            }

            if (choice == 7) {
                continue;
            }

            if (listOfPet.isEmpty()) {
                System.out.println("No pets found.");
            } else {
                DisplayUtils.displayPetsForStaff(listOfPet);
            }
        }
    }

    public Pet findPetID(String petID) {
        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                return pet;
            }
        }
        return null;
    }

    public void updatePetStatus(String petID, AdoptionStatus adoptionStatus) {
        Pet pet = findPetID(petID);
        if (pet == null) {
            return;
        }

        pet.setAdoptionStatus(adoptionStatus);

        boolean isSaved = FileHandler.updateRecord(RecordManager.FILE_PET_LIST, pet.getPetID(),
                0, pet.toFileString());
        if (!isSaved) {
            System.out.println("Warning: " + petID + " is not in " + RecordManager.FILE_PET_LIST
                    + ". Status changed in memory only.");
        }
    }

    public void sortByCategory() {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.printMenu("Enter Desired Category of Pet:", "Dog", "Cat", "Bird", "#Exit", "Go Back");
        int categoryChoice = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3, 4);
        if (categoryChoice == 4) {
            return;
        }
        DisplayUtils.printMenu("Sort By..", "Age", "Price", "Gender", "#Exit", "Go Back");
        int sortBy = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3, 4);

        if (sortBy == 4) {
            return;
        }

        DisplayUtils.printMenu("Order: ", "Ascending", "Descending", "#Exit", "Go Back");
        int isAscendingOrDescending = DataValidation.intChoiceValidation("Enter choice", 1, 2, 3);
        if (isAscendingOrDescending == 3) {
            return;
        }
        sortAndShow(categoryChoice, sortBy, isAscendingOrDescending == 1);
    }

    private void sortAndShow(int categoryChoice, int sortBy, boolean isAscendingOrDescending) {
        String type = switch (categoryChoice) {
            case 1 -> Dog.TYPE;
            case 2 -> Cat.TYPE;
            default -> Bird.TYPE;
        };

        ArrayList<Pet> sorted = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getType().equals(type)) {
                sorted.add(pet);
            }
        }

        if (sorted.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        Comparator<Pet> comparator = switch (sortBy) {
            case 1 -> Comparator.comparingInt(Pet::getAge);
            case 2 -> Comparator.comparingDouble(Pet::getPrice);
            default -> Comparator.comparing(Pet::getGender);
        };

        if (!isAscendingOrDescending) {
            comparator = comparator.reversed();
        }

        sorted.sort(comparator);
        DisplayUtils.displayPetsForStaff(sorted);

    }

    // -------------CUSTOMER METHODS
    // PET AVAILABLE
    public ArrayList<Pet> getAvailablePets() {
        ArrayList<Pet> pets = FileHandler.viewAllPetRecords();

        return availablePets(pets);
    }

    public ArrayList<Pet> availablePets(ArrayList<Pet> pets) {
        ArrayList<Pet> availablePets = new ArrayList<>();
        ArrayList<String> reserve = new ArrayList<>();
        for (Reservation pet : viewAllReserveRecords()) {
            reserve.add(pet.getPetID().toUpperCase());
        }

        for (Pet pet : pets) {
            boolean isAvailable = pet.getAdoptionStatus() == AdoptionStatus.AVAILABLE;
            if (isAvailable && !reserve.contains(pet.getPetID().toUpperCase())) {
                availablePets.add(pet);
            }
        }
        return availablePets;
    }

    // ------------REQUEST ADOPTION (CUSTOMER)
    public void requestAdoption(Customer customer) {
        ArrayList<Pet> availablePets = getAvailablePets();

        if (availablePets.isEmpty()) {
            DisplayUtils.centeredTitle("REQUEST ADOPTION", 100);
            System.out.println("No pets available for adoption.");
            return;
        }

        DisplayUtils.customerPetHeader("REQUEST ADOPTION");
        DisplayUtils.displayPetsForCustomer(availablePets);

        requestAdoptionCustomer(availablePets, customer);
    }

    public void requestAdoptionCustomer(ArrayList<Pet> availablePets, Customer customer) {
        String petID = DataValidation.validatePetIdInput();

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

        char requestAdoption = DataValidation
                .charChoiceValidation("\nDo you want to request adoption for Pet ID: " + petID + "?", 'y', 'n');

        if (requestAdoption == 'N') {
            System.out.println("Adoption request cancelled.");
            return;
        } else {
            System.out.println("Fill out the following information for the adoption request:");
        }

        PaymentPlan paymentPlan = paymentPlanSelection();
        if (paymentPlan == null) {
            System.out.println("Adoption request cancelled.");
            return;
        }

        double downPaymentAmount = paymentPlan.computeDownPayment(selectedPet.getPrice());
        double balanceLeft = selectedPet.getPrice() - downPaymentAmount;

        LocalDate date = DataValidation.validateDate("Enter date for claiming of pet (mm-dd-yyyy): ");
        while (!DataValidation.dateForClaimingPet(date)) {
            System.out.println("Date should be on or within 7 days after the adoption request.");
            date = DataValidation.validateDate("Enter date for claiming of pet: ");
        }

        System.out.println("\nConfirm adoption request by entering your email address.");
        String confirmByEmail = DataValidation.validateEmailAddress(); // Get email

        while (!confirmByEmail.equalsIgnoreCase(customer.getEmailAddress())) { // check if customer's actual email
            System.out.println("Email is not found. Please enter another email.");
            confirmByEmail = DataValidation.validateEmailAddress();
        }

        String transactionNum = TransactionNumGenerator.transactionNumGenerator();
        RecordManager rm = new RecordManager();
        String reservationRecord = rm.buildAdoptionRequestRecord(customer.getFirstName(), customer.getLastName(),
                customer.getEmailAddress(), selectedPet, paymentPlan.getName(), downPaymentAmount, balanceLeft, transactionNum,
                date);
        FileHandler.appendRecord(FILE_RESERVED, reservationRecord);
        updatePetStatus(selectedPet.getPetID(), AdoptionStatus.ADOPTION_REVIEW);

        DisplayUtils.printSummaryBox("ADOPTION REQUEST DETAILS",
                new String[][] {
                        { "Transaction Number", transactionNum },
                        { "Pet ID", selectedPet.getPetID() },
                        { "Payment Plan", paymentPlan.getName() },
                        { "Down Payment", String.valueOf(downPaymentAmount) },
                        { "Balance", String.valueOf(balanceLeft) },
                        { "Date Requested", LocalDate.now().toString() },
                        { "Date Claiming", date.toString() }
                });
        System.out.println("Adoption request for Pet ID: " + petID + " with transaction number " + transactionNum
                + " has been submitted for review.");
    }

    private static PaymentPlan paymentPlanSelection() {
        DisplayUtils.printMenu("SELECT PAYMENT PLAN",
                "#Available Payment Plans",
                "Full Payment",
                "50% Down Payment",
                "75% Down Payment");

        int choice = DataValidation.intChoiceValidation("Select Payment Plan for Adoption Request: ", 1, 2, 3);

        PaymentPlan plan = switch(choice){
            case 1 -> new FullPayment();
            case 2 -> new FiftyPercentDP();
            default -> new SeventyFivePercentDP();
        };

        System.out.println("You have selected " + plan.getName() + ".");
        return plan;
    }

    // ------------CANCEL REQUEST ADOPTION (CUSTOMER)
    public void cancelAdoption(Customer customer) {
        DisplayUtils.centeredTitle("CANCEL REQUEST ADOPTION", 100);
        ArrayList<Reservation> reserveDetails = RecordManager.viewAllReserveRecords();
        if (reserveDetails.isEmpty()) {
            System.out.println("No pets are reserved.");
            return;
        }
        String transactionNum = DataValidation.transactionNumberValidation();

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

        if (FileHandler.findRecord(RecordManager.FILE_CANCELLATION_REQUEST, transactionNum,
                RES_TRANSACTION_NUM) != null) {
            System.out.println(
                    "A cancellation request for " + found.getTransactionNum() + " is already being processed.");
            return;
        }

        System.out.println("Reservation found!");
        DisplayUtils.printSummaryBox("PET DETAILS",
                new String[][] {
                        { "Customer's Name", found.getFirstName() + " " + found.getLastName() },
                        { "Customer's Email", found.getEmail() },
                        { "Transaction Number", found.getTransactionNum() },
                        { "Pet ID", found.getPetID() },
                        { "Payment Plan", found.getPaymentPlan() },
                        { "Down Payment", String.valueOf(found.getDownPayment()) },
                        { "Balance", String.valueOf(found.getBalanceLeft()) },
                        { "Date Requested", found.getDateRequested().toString() },
                        { "Date Claiming", found.getDateClaiming().toString() }
                });

        char cancelAdoptionConfirmation = DataValidation
                .charChoiceValidation("Are you sure you want to cancel your reservation?: ", 'y', 'n');

        if (cancelAdoptionConfirmation == 'N') {
            System.out.println("Adoption Cancellation Cancelled.");
            return;
        }

        String reservationRecord = FileHandler.findRecord(
                RecordManager.FILE_RESERVED, transactionNum, RES_TRANSACTION_NUM);

        if (reservationRecord == null) {
            System.out.println("Reservation record not found. The cancellation request was not submitted.");
            return;
        }

        FileHandler.appendRecord(RecordManager.FILE_CANCELLATION_REQUEST, reservationRecord);
        updatePetStatus(found.getPetID(), AdoptionStatus.CANCELLATION_REVIEW);
        System.out.println(
                "Your cancellation request of pet request adoption is now being processed. Wait for the Admin or Coordinator to approve your request.");
    }

    // ------------REQUEST RETURN ADOPTION (CUSTOMER)
    public void requestReturn(Customer customer) {
        DisplayUtils.centeredTitle("REQUEST PET RETURN", 100);
        ArrayList<Reservation> adopted = RecordManager.viewAllPetAdoptedRecords();

        if (adopted.isEmpty()) {
            System.out.println("No pets available for return.");
            return;
        }
        String transactionNum = DataValidation.transactionNumberValidation();

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

        if (FileHandler.findRecord(FILE_RETURN_REQUEST, transactionNum, RES_TRANSACTION_NUM) != null) {
            System.out.println("A return request for " + found.getTransactionNum() + " is already being processed.");
            return;
        }

        String adoptedPet = FileHandler.findRecord(FILE_ADOPTED, transactionNum, RES_TRANSACTION_NUM);

        if (adoptedPet == null) {
            System.out.println("Adoption record not found.");
            return;
        }

        String dateAdopted = adoptedPet.split("\\s*\\|\\s*")[RecordManager.AD_DATE_FINALIZED];

        System.out.println("Adoption found!\n");
        DisplayUtils.printSummaryBox("PET DETAILS",
                new String[][] {
                        { "Customer's Name", found.getFirstName() + " " + found.getLastName() },
                        { "Transaction Number", found.getTransactionNum() },
                        { "Pet ID", found.getPetID() },
                        { "Date Adopted", dateAdopted },
                });

        char returnAdoptionConfirmation = DataValidation
                .charChoiceValidation("Are you sure you want to return your pet?", 'y', 'n');

        if (returnAdoptionConfirmation == 'N') {
            System.out.println("Return Adoption Request Cancelled.");
            return;
        }

        FileHandler.appendRecord(FILE_RETURN_REQUEST, adoptedPet);
        updatePetStatus(found.getPetID(), AdoptionStatus.RETURN_PENDING);
        System.out.println(
                "Your return request is now being processed. Wait for the Admin or Coordinator to approve your request.");
    }

    public boolean savePets() {
        return FileHandler.savePets(pets);
    }

    public boolean approveIncomingPet(Pet pet) {
        if (findPetID(pet.getPetID()) != null) {
            return false;
        }

        pet.setAdoptionStatus(AdoptionStatus.AVAILABLE);
        pets.add(pet);
        if (!savePets()) {
            pets.remove(pet); // keep memory and file in sync
            return false;
        }
        return true;
    }
}